/**
 * 
 */
package bgdownloader;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * @author bugman
 *
 */
public class Downloader extends Thread{
	private URL fileDownload;
	private String destination;
	private long fileSize;
	private int percentage=0;
	private Object syncObject;
	private DownloadList downloadTable;
	
	public String getFilenameDownload(){
		return fileDownload.toString();
	}
	
	public Downloader(URL urlDownload, String outputFile, String size, Object syncObject, DownloadList downloadTable){
		fileDownload = urlDownload;
		destination = outputFile;
		fileSize = Long.valueOf(size);
		this.downloadTable = downloadTable;
		this.syncObject = syncObject;
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
			// TODO Auto-generated catch block
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
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return false;
            }
            catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return false;
            }
            return true;
    }
	
	/** This is where the file gets downloaded from
	 * 
	 */
	public void getFile(){
		boolean viaChannel=false;

		System.out.println("Download Initiated");
		try {
			if (isInternetReachable()){
				if (viaChannel){
					ReadableByteChannel rbc = Channels.newChannel(fileDownload.openStream());
					FileOutputStream fos = new FileOutputStream(destination);
					
					if (fileSize>0){
						long partSize = 1024;
						long filepos=0;
						percentage=0;
					
						while (filepos<fileSize){
							percentage=(int) ((filepos/fileSize)*100);
							// transferring via Channel's is soooooooooooo slow.
							fos.getChannel().transferFrom(rbc, filepos, filepos+partSize);
							filepos+=partSize;
						}
					} else {
						fos.getChannel().transferFrom(rbc, 0, 1 << 24);
					}
				} else {
					byte buf[]=new byte[1024];
					int byteRead;	// Number of bytes read from file being downloaded
					long saved=0;	// Number of bytes saved
					//int lastPercent=-1;
					OutputStream outStream= new BufferedOutputStream(new FileOutputStream(destination));
					
					InputStream inStream = fileDownload.openStream();
					while ((byteRead = inStream.read(buf)) != -1){
						outStream.write(buf, 0, byteRead);
						saved+=byteRead;
						if (fileSize>0){
							double temppercent=((double)saved/(double)fileSize);
							percentage=(int)((temppercent)*100);
							downloadTable.downloadProgress(fileDownload.toString(), percentage);
						}
					}
					inStream.close();
					outStream.close();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public int downloadCompleted(){
		return percentage;
	}
	
	public void run(){
		getFile();
		synchronized (syncObject){
			syncObject.notify();
		}
	}
}
