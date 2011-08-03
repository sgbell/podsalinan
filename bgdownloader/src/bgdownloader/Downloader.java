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
	URL fileDownload;
	String directory,
		   destination;
	
	public Downloader(URL urlDownload, String outputFile) {
		fileDownload = urlDownload;
		destination = outputFile;
	}
	
	public void run(){
		try {
			ReadableByteChannel rbc = Channels.newChannel(fileDownload.openStream());
			FileOutputStream fos = new FileOutputStream(destination);
			fos.getChannel().transferFrom(rbc, 0, 1 << 24);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
