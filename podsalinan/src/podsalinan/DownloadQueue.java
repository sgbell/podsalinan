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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;

public class DownloadQueue implements Runnable, RunnableCompleteListener{
	private Vector<Downloader> downloaders;	// Collection of running downloaders
	private DataStorage data=null;

	public DownloadQueue(){
		
	}
	
	public DownloadQueue(DataStorage newData){
		setData(newData);
	}
	
	public void run(){
		downloaders = new Vector<Downloader>();
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
						download.setStatus(Details.CURRENTLY_DOWNLOADING);
						Downloader newDownloader = new Downloader(download);
						startDownload(newDownloader);
					}
				}
			}
			// Stop all downloaders when the program is exiting
			for (Downloader downloader : downloaders)
				downloader.setStopDownload(true);
			
			// Set incomplete downloads to queued, on exit.
			int downloadCount=0;
			boolean foundQueuedItem=false;
			while ((downloadCount<data.getUrlDownloads().size())&&
				   (!foundQueuedItem)){
				URLDownload download =data.getUrlDownloads().getDownloads().get(downloadCount);
				// If download is set to incomplete set it to queued for next start
				if (download.getStatus()==Details.INCOMPLETE_DOWNLOAD)
					download.setStatus(Details.DOWNLOAD_QUEUED);
				// If Queued, exit the while loop
				if (download.getStatus()==Details.DOWNLOAD_QUEUED)
					foundQueuedItem=true;
				downloadCount++;
			}			
		}
	}

	/**
	 * Starts a new Download. Adds the listener to it, which will then be notified in turn when 
	 * the download has finished, and remove the downloader from the array of downloaders.
	 * @param newDownloader
	 */
	public void startDownload(Downloader newDownloader){
		downloaders.add(newDownloader);
		newDownloader.addListener(this);
		Thread downloadThread = new Thread(newDownloader,"Downloader");
		downloadThread.start();
		System.out.println("Download Started: "+newDownloader.getFilenameDownload());		
	}
	
	@Override
	public void notifyOfThreadComplete(Runnable runnable) {
		for (int dc=0; dc < downloaders.size(); dc++){
			if ((downloaders.get(dc).getResult()==1)||(downloaders.get(dc).downloadCompleted()==100)){
				//System.out.println("Finished: "+downloaders.get(dc).getFilenameDownload());
				// Locking on the queueGui so it can't be updated while an item is being released.
				/*
				if (downloaders.get(dc).isPodcast()){
				} else {
				}
				downloaders.remove(dc);
					*/
			}
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
}
