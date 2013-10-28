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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Vector;

/**
 * @author bugman
 *
 */
public class URLDownloadList extends DownloadDetails {

	private Vector<URLDownload> downloads;
	
	public URLDownloadList(){
		super("Downloads");
		downloads = new Vector<URLDownload>();
	}
	
	public Vector<URLDownload> getDownloads(){
		return downloads;
	}
	
	public void addDownload(String url, String destination) {
		URLDownload newFile= new URLDownload(url);
		newFile.setDestination(destination);
		if (newFile.getURL()!=null){
			downloads.add(newFile);
			checkDownloadSize(newFile);
		}
	}
	
	public void addDownload(String url, String destination, String size, boolean added) {
		URLDownload newFile= new URLDownload(url, added);
		if (newFile.getURL()!=null){
			newFile.setDestination(destination);
			if (size!="-1")
				newFile.setSize(size);
			downloads.add(newFile);
			checkDownloadSize(newFile);
		}
	}
	
	public void addDownload(URLDownload newDownload, int priority) {
		if ((downloads.size()==0)||
			(priority>downloads.size()))
			downloads.add(newDownload);
		else if (priority<downloads.size()){
			downloads.insertElementAt(newDownload, priority);
		}
	}
	
	public void addDownload(URL url, Podcast podcast, String size){
		URLDownload newFile = new URLDownload(url,false);
		newFile.setDestination(podcast.getDirectory());
		newFile.setPodcastId(podcast.getDatafile());
		if (size!="-1")
			newFile.setSize(size);
		downloads.add(newFile);
		checkDownloadSize(newFile);
	}
	/**
	 * This will move the selected download up the queue
	 * @param downloadId
	 */
	public void increasePriority(int downloadId){
		if (downloadId>0){
			Collections.swap(downloads, downloadId, downloadId-1);
			downloads.get(downloadId).setUpdated(true);
		}
	}
	
	/**
	 * This will move the selected download down the queue
	 * @param downloadId
	 */
	public void decreasePriority(int downloadId){
		if (downloadId<downloads.size()-2){
			Collections.swap(downloads, downloadId, downloadId+1);
			downloads.get(downloadId).setUpdated(true);
		}
	}
	
	public void checkDownloadSize(URLDownload newFile){
		if (Long.parseLong(newFile.getSize())==0){
			try {
				URLConnection stream = newFile.getURL().openConnection();
				int fileSize=stream.getContentLength();
				newFile.setSize(Long.toString(fileSize));
				newFile.setStatus(Details.NOT_STARTED);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String filename=newFile.getDestination();
		File localFile = new File(filename);
		if (localFile.exists())
			if (localFile.length() < Long.parseLong(newFile.getSize())){
				newFile.setStatus(Details.PREVIOUSLY_STARTED);
			} else if (localFile.length() >= Long.parseLong(newFile.getSize())){
				newFile.setStatus(Details.FINISHED);					
			}
	}

	public void cancelDownload(int download) {
		if ((download >=0)&&(download<downloads.size())){
			downloads.get(download).setStatus(Details.DO_NOT_DOWNLOAD);
		}
	}
	
	public void removeDownload(int download) {
		if ((download >= 0)&&(download<downloads.size())){
			// TODO finish code here
		}
	}

	public void deleteDownload(int download) {
		if ((download >=0)&&(download<downloads.size())){
			downloads.get(download).isRemoved();
			restartDownload(download);
		}
	}

	public boolean restartDownload(int download) {
		if ((download >=0)&&(download<downloads.size())){
			if (deleteFile(downloads.get(download))){
				downloads.get(download).setStatus(Details.NOT_STARTED);
				return true;
			}
		}
		return false;
	}
	
	public boolean deleteFile(URLDownload download){
		File localFile = new File(download.getDestination());
		if (localFile.exists()){
			// delete the file
			localFile.delete();
			return true;
		}
		return false;
	}

	public int size() {
		return downloads.size();
	}

	public int findDownload(URL url) {
		int count=0;
		for (URLDownload download : downloads){
			if (download.getURL().toString().equalsIgnoreCase(url.toString()))
				return count;
			count++;
		}
		return -1;
	}
}
