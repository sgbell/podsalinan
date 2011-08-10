/**
 * 
 */
package bgdownloader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
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
	
	/** This is where the file gets downloaded from
	 * 
	 */
	public void getFile(){
		try {
			ReadableByteChannel rbc = Channels.newChannel(fileDownload.openStream());
			FileOutputStream fos = new FileOutputStream(destination);
			/* To use the progress bar, we'll need to set it up so transferFrom copies the file in stages,
			 *  and updates a precentages for the progress bar.
			 *  Can't use the channels stuff, cos I can't get the file size... for the progress bar :(
			 */
			
			fos.getChannel().transferFrom(rbc, 0, 1 << 24);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run(){
		getFile();
	}
}
