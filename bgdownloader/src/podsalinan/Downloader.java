/**
 * 
 */
package podsalinan;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

		try {
			if (isInternetReachable()){
				byte buf[]=new byte[1024];
				int byteRead;	// Number of bytes read from file being downloaded
				long saved=0;	// Number of bytes saved

				// The following if statement checks if the file exists, and then get's the size
				/*
				File checkFile = new File(destination);
				if (checkFile.exists()){
					saved=checkFile.length();
				}
				*/
				
				OutputStream outStream= new BufferedOutputStream(new FileOutputStream(destination));
					
				InputStream inStream = fileDownload.openStream();
				
				// If the size of the file already saved is bigger than 0 and smaller than the download size
				// skip to the point in the file we need to continue from.
				/*
				if (saved>0&&saved<fileSize){
					inStream.skip(saved);
				}
				*/
				
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
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
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
