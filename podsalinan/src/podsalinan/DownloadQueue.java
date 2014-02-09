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
package podsalinan;

import java.util.Vector;

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
				if ((data.getUrlDownloads().getNumberOfQueuedDownloads()>0)&&
					(downloaders.size()<Integer.parseInt(data.getSettings().getSettingValue("maxDownloaders")))){
					//search downloadList for next queued item.
					URLDownload download = data.getUrlDownloads().getHighestQueuedItem();
					if (download!=null){
						/* Change download status here. otherwise if it is set by the Downloader, it may
						 * Assign the download to multiple downloaders.
						 */
						//System.out.println("Adding to Download System: "+download.getURL().toString());
						download.setStatus(Details.CURRENTLY_DOWNLOADING);
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
			if ((download.getStatus()==Details.INCOMPLETE_DOWNLOAD)||
				(download.getStatus()==Details.CURRENTLY_DOWNLOADING))
				download.setStatus(Details.DOWNLOAD_QUEUED);
			// If Queued, exit the while loop
			if (download.getStatus()==Details.DOWNLOAD_QUEUED)
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
		//System.out.println("Download Started: "+newDownloader.getFilenameDownload());		
	}
	
	@Override
	public void notifyOfThreadComplete(Runnable runnable) {
		Downloader downloader = (Downloader) runnable;
		URLDownload download = downloader.getURLDownload();
		
		int percentage = (int)((double)download.getDestinationFile().length()/(Double.parseDouble(download.getSize()))*100);
		
		//TODO The following if statements need to also hit up the podcast if the download is a podcast and set the
		//     episode status to finished.
		if (percentage==100){
			download.setStatus(Details.FINISHED);
			downloaders.remove(downloader);
		} else if (percentage<100){
			downloaders.remove(downloader);
			synchronized(download){
				try {
					download.wait(5000);
				} catch (InterruptedException e) {
				}
			}
			data.getUrlDownloads().decreasePriority(download);
			download.setStatus(Details.DOWNLOAD_QUEUED);
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
			download.setStatus(Details.DOWNLOAD_FAULT);
		} else if (percentage>100){
			downloaders.remove(downloader);
			synchronized(download){
				try {
					download.wait(5000);
				} catch (InterruptedException e) {
				}
			}
			data.getUrlDownloads().decreasePriority(download);
			download.setStatus(Details.DOWNLOAD_FAULT);
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
}
