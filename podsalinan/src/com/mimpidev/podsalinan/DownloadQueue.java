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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Vector;

import com.mimpidev.dev.debug.Log;
import com.mimpidev.podsalinan.data.URLDetails;
import com.mimpidev.podsalinan.data.Podcast;
import com.mimpidev.podsalinan.data.URLDownload;

public class DownloadQueue implements Runnable, RunnableCompleteListener{
	private static Vector<Downloader> downloaders;	// Collection of running downloaders
	private static DataStorage data=null;
	private boolean isFinished;
	private	Object downloadQueueObject;

	private boolean debug=false;

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
				int maxDownloaders=0;
				try {
					maxDownloaders=Integer.parseInt(data.getSettings().getSettingValue("maxDownloaders"));
				} catch (NumberFormatException e){
					maxDownloaders=3;
				}
				/* Because of debugging I had noticed that there can be lots and lots of downloader threads
				 * running, so I'm rewriting the downloader so this class starts them all at startup
				 * and they sleep if they have nothing to do. They then wake up when they are passed a download.
				 */
				int downloadCount=0;
				// Starting download threads
				synchronized (downloaders){
					if (downloaders.size()<maxDownloaders){
						for (downloadCount=downloaders.size(); downloadCount<maxDownloaders; downloadCount++){
							Downloader newDownloader = new Downloader(data.getFileSystemSlash());
							downloaders.add(newDownloader);
							newDownloader.addListener(this);
							Thread downloadThread = new Thread(newDownloader,"Downloader");
							downloadThread.start();
						}
					}
				
					// If the number of downloaders has been dropped, we need to make sure that the system
					// reflects this, and removes a downloader
				}
				while (downloaders.size()>maxDownloaders){
					if (!downloaders.get(downloaders.size()-1).isStopThread()){
						downloaders.get(downloaders.size()-1).endThread();
					}
				}

				synchronized(data.getUrlDownloads()){
					while (data.getUrlDownloads().getHighestQueuedItem()!=null && sleepingDownloaderFound()){
						//search downloadList for next queued item.
						URLDownload download = data.getUrlDownloads().getHighestQueuedItem();
						if (download!=null){
							boolean downloadStarted=false;
							downloadCount=0;
							while (downloadCount<downloaders.size() && !downloadStarted){
								Downloader downloader=downloaders.get(downloadCount);
								if (!downloader.currentlyDownloading()){
									download.setStatus(URLDetails.CURRENTLY_DOWNLOADING);
									downloader.setDownload(download);
									startDownload(downloader);
									downloadStarted=true;
								} else {
									if (downloader.getURLDownload()!=null && 
										downloader.getURLDownload().getUid().equals(download.getUid())){
										downloadStarted=true;
									}
								}
								downloadCount++;
							}
						}
					}
				}
				
				try {
					synchronized(getDownloadQueueObject()){
						getDownloadQueueObject().wait();
					}
				} catch (InterruptedException e) {
					if (Log.isDebug())Log.printStackTrace(e.getStackTrace());
				}
			}
			// Stop all downloaders when the program is exiting
			synchronized(downloaders){
				for (Downloader downloader : downloaders){
					downloader.endThread();
				}
			}
			
		}
		
		// While Downloaders still shutting down we need to put a pause in until all downloaders have finished
		while (downloaders.size()>0){
		//	downloaders.get(0).endThread();
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
		synchronized(data.getUrlDownloads()){
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
		}
		isFinished=true;
		synchronized(data.getFinishWait()){
			data.getFinishWait().notify();
		}
	}

	private boolean sleepingDownloaderFound() {
		boolean sleeping=false;
		synchronized(downloaders){
			for (Downloader downloader: downloaders){
				if (!downloader.currentlyDownloading())
					sleeping=true;
			}
		}
		return sleeping;
	}

	/**
	 * Starts a new Download. Adds the listener to it, which will then be notified in turn when 
	 * the download has finished, and remove the downloader from the array of downloaders.
	 * @param newDownloader
	 */
	public void startDownload(Downloader newDownloader){
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
							if (Log.isDebug())Log.printStackTrace(e.getStackTrace());
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
		
		if (download!=null){
			synchronized(download){
				// download = null when downloader is asleep and is nut currently working. This can happen when the system is being shutdown
				if (download.getURL().length()>0){
					int percentage;
					// if download doesn't have a stored value (because the server wouldn't supply one, set the download percentage to 100%
					if (download.getSize().length()==0 || download.getSize().equals("-1")){
						percentage=100;
					} else {
					    percentage = (int)((double)download.getDestinationFile().length()/(Double.parseDouble(download.getSize()))*100);
					}
					if (percentage==100){
						download.setStatus(URLDetails.FINISHED);
						if (download.getPodcastSource().length()>0)
							data.getUrlDownloads().deleteDownload(download);
						else {
							try {
								InputStream testFileStream = new FileInputStream(download.getDestinationFile());
								if (debug) if (Log.isDebug())Log.logInfo(this, "Destination File:"+download.getDestinationFile().toString());
								BufferedReader reader = new BufferedReader(new InputStreamReader(testFileStream));
								String line=reader.readLine();
								if (line != null){
									if (debug) if (Log.isDebug())Log.logInfo(this, "First Line:"+line.toString());
									if (line.contains("rss") || line.contains("xml")){
										testFileStream.close();
										Podcast newPodcast = new Podcast(download);
										// The following makes sure we don't have multiple podcasts uid's
										while (data.getPodcasts().getPodcastByUid(newPodcast.getDatafile())!=null){
											newPodcast.setDatafile(newPodcast.createUID(newPodcast.getName()+(new Date().toString())));
										}
										data.getPodcasts().add(newPodcast);
										// Because it is a podcast initiator we want to remove it from the download list automatically
										data.getUrlDownloads().deleteDownload(download);
									}
								}
								testFileStream.close();
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					} else if ((percentage<100)&&(download.getStatus()!=URLDetails.DESTINATION_INVALID)){
						if (debug) if (Log.isDebug())Log.logInfo(this, "download.status="+download.getStatus());
						try {
							download.wait(5000);
						} catch (InterruptedException e) {
						}
						data.getUrlDownloads().decreasePriority(download);
						download.setStatus(URLDetails.DOWNLOAD_QUEUED);
					} else if ((!download.getDestinationFile().isFile())||
							   (!download.getDestinationFile().exists())){
						try {
							download.wait(5000);
						} catch (InterruptedException e) {
						}
						data.getUrlDownloads().decreasePriority(download);
						download.setStatus(URLDetails.DESTINATION_INVALID);
					} else if (percentage>100){
						try {
							download.wait(5000);
						} catch (InterruptedException e) {
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
					if (data.getSettings().isFinished()){
						synchronized(downloaders){
							downloaders.remove(downloader);
						}
					}
				} 
			}			
		}
		downloader.downloaderFinished(true);
		downloader.clearDownload();
		if (((download==null ||
			download.getURL().equals("")) &&
			(data.getSettings().isFinished())) ||
			(downloader.isStopThread())){
			downloaders.remove(downloader);
		}

		synchronized(getDownloadQueueObject()){
			getDownloadQueueObject().notify();
		}
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

	/**
	 * @return the downloadQueueObject
	 */
	public Object getDownloadQueueObject() {
		return downloadQueueObject;
	}

	/**
	 * @param downloadQueueObject the downloadQueueObject to set
	 */
	public void setDownloadQueueObject(Object downloadQueueObject) {
		this.downloadQueueObject = downloadQueueObject;
	}
	
	/**
	 * 
	 * @return
	 */
	public static int getDownloadSpeedLimit(int currentSpeed){
		int downloadLimit=0;
		int currentTotalSpeed=0;
		try {
			downloadLimit=Integer.parseInt(data.getSettings().getSettingValue("downloadLimit"));
		} catch (NumberFormatException e){
			downloadLimit=300;
		}
		
		for (Downloader downloader : downloaders){
			currentTotalSpeed += downloader.getCurrentDownloadSpeed();
		}
		if (currentTotalSpeed>downloadLimit){
			return currentSpeed-(currentTotalSpeed-downloadLimit);
		} else if (currentTotalSpeed<downloadLimit/downloaders.size()){
			return currentSpeed+(downloadLimit-currentTotalSpeed);
		} else {
			return currentSpeed;
		}
	}
}
