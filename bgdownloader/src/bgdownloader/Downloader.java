/**
 * 
 */
package bgdownloader;

import java.io.FileOutputStream;
import java.io.IOException;
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
	
	public String getFilenameDownload(){
		return fileDownload.toString();
	}
	
	public Downloader(URL urlDownload, String outputFile, String size){
		fileDownload = urlDownload;
		destination = outputFile;
		fileSize = Long.valueOf(size);
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
				ReadableByteChannel rbc = Channels.newChannel(fileDownload.openStream());
				FileOutputStream fos = new FileOutputStream(destination);
				
				if (fileSize>0){
					long partSize = 0;
					partSize =fileSize/100;
					long filepos=0;
				
					for (percentage=0; percentage < 100; percentage++){
						filepos=partSize*percentage;
						fos.getChannel().transferFrom(rbc, filepos, filepos+partSize);
					}
				} else {
					fos.getChannel().transferFrom(rbc, 0, 1 << 24);
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
	}
}
