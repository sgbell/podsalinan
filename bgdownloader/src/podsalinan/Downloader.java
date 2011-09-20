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
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

/**
 * @author bugman
 *
 */
public class Downloader extends NotifyingRunnable{
	private URL fileDownload;
	private String destination;
	private long fileSize;
	private int percentage=0,
				listNum;
	private Object syncObject;
	private DownloadList downloadTable;
	private boolean podcast;
	
	public String getFilenameDownload(){
		return fileDownload.toString();
	}
	
	public boolean isPodcast(){
		return podcast;
	}
	
	public Downloader(URL urlDownload, String outputFile, String size, Object syncObject, DownloadList downloadTable, int listNum, boolean podcast){
		fileDownload = urlDownload;
		destination = outputFile;
		fileSize = Long.valueOf(size);
		this.downloadTable = downloadTable;
		this.syncObject = syncObject;
		this.podcast=podcast;
		this.listNum=listNum;
	}
	
	public Downloader(URL urlDownload, String outputFile, Object syncObject, DownloadList downloadTable) {
		this(urlDownload,outputFile);
		this.syncObject = syncObject;
		this.downloadTable=downloadTable;
	}
	
	public Downloader(URL urlDownload, String outputFile) {
		fileDownload = urlDownload;
		destination = outputFile;
		
		try {
			URLConnection conn = fileDownload.openConnection();
			fileSize = conn.getContentLength();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}	

	/** code found at: 
	 * http://stackoverflow.com/questions/1139547/detect-internet-connection-using-java
	 * This test will cover if a download can occur or not.
	 * @return if its connected to the internet
	 */
	public static boolean isInternetReachable()
    {
            try {
                    //make a URL to a known source
                    URL url = new URL("http://www.google.com");

                    //open a connection to that source
                    HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();

                    //trying to retrieve data from the source. If there
                    //is no connection, this line will fail
                    @SuppressWarnings("unused")
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
		
		try {
			if (isInternetReachable()){
				byte buf[]=new byte[1024];
				int byteRead;	// Number of bytes read from file being downloaded
				long saved=0;	// Number of bytes saved

				// The following if statement checks if the file exists, and then get's the size
				
				File outputFile = new File(destination);
				if (outputFile.exists()){
					saved=outputFile.length();
				}
				
				URLConnection conn = fileDownload.openConnection();
				long tempfileSize = conn.getContentLength();
				if (fileSize!=tempfileSize)
					fileSize=tempfileSize;
				
				//System.out.println(fileDownload.toString()+" - "+fileSize);
				if ((saved<fileSize)||(fileSize==-1)){
					outStream = new RandomAccessFile(outputFile,"rw");
					outStream.seek(saved);
					
					InputStream inStream = fileDownload.openStream();
					inStream.skip(saved);
					//System.out.println(fileDownload.toString());
					//System.out.println(saved+" of "+fileSize);					
					
					while ((byteRead = inStream.read(buf)) != -1){
						outStream.write(buf, 0, byteRead);
						saved+=byteRead;
						if (downloadTable!=null)						
							if (fileSize>0){
								double temppercent=((double)saved/(double)fileSize);
								percentage=(int)((temppercent)*100);
								downloadTable.downloadProgress(listNum, percentage);
							}
					}
					inStream.close();
					outStream.close();					
				} else if (saved==fileSize){
					percentage=100;
					downloadTable.downloadProgress(listNum, 100);
				}
			} else {
				// No internet connection.
				return 1;
			}
		} catch (UnknownHostException e){
			return 1;
		} catch (IOException e) {
			return 1;
		}
		return 0;
	}
	
	public int downloadCompleted(){
		return percentage;
	}
	
	@Override
	public void doRun() {
		getFile();
		synchronized (syncObject){
			syncObject.notify();
		}		
	}
}
