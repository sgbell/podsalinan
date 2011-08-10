/**
 *  This is the class used to store details about the rss feed
 */
package bgdownloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author bugman
 *
 */
public class RssFeedDetails extends Thread{

	private String feedName;
	private URL address;
	private String localStore;
	private DownloadList downloads;

	/**
	 * RssFeedDetails is the constructor used to store information about the rssFeed, including the
	 * download list.
	 * @param feedName - A String used in the Tree table
	 * @param address - the URL
	 * @param localStore - the local system address for the files to be downloaded to
	 * @param downloads - The DownloadList
	 */
	public RssFeedDetails(String feedName, String address, String localStore, DownloadList downloads) {
		try {
			this.feedName = feedName;
			this.address = new URL(address);
			this.localStore = localStore;
			this.downloads = downloads;
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * setDownloadList modifies the download list
	 * @param downloads
	 */
	public void setDownloadList(DownloadList downloads){
		this.downloads = downloads;
	}
	
	public DownloadList getDownloadList(){
		return downloads;
	}
	
	public void setFeedName(String feed){
		feedName = feed;
	}
	
	public void setURL(String url){
		try {
			address = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public void setLocalStore(String localDir){
		localStore = localDir;
	}
	
	public String getFeedName(){
		return feedName;
	}
	
	public URL getURL(){
		return address;
	}
	
	public String getLocalStore(){
		return localStore;
	}
	
	public String toString(){
		return feedName;
	}
	
	/**
	 * This will start a thread, which will initialize the podcast information,
	 * by either loading it from a current stored file, or by downloading the podcast
	 * information from the internet, if the podcast is not already in the system.
	 */
	public void run(){
		// To start off with, I'm only setting up this thread to download the podcast and
		// create the database. After that I'll set this thread to check for the existance
		// of the podcast database already and then it will only update.
		String localDirectory = System.getProperty("user.home");
		localDirectory=localDirectory.concat("/.bgdownloader");
		File settingsDir = new File(localDirectory);
		if (!settingsDir.exists()){
			settingsDir.mkdir();
		}
		String outputFile = localDirectory.concat("/temp.xml");
		
		Downloader d = new Downloader(address,outputFile);
		// I don't want to start another thread, as this is already being executed
		// in a thread, and we can't continue without the file.
		d.getFile();
		XmlReader podcastXML = new XmlReader(outputFile);

		System.out.println(podcastXML.getFeedTitle());
		for (int counter=0; counter < podcastXML.getDownloadCount(); counter++){
			System.out.println(podcastXML.getDownloadValue(counter,"pubDate",null)+" - "+podcastXML.getDownloadValue(counter,"title",null));
			System.out.println(podcastXML.getDownloadValue(counter, "enclosure", "url"));
			System.out.println("Size: "+podcastXML.getDownloadValue(counter, "enclosure", "length"));
			System.out.println("Description: "+podcastXML.getDownloadValue(counter, "description", null));
		}
	}
}
