/**
 *  This is the class used to store details about the rss feed
 */
package bgdownloader;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author bugman
 *
 */
public class RssFeedDetails {

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
	public RssFeedDetails(String feedName, URL address, String localStore, DownloadList downloads) {
		this.feedName = feedName;
		this.address = address;
		this.localStore = localStore;
		this.downloads = downloads;
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
}
