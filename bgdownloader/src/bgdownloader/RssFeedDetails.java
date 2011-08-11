/**
 *  This is the class used to store details about the rss feed
 */
package bgdownloader;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

/**
 * @author bugman
 *
 */
public class RssFeedDetails extends Thread{

	private String feedName; // String storing the feed.
	private URL address; // The Address for the podcast
	private String localStore; // Directory to download files to.
	private SQLiteConnection feedDb; // Connection the the database for this feed
	private DownloadList downloads; // Gui list for the feed
	private boolean newFeed,  // Is this a creation of a brand new feed?
					finished=false; // Has the rssfeed been initialized

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
		newFeed = false;
	}
	
	/** This is used when creating a brand new feed
	 * 
	 * @param address
	 */
	public RssFeedDetails(String address){
		try {
			this.address = new URL(address);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newFeed = true;
	}

	/**
	 * 
	 * @param feedName
	 * @param localdb
	 * @param address
	 * @param localDir
	 */
	public RssFeedDetails(String feedName, String localdb,
			String address, String localDir) {
		
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

		//SQLiteConnection db = new SQLiteConnection(new File());
		
		//System.out.println(podcastXML.getFeedTitle());
		if (newFeed){
			// Grab the feed name from the podcast feed
			feedName = podcastXML.getFeedTitle();
			
			// Set download directory to default directory
			localStore=System.getProperty("user.home").concat("/Videos"+"/"+feedName);
			File localDir = new File(localStore);
			if (!localDir.exists()){
				localDir.mkdirs();
			}
			
			// create new download list
			downloads = new DownloadList(true);
			
			try {
				// The following lines are used to create a md5
				// hash for the filename.
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] bytesFeedName = feedName.getBytes("UTF-8");
				md.update(bytesFeedName, 0, feedName.length());
				// The feedFilename is a md5 hash.
				String feedFilename = new BigInteger(1, md.digest()).toString().substring(0, 8);
				
				feedFilename=System.getProperty("user.home").concat("/.bgdownloader/"+feedFilename+".pod");
				
				feedDb = new SQLiteConnection (new File(feedFilename));
				try {
					feedDb.open(true);
					SQLiteStatement sql = feedDb.prepare("CREATE TABLE IF NOT EXISTS shows(" +
														 "id INTEGER PRIMARY KEY AUTOINCREMENT," +
														 "published TEXT," +
														 "title TEXT," +
														 "url TEXT," +
														 "size INTEGER," +
														 "description TEXT);");
					sql.stepThrough();
					sql.dispose();
				} catch (SQLiteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			
		}

		for (int counter=0; counter < podcastXML.getDownloadCount(); counter++){
			try {
				String description = podcastXML.getDownloadValue(counter, "description", null);
				description=description.replaceAll("\'", "&apos;");
				System.out.println(description);
				
				SQLiteStatement sql = feedDb.prepare("INSERT INTO shows(published,title,url,size,description) VALUES('"+podcastXML.getDownloadValue(counter,"pubDate",null)+"'," +
													 "'"+podcastXML.getDownloadValue(counter,"title",null)+"'," +
													 "'"+podcastXML.getDownloadValue(counter, "enclosure", "url")+"'," +
													 podcastXML.getDownloadValue(counter, "enclosure", "length")+"," +
													 "'"+description+"');");
				sql.stepThrough();
				sql.dispose();
				
				downloads.addDownload(podcastXML.getDownloadValue(counter,"title",null),
									  podcastXML.getDownloadValue(counter,"pubDate",null),
									  podcastXML.getDownloadValue(counter, "enclosure", "url"),
									  "0%");
			} catch (SQLiteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		finished = true;
	}

	public boolean isFinished() {
		return finished;
	}
}
