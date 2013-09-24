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
import java.net.URLConnection;
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
	
	public void addDownload(String url) {
		URLDownload newFile= new URLDownload(url);
		if (newFile.getURL()!=null){
			downloads.add(newFile);
			checkDownloadSize(newFile);
		}
	}
	
	public void addDownload(String url, String size, boolean added) {
		URLDownload newFile= new URLDownload(url, added);
		newFile.setSize(size);
		downloads.add(newFile);
		checkDownloadSize(newFile);
	}
	
	/**
	 * This will move the selected download up the queue
	 * @param downloadId
	 */
	public void increasePriority(int downloadId){

		downloads.get(downloadId).setUpdated(true);
	}
	
	/**
	 * This will move the selected download down the queue
	 * @param downloadId
	 */
	public void decreasePriority(int downloadId){

		downloads.get(downloadId).setUpdated(true);
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
		String filename=newFile.getDestination()+"/"+newFile.getURL().toString().substring(newFile.getURL().toString().lastIndexOf('/')+1);
		File localFile = new File(filename);
		if (localFile.exists())
			if (localFile.length() < Long.parseLong(newFile.getSize())){
				newFile.setStatus(Details.PREVIOUSLY_STARTED);
			} else if (localFile.length() >= Long.parseLong(newFile.getSize())){
				newFile.setStatus(Details.FINISHED);					
			}
	}
}
