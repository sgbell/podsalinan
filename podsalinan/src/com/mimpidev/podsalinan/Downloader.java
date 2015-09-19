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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.List;

import com.mimpidev.dev.debug.Log;
import com.mimpidev.podsalinan.data.URLDetails;
import com.mimpidev.podsalinan.data.URLDownload;


public class Downloader extends NotifyingRunnable{
    public final static int NO_STATUS=0;
	public final static int CURRENTLY_DOWNLOADING=1;
	public final static int DOWNLOAD_COMPLETE=2;
	public final static int CONNECTION_FAILED=-1;
	public final static int DOWNLOAD_INCOMPLETE=-2;
	public final static int DESTINATION_INVALID=-3;
	public final static int DOWNLOAD_ERROR=-4;
	
	private URLDownload downloadItem;
    private static int result=NO_STATUS;
    private String fileSystemSlash;
    private URL downloadURL;
    private long saved=0;
    private Object syncObject = new Object();
    
    private boolean active=false;
    private boolean debug=false;
	
    public Downloader(URLDownload download, String newFileSystemSlash){
    	downloadItem = download;
    	fileSystemSlash=newFileSystemSlash;
    	// Creating a new object for downloadURL from downloadItem, so that if the url is changed for the source,
    	// it wont change in the system
    	try {
			downloadURL = new URL(downloadItem.getURL().toString());
		} catch (MalformedURLException e) {
			downloadURL=null;
		}
    }
	
	public Downloader(URL urlDownload, String outputFile, String size){
		downloadItem = new URLDownload();
		downloadItem.setURL(urlDownload.toString());
		downloadItem.setSize(size);
		try {
			downloadURL = new URL(urlDownload.toString());
		} catch (MalformedURLException e) {
			downloadURL = null;
		}
	}
	
	public Downloader(URL urlDownload, String outputFile) {
		downloadItem = new URLDownload();
		downloadItem.setURL(urlDownload.toString());
		try {
			downloadURL = new URL(urlDownload.toString());
		} catch (MalformedURLException e1) {
			downloadURL = null;
		}
		
		try {
			if (downloadURL!=null){
				URLConnection conn = downloadURL.openConnection();
				List<String> values = conn.getHeaderFields().get("content-Length");
				if (values != null && !values.isEmpty())
					downloadItem.setSize((String) values.get(0));
			}
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		
	}
	
	public Downloader(URL urlDownload, File outputFile){
		downloadItem = new URLDownload();
		downloadItem.setURL(urlDownload.toString());
		downloadItem.setDestination(outputFile);
		downloadItem.setStatus(URLDetails.CURRENTLY_DOWNLOADING);
		try {
			downloadURL = new URL(urlDownload.toString());
		} catch (MalformedURLException e1) {
			downloadURL = null;
		}
		
	}

	/**
	 * This is used to initialize a downloader thread
	 * @param fileSystemSlash
	 */
	public Downloader(String fileSystemSlash) {
		this.fileSystemSlash=fileSystemSlash;
		downloadItem=new URLDownload();
	}
	
	/**
	 * 
	 */
	public void setDownload(URLDownload downloadItem){
		this.downloadItem = downloadItem;
        setResult(NO_STATUS);
		
        if (downloadItem.getURL().length()>0){
    		try {
    			downloadURL = new URL(downloadItem.getURL().toString());
    		} catch (MalformedURLException e) {
    			downloadURL=null;
    		}
        } else {
        	downloadURL=null;
        }
		if (downloadURL!=null){
			active=true;
			synchronized(syncObject){
				syncObject.notify();
			}
			if (Log.isDebug())Log.logInfo(this, "Download Passed in."); 
		} else {
			downloadItem=null;
		}
	}
	
	public void endThread(){
		synchronized(syncObject){
			syncObject.notify();
		}
		this.setStopThread(true);
	}

	/** code found at: 
	 * http://stackoverflow.com/questions/1139547/detect-internet-connection-using-java
	 * This test will cover if a download can occur or not.
	 * @return if its connected to the internet
	 */
	@SuppressWarnings("unused")
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
            		setResult(CONNECTION_FAILED);
                    return false;
            }
            catch (IOException e) {
            		setResult(CONNECTION_FAILED);
                    return false;
            }
            return true;
    }

	public boolean checkURLRedirect(URL urlToCheck){
		HttpURLConnection httpConn;
		try {
			httpConn = (HttpURLConnection)urlToCheck.openConnection();
			httpConn.setReadTimeout(5000);
		
			int status;
			status = httpConn.getResponseCode();
			if (status!= HttpURLConnection.HTTP_OK){
				if ((status == HttpURLConnection.HTTP_MOVED_TEMP)||
					(status == HttpURLConnection.HTTP_MOVED_PERM)||
					(status == HttpURLConnection.HTTP_SEE_OTHER)){
					String newURL = httpConn.getHeaderField("Location");
					downloadURL = new URL(newURL);
				}
				return true;
			}

		} catch (IOException e) {
			return false;
		}
		return false;
	}
	
	/** This is where the file gets downloaded from
	 * 
	 */
	public int getFile(){
		RandomAccessFile outStream;
		boolean remoteFileExists=false;
		int numTries=0;
		URLConnection conn;
		
		if (debug)Log.logInfo(this, "Is internet Reachable? ");
		if (isInternetReachable()){
			setResult(CURRENTLY_DOWNLOADING);
			if (debug)Log.logInfo(this, "Yes it is.");
			byte buf[]=new byte[1024];
			int byteRead;	// Number of bytes read from file being downloaded
			
			if (debug)Log.logInfo(this, "Attempting to download:"+downloadItem.getURL()); 				
			
			while ((!remoteFileExists)&&(numTries<2)
					&&(!isStopThread())){
				//System.out.println("Inside first while");
				try {
					checkURLRedirect(new URL(downloadItem.getURL()));
					
					conn = downloadURL.openConnection();
					/* The following line gets the file size of the Download. had to do it this 
					 * way cos URLConnection.getContentLength was an int and couldn't handle over
					 * 2GB
					 */
					String length=null;
					List<String> values = conn.getHeaderFields().get("content-Length");
					boolean isDirectory=false;

					File destinationFile=downloadItem.getDestinationFile();
					if (destinationFile.list()!=null)
						isDirectory=true;
					else
						isDirectory=false;
					
					// If the destination is currently set to a folder, we need to add the filename to it
					if (isDirectory){
						// First we test if the server is handing the program a filename to set, set it here.
						List<String> disposition = conn.getHeaderFields().get("content-disposition");

						if (disposition!=null){
							// If the file is going to be renamed from the server, grab the new name here
							int index = disposition.get(0).indexOf("filename=");
							if (index > 0){
								String newFilename=disposition.get(0).substring(index+10, disposition.get(0).length() - 1);
									File newFile = new File(downloadItem.getDestination()+fileSystemSlash+newFilename);
									// If it doesn't exists create it.
									if (!newFile.exists())
										newFile.createNewFile();
									// If it has been created set it as the destination for the download.
									if ((newFile.exists())&&(newFile.isFile()))
										downloadItem.setDestination(newFile);
							}
						} else {
							String filePath=downloadItem.getDestination()+fileSystemSlash+getFilenameDownload();
							File outputFile = new File(filePath);
							downloadItem.setDestination(outputFile);
						}
					} else if ((!destinationFile.exists())&&(!destinationFile.getParentFile().exists())){
						downloadItem.setStatus(URLDetails.DESTINATION_INVALID);
						if (debug) Log.logInfo(this, "Destination does not exist.");
						return DESTINATION_INVALID;
					}
					
					if (values != null && !values.isEmpty()){
						length = (String) values.get(0);
					}
					if (downloadItem.getDestinationFile().exists()){
						saved=downloadItem.getDestinationFile().length();
					}
					
					long tempfileSize=-1;
					if (length!=null){
						tempfileSize = Long.parseLong(length);
						if (Long.parseLong(downloadItem.getSize())!=tempfileSize)
							downloadItem.setSize(length);
					}
					if (downloadItem.getSize()==null || downloadItem.getSize().equals(""))
						downloadItem.setSize("-1");
					remoteFileExists=true;
				} catch (MalformedURLException e) {
					e.printStackTrace();
					setResult(CONNECTION_FAILED);
					remoteFileExists=false;
				} catch (IOException e) {
					e.printStackTrace();
					setResult(CONNECTION_FAILED);
					remoteFileExists=false;
				}

				/* Just incase the link (whether it was manually added or via a podcast)
				 * is incorrect, we will try to switch it from http to ftp and vice versa.
				 */
				if (!remoteFileExists){
					String protocol;
					if (downloadURL.toString().substring(0, 3).equals("http")){
						protocol = "ftp";
					} else
						protocol = "http";
					try {
						
						downloadURL = new URL(protocol,
												downloadURL.getHost(),
												downloadURL.getPort(),
												downloadURL.getFile());
					} catch (MalformedURLException e) {
						setResult(CONNECTION_FAILED);
						remoteFileExists=false;
					}
				}
				numTries++;
			}
			
			if (remoteFileExists){
				try {
					if ((saved<Long.parseLong(downloadItem.getSize()))||
						(Long.parseLong(downloadItem.getSize())==-1)){
						if (debug) Log.logInfo(this, "Filename to save to:"+downloadItem.getDestinationFile()); 
                        File outputFile = downloadItem.getDestinationFile();
                        if ((outputFile.exists() && outputFile.canWrite())||
                        	 outputFile.createNewFile()){
    						outStream = new RandomAccessFile(downloadItem.getDestinationFile(),"rw");
    						outStream.seek(saved);
    						
    						conn = downloadURL.openConnection();
    						/* Skip incoming connection ahead before we connect a stream to it,
    						 * otherwise it'll waste user bandwidth						
    						 */
    						conn.setRequestProperty("Range", "bytes="+ saved + "-");
    						conn.connect();
    						InputStream inStream = conn.getInputStream();
    						if (debug) Log.logInfo(this, "External socket opened to download."); 
    						
    						
    						long time=System.currentTimeMillis();
    						int chunkCount=0;
    						//System.out.println("before the download while");
    						if (debug) Log.logInfo(this, "Start to read."); 
    						while (((byteRead = inStream.read(buf)) > 0)
    								&&(!isStopThread())
    								&&(downloadItem.getStatus()==URLDetails.CURRENTLY_DOWNLOADING)){
    							//System.out.println("Downloading....");
    							outStream.write(buf, 0, byteRead);
    							saved+=byteRead;
    							chunkCount++;
    							
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
    						if (debug) Log.logInfo(this, "Finished reading."); 
                        } else {
                        	if (debug) Log.logInfo(this, "Can not open local file to save.");
                        	downloadItem.setStatus(URLDetails.DESTINATION_INVALID);
                        	setResult(DOWNLOAD_ERROR);
                        }
					}
					if (downloadItem.getStatus()!=URLDetails.DESTINATION_INVALID){
						if (saved==Long.parseLong(downloadItem.getSize())){
							downloadItem.setStatus(URLDetails.FINISHED);
							setResult(DOWNLOAD_COMPLETE);
						} else if (saved<Long.parseLong(downloadItem.getSize())){
							downloadItem.setStatus(URLDetails.INCOMPLETE_DOWNLOAD);
							setResult(DOWNLOAD_INCOMPLETE);
						} else if ((saved>Long.parseLong(downloadItem.getSize()))&&
								    (Long.parseLong(downloadItem.getSize())>0)){
							downloadItem.setStatus(URLDetails.DOWNLOAD_FAULT);
							setResult(DOWNLOAD_ERROR);
						} else if ((Long.parseLong(downloadItem.getSize())==-1)&&saved>0){
							downloadItem.setStatus(URLDetails.FINISHED);
							setResult(DOWNLOAD_COMPLETE);
						}
					}
				} catch (UnknownHostException e){
					downloadItem.setStatus(URLDetails.INCOMPLETE_DOWNLOAD);
					setResult(CONNECTION_FAILED);
				} catch (IOException e) {
                    File outputFile = downloadItem.getDestinationFile();
                    if (!outputFile.exists()){
                    	downloadItem.setStatus(URLDetails.DESTINATION_INVALID);
                    } else {
                    	downloadItem.setStatus(URLDetails.INCOMPLETE_DOWNLOAD);
                    }
					setResult(CONNECTION_FAILED);
				}
			}
		} else {
			// No internet connection.
			setResult(CONNECTION_FAILED);
		}
		return result;
	}
	
	/**
	 * doRun is just a reconfiguration because of the NotifyingRunnable, it is called by the run method in the
	 * interface Downloader inherits
	 */
	public void doRun() {
		synchronized(syncObject){
			try {
				syncObject.wait();
			} catch (InterruptedException e) {
				if (Log.isDebug())Log.printStackTrace(e.getStackTrace());
			}
		}
		if (debug) Log.logInfo(this, "doRun is awake");
		if (!isStopThread()){
			result = getFile();
		}
	}
	
	public String getFilenameDownload(){
		return downloadURL.toString().split("/")[downloadURL.toString().split("/").length-1];
	}

	/**
	 * @return the result
	 */
	public synchronized static int getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public synchronized static void setResult(int result) {
		Downloader.result = result;
	}

	public URLDownload getURLDownload(){
		return downloadItem;
	}
	
	/** This is used to get the on going downloaded size
	 * 
	 * @return amount of data downloaded
	 */
	public long getSaved(){
		return saved;
	}

	/**
	 * 
	 * @return
	 */
	public boolean currentlyDownloading() {
		
		return active;
	}

	public void downloaderFinished(boolean finished) {
		active=!finished;
	}

	public void clearDownload() {
		downloadItem = null;
	}
}
