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
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.table.DefaultTableModel;

public class Downloader extends NotifyingRunnable{
	private URLDownload downloadItem;
	private long fileSize;
	private int percentage=0,
				result;
	
	public String getFilenameDownload(){
		return downloadItem.getURL().toString();
	}
	
	public Downloader(URL urlDownload, String outputFile, String size){
		downloadItem = new URLDownload();
		downloadItem.setURL(urlDownload);
		downloadItem.setDestination(outputFile);
		fileSize = Long.valueOf(size);
	}
	
	public Downloader(URL urlDownload, String outputFile) {
		downloadItem = new URLDownload();
		downloadItem.setURL(urlDownload);
		downloadItem.setDestination(outputFile);
		
		try {
			URLConnection conn = downloadItem.getURL().openConnection();
			fileSize = conn.getContentLength();
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		
	}
	
	public Downloader(URL urlDownload, File outputFile){
		downloadItem = new URLDownload();
		downloadItem.setURL(urlDownload);
		downloadItem.setDestination(outputFile);
	}

	/** code found at: 
	 * http://stackoverflow.com/questions/1139547/detect-internet-connection-using-java
	 * This test will cover if a download can occur or not.
	 * @return if its connected to the internet
	 */
	public static boolean isInternetReachable()
    {
            try {
            	//System.out.println("Checking the internet");
                //make a URL to a known source
                URL url = new URL("http://www.google.com");

                //open a connection to that source
                HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();

                //trying to retrieve data from the source. If there
                //is no connection, this line will fail
                Object objData = urlConnect.getContent();
            } catch (UnknownHostException e) {
                    return false;
            }
            catch (IOException e) {
                    return false;
            }
            return true;
    }
	
	/** This is where the file gets downloaded from
	 * 
	 */
	public int getFile(){
		RandomAccessFile outStream;
		boolean remoteFileExists=false;
		int numTries=0;
		URLConnection conn;
		
			//System.out.println("Is internet Reachable?");
		if (isInternetReachable()){
			byte buf[]=new byte[1024];
			int byteRead;	// Number of bytes read from file being downloaded
			long saved=0;	// Number of bytes saved
			String totalSizeModifier;

			// The following if statement checks if the file exists, and then get's the size
			File outputFile = new File(downloadItem.getDestination());
			if (outputFile.exists()){
				saved=outputFile.length();
			}
				
			
			while ((!remoteFileExists)&&(numTries<2)){
				try {
					conn = downloadItem.getURL().openConnection();
					/* The following line gets the file size of the Download. had to do it this 
					 * way cos URLConnection.getContentLength was an int and couldn't handle over
					 * 2GB
					 */
					//String length=conn.getHeaderField("content-Length");
					String length=null;
					List values = conn.getHeaderFields().get("content-Length");
					if (values != null && !values.isEmpty()){
						length = (String) values.get(0);
					}
					
					long tempfileSize=-1;
					if (length!=null)
						tempfileSize = Long.parseLong(length);
					if (fileSize!=tempfileSize)
						fileSize=tempfileSize;
					remoteFileExists=true;
				} catch (MalformedURLException e) {
					remoteFileExists=false;
				} catch (IOException e) {
					remoteFileExists=false;
				}

				/* Just incase the link (whether it was manually added or via a podcast)
				 * is incorrect, we will try to switch it from http to ftp and vice versa.
				 */
				if (!remoteFileExists){
					String protocol;
					if (downloadItem.getURL().toString().substring(0, 3).equals("http")){
						protocol = "ftp";
					} else
						protocol = "http";
					try {
						URL newDownload = downloadItem.getURL();
						downloadItem.setURL(new URL(protocol,
													newDownload.getHost(),
													newDownload.getPort(),
													newDownload.getFile()).toString());
					} catch (MalformedURLException e) {
						remoteFileExists=false;
					}
				}
				numTries++;
			}
			
			if (remoteFileExists){
				double fileSizeModified = 0;
				totalSizeModifier="";
				if (fileSize>1073741824){
					totalSizeModifier=" Gb";
					fileSizeModified=fileSize/1073741824;
				} else if (fileSize>1048576){
					totalSizeModifier=" Mb";
					fileSizeModified=fileSize/1048576;					
				} else if (fileSize>1024){
					totalSizeModifier=" Kb";
					fileSizeModified=fileSize/1024;
				}
					
				try {
					if ((saved<fileSize)||(fileSize==-1)){
						outStream = new RandomAccessFile(outputFile,"rw");
						outStream.seek(saved);
						
						conn = downloadItem.getURL().openConnection();
						/* Skip incoming connection ahead before we connect a stream to it,
						 * otherwise it'll waste user bandwidth						
						 */
						conn.setRequestProperty("Range", "bytes="+ saved + "-");
						conn.connect();
						InputStream inStream = conn.getInputStream();
						
						
						long time=System.currentTimeMillis();
						int chunkCount=0;
						while ((byteRead = inStream.read(buf)) > 0){
							outStream.write(buf, 0, byteRead);
							saved+=byteRead;
							chunkCount++;
							
							String outputString;
							String savedModifier="";
							double savedModified=0;
							// Download window output
							if (saved>1073741824){
								savedModifier=" Gb";
								savedModified = (double)saved/1073741824;
							} else if (saved>1048576){
								savedModifier=" Mb";
								savedModified = (double)saved/1048576;
							} else if (saved>1024){
								savedModifier=" Kb";
								savedModified = (double)saved/1024;
							} else
								savedModified=saved;

							savedModified=new Double(new DecimalFormat("#.##").format(savedModified)).doubleValue();

							if (fileSize>0)
								outputString = savedModified + savedModifier + " of " + fileSizeModified + totalSizeModifier;
							else
								outputString = savedModified + savedModifier;
								
							// Download speed limited to 300kb/sec
							if (chunkCount>=300){
								try {
									if ((System.currentTimeMillis()-time)<1000){
										Thread.sleep(1000-(System.currentTimeMillis()-time));
									}
								} catch (InterruptedException e) {
									// sleep interrupted
								}
							}
								
						}
						inStream.close();
						outStream.close();					
					} else if (saved==fileSize){
						percentage=100;
					}
				} catch (UnknownHostException e){
					return 1;
				} catch (IOException e) {
					return 1;
				}
			}
		} else {
			// No internet connection.
			return 1;
		}
		return 0;
	}
	
	public int downloadCompleted(){
		return percentage;
	}
	
	/**
	 * doRun is just a reconfiguration because4 of the NotifyingRunnable, it is called by the run method in the
	 * interface Downloader inherits
	 */
	public void doRun() {

	}
	
	public int getResult(){
		return result;
	}
}
