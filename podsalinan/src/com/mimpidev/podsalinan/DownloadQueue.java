/*******************************************************************************
 * Copyright (c) 2011 Sam Bell.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or  any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See
 * the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Sam Bell - initial API and implementation
 ******************************************************************************/
/**
 * 
 */
package com.mimpidev.podsalinan;

import java.util.Vector;

import com.mimpidev.podsalinan.data.URLDetails;
import com.mimpidev.podsalinan.data.Podcast;
import com.mimpidev.podsalinan.data.URLDownload;

public class DownloadQueue implements Runnable, RunnableCompleteListener{
	private Vector<Downloader> downloaders;	// Collection of running downloaders
	private DataStorage data=null;
	private boolean isFinished;

	public DownloadQueue(){
		
	}
	
	public DownloadQueue(DataStorage newData){
		setData(newData);
	}
	
	public void run(){
		downloaders = new Vector<Downloader>();
		isFinished=false;
		if (data!=null){
			while (!data.getSettings().isFinished()){
				/*
				 *  Psuedo code:
				 *  
				 *  while ((urldownloadlist.numberOfDownloadsNotCurrentlyDownload)&&
				 *         (countActiveDownloaders<settings.getValue("maxDownloaders")){
				 *       urldownloadList.getNextDownloadNotCurrentlyDownloading
				 *       Create Downloader with next URLDownload object
				 *       start downloader        
				 *  }
				 *  
				 *  sleep
				 */
				int maxDownloaders;
				try {
					maxDownloaders=Integer.parseInt(data.getSettings().getSettingValue("maxDownloaders"));
				} catch (NumberFormatException e){
					maxDownloaders=3;
				}
				
				if ((data.getUrlDownloads().getNumberOfQueuedDownloads()>0)&&
					(downloaders.size()<maxDownloaders)){
					//search downloadList for next queued item.
					URLDownload download = data.getUrlDownloads().getHighestQueuedItem();
					if (download!=null){
						/* Change download status here. otherwise if it is set by the Downloader, it may
						 * Assign the download to multiple downloaders.
						 */
						//System.out.println("Adding to Download System: "+download.getURL().toString());
						download.setStatus(URLDetails.CURRENTLY_DOWNLOADING);
						Downloader newDownloader = new Downloader(download,data.getFileSystemSlash());
						startDownload(newDownloader);
					}
				}
			}
			// Stop all downloaders when the program is exiting
			synchronized(downloaders){
				for (Downloader downloader : downloaders)
					downloader.setStopDownload(true);
			}
			
		}
		
		// While Downloaders still shutting down we need to put a pause in until all downloaders have finished
		while (downloaders.size()>0){
			synchronized(data.getFinishWait()){
				try {
					data.getFinishWait().wait(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		// Set incomplete downloads to queued, on exit.
		int downloadCount=0;
		boolean foundQueuedItem=false;
		while ((downloadCount<data.getUrlDownloads().size())&&
			   (!foundQueuedItem)){
			URLDownload download =data.getUrlDownloads().getDownloads().get(downloadCount);
			// If download is set to incomplete set it to queued for next start
			if ((download.getStatus()==URLDetails.INCOMPLETE_DOWNLOAD)||
				(download.getStatus()==URLDetails.CURRENTLY_DOWNLOADING))
				download.setStatus(URLDetails.DOWNLOAD_QUEUED);
			// If Queued, exit the while loop
			if (download.getStatus()==URLDetails.DOWNLOAD_QUEUED)
				foundQueuedItem=true;
			downloadCount++;
		}			
		isFinished=true;
		synchronized(data.getFinishWait()){
			data.getFinishWait().notify();
		}
	}

	/**
	 * Starts a new Download. Adds the listener to it, which will then be notified in turn when 
	 * the download has finished, and remove the downloader from the array of downloaders.
	 * @param newDownloader
	 */
	public void startDownload(Downloader newDownloader){
		synchronized(downloaders){
			downloaders.add(newDownloader);
		}
		newDownloader.addListener(this);
		Thread downloadThread = new Thread(newDownloader,"Downloader");
		downloadThread.start();
		URLDownload download = newDownloader.getURLDownload();
		updatePodcastEpisodeStatus(download.getPodcastSource(),download.getURL().toString(),URLDetails.CURRENTLY_DOWNLOADING);
	}
	
	public boolean updatePodcastEpisodeStatus(String podcastId, String url, int newStatus){
		Vector<Podcast> podcasts = data.getPodcasts().getList();
		Podcast selectedPodcast=null;
		if (podcastId!=null){
			synchronized(podcasts){
				int podcastCount=0;
				while ((selectedPodcast==null)&&(podcastCount<podcasts.size())){
						if (podcasts.get(podcastCount).getDatafile().equalsIgnoreCase(podcastId))
							selectedPodcast=podcasts.get(podcastCount);
						else
							podcastCount++;
				}
				if (selectedPodcast!=null){
					while (selectedPodcast.getEpisodeByURL(url)==null){
						// If the podcast Episodes have not been loaded yet, we will need to pause this thread
						// and check it again. until it exists. The download will have already started :)
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							Podsalinan.debugLog.printStackTrace(e.getStackTrace());
						}
					}
					selectedPodcast.getEpisodeByURL(url).setStatus(newStatus);
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public void notifyOfThreadComplete(Runnable runnable) {
		Downloader downloader = (Downloader) runnable;
		URLDownload download = downloader.getURLDownload();
		
		int percentage = (int)((double)download.getDestinationFile().length()/(Double.parseDouble(download.getSize()))*100);
		
		if (percentage==100){
			download.setStatus(URLDetails.FINISHED);
			if (download.getPodcastSource().length()>0)
				data.getUrlDownloads().deleteDownload(download);
			downloaders.remove(downloader);
		} else if ((percentage<100)&&(download.getStatus()!=URLDetails.DESTINATION_INVALID)){
			downloaders.remove(downloader);
			synchronized(download){
				try {
					download.wait(5000);
				} catch (InterruptedException e) {
				}
			}
			data.getUrlDownloads().decreasePriority(download);
			download.setStatus(URLDetails.DOWNLOAD_QUEUED);
		} else if ((!download.getDestinationFile().isFile())||
				   (!download.getDestinationFile().exists())){
			downloaders.remove(downloader);
			synchronized(download){
				try {
					download.wait(5000);
				} catch (InterruptedException e) {
				}
			}
			data.getUrlDownloads().decreasePriority(download);
			download.setStatus(URLDetails.DESTINATION_INVALID);
		} else if (percentage>100){
			downloaders.remove(downloader);
			synchronized(download){
				try {
					download.wait(5000);
				} catch (InterruptedException e) {
				}
			}
			data.getUrlDownloads().decreasePriority(download);
			download.setStatus(URLDetails.DOWNLOAD_FAULT);
		}
		int newEpisodeStatus=0;
		switch (download.getStatus()){
			case URLDetails.FINISHED:
			case URLDetails.DOWNLOAD_QUEUED:
				newEpisodeStatus=download.getStatus();
				break;
			case URLDetails.DOWNLOAD_FAULT:
				newEpisodeStatus=URLDetails.DOWNLOAD_QUEUED;
		}
		updatePodcastEpisodeStatus(download.getPodcastSource(),download.getURL().toString(), newEpisodeStatus);

	}
		
	public Vector<Downloader> getDownloaders(){
		return downloaders;
	}

	/**
	 * @return the data
	 */
	public DataStorage getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(DataStorage data) {
		this.data = data;
	}

	/**
	 * @return the isFinished
	 */
	public boolean isFinished() {
		return isFinished;
	}

	/**
	 * @param isFinished the isFinished to set
	 */
	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
}
