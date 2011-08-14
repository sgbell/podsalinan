/**
 * 
 */
package bgdownloader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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
	
	public Downloader(URL urlDownload, String outputFile) {
		fileDownload = urlDownload;
		destination = outputFile;

	}
	
	
	/** code found at: 
	 * http://stackoverflow.com/questions/1139547/detect-internet-connection-using-java
	 * This test will cover if a download can occur or not.
	 * @return
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
				/* To use the progress bar, we'll need to set it up so transferFrom copies the file in stages,
				 *  and updates a precentages for the progress bar.
				 *  Can't use the channels stuff, cos I can't get the file size... for the progress bar :(
				 */
			
				fos.getChannel().transferFrom(rbc, 0, 1 << 24);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void run(){
		getFile();
	}
}
