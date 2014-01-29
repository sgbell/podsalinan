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
	private URLDownloadList downloadList;
	private ProgSettings progSettings;

	public DownloadQueue(){
		
	}
	
	public DownloadQueue(ProgSettings progSettings, URLDownloadList listOfDownloads){
		this.progSettings=progSettings;
		downloadList = listOfDownloads;
	}
	
	public void run(){
		downloaders = new Vector<Downloader>();
		while (!progSettings.isFinished()){
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
		}
	}

	/**
	 * Starts a new Download.
	 * @param newDownloader
	 */
	public void startDownload(Downloader newDownloader){
		downloaders.add(newDownloader);
		newDownloader.addListener(this);
		Thread downloadThread = new Thread(newDownloader,"Downloader");
		downloadThread.start();
		//System.out.println("Download Started: "+newDownloader.getFilenameDownload());		
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
		
	public URLDownloadList getDownloadList(){
		return downloadList;
	}
	
	public void setProgSettings(ProgSettings globalSettings){
		progSettings = globalSettings;
	}
	
	public Vector<Downloader> getDownloaders(){
		return downloaders;
	}
}
