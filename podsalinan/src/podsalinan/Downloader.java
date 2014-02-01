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

public class Downloader extends NotifyingRunnable{
	private URLDownload downloadItem;
	private int percentage=0;
    private static int result;
	
	public final static int CONNECTION_FAILED=-1;
	public final static int DOWNLOAD_COMPLETE=1;
	public final static int DOWNLOAD_INCOMPLETE=-2;
	
    public Downloader(URLDownload download){
    	downloadItem = download;
    }
	
	public Downloader(URL urlDownload, String outputFile, String size){
		downloadItem = new URLDownload();
		downloadItem.setURL(urlDownload);
		downloadItem.setDestination(outputFile);
		downloadItem.setSize(size);
	}
	
	public Downloader(URL urlDownload, String outputFile) {
		downloadItem = new URLDownload();
		downloadItem.setURL(urlDownload);
		downloadItem.setDestination(outputFile);
		
		try {
			URLConnection conn = downloadItem.getURL().openConnection();
			List<String> values = conn.getHeaderFields().get("content-Length");
			if (values != null && !values.isEmpty())
				downloadItem.setSize((String) values.get(0));
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
            		result=CONNECTION_FAILED;
                    return false;
            }
            catch (IOException e) {
            		result=CONNECTION_FAILED;
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
					List<String> values = conn.getHeaderFields().get("content-Length");
					if (values != null && !values.isEmpty()){
						length = (String) values.get(0);
					}
					
					long tempfileSize=-1;
					if (length!=null)
						tempfileSize = Long.parseLong(length);
					if (Long.parseLong(downloadItem.getSize())!=tempfileSize)
						downloadItem.setSize(length);
					remoteFileExists=true;
				} catch (MalformedURLException e) {
					result=CONNECTION_FAILED;
					remoteFileExists=false;
				} catch (IOException e) {
					result=CONNECTION_FAILED;
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
						result=CONNECTION_FAILED;
						remoteFileExists=false;
					}
				}
				numTries++;
			}
			
			if (remoteFileExists){
				double fileSizeModified = 0;
				totalSizeModifier="";
				if (Long.parseLong(downloadItem.getSize())>1073741824){
					totalSizeModifier=" Gb";
					fileSizeModified=Long.parseLong(downloadItem.getSize())/1073741824;
				} else if (Long.parseLong(downloadItem.getSize())>1048576){
					totalSizeModifier=" Mb";
					fileSizeModified=Long.parseLong(downloadItem.getSize())/1048576;					
				} else if (Long.parseLong(downloadItem.getSize())>1024){
					totalSizeModifier=" Kb";
					fileSizeModified=Long.parseLong(downloadItem.getSize())/1024;
				}
					
				try {
					if ((saved<Long.parseLong(downloadItem.getSize()))||(Long.parseLong(downloadItem.getSize())==-1)){
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
						//TODO: Add a check on ProgSettings.isFinished() below.
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

							if (Long.parseLong(downloadItem.getSize())>0)
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
					} else if (saved==Long.parseLong(downloadItem.getSize())){
						percentage=100;
						downloadItem.setStatus(Details.FINISHED);
					}
				} catch (UnknownHostException e){
					return CONNECTION_FAILED;
				} catch (IOException e) {
					return CONNECTION_FAILED;
				}
			}
		} else {
			// No internet connection.
			return CONNECTION_FAILED;
		}
		return DOWNLOAD_COMPLETE;
	}
	
	public int downloadCompleted(){
		return percentage;
	}
	
	/**
	 * doRun is just a reconfiguration because4 of the NotifyingRunnable, it is called by the run method in the
	 * interface Downloader inherits
	 */
	public void doRun() {
		// All it needs to do is start downloading and grab the result.
		result = getFile();
	}
	
	public String getFilenameDownload(){
		return downloadItem.getURL().toString().split("/")[downloadItem.getURL().toString().split("/").length-1];
	}

	/**
	 * @return the result
	 */
	public int getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(int result) {
		Downloader.result = result;
	}
}
