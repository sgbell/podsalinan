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
import java.util.Vector;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

/**
 * @author bugman
 *
 */
public class RssFeedDetails extends Thread{

	public class Episode {
		public Episode(String published, String title,
				String url, int length, String desc) {
			date = published;
			this.title=title;
			this.url=url;
			size=length;
			description=desc;
		}
		public String date,
					  title,
					  url,
					  description;
		public int size;
	};
	
	private String feedName; // String storing the feed.
	private URL address; // The Address for the podcast
	private String localStore; // Directory to download files to.
	private String feedDbName; // filename of Database.
	private SQLiteConnection feedDb; // Connection the the database for this feed
	private DownloadList downloads; // Gui list for the feed
	private boolean newFeed,  // Is this a creation of a brand new feed?
					finished=false; // Has the rssfeed been initialized
	private Vector<Episode> downloadData = new Vector<Episode>(); // Used to store the the downloads, seperate from the DownloadList

	/** This will create a podcast from previously saved database.
	 * 
	 * @param feedName - A String used in the Tree table
	 * @param address - the URL
	 * @param localStore - the local system address for the files to be downloaded to
	 * @param downloads - The DownloadList
	 */
	public RssFeedDetails(String feedName, String dbStore, String address, String localStore) {
		try {
			this.feedName = feedName;
			this.address = new URL(address);
			this.localStore = localStore;
			this.feedDbName = dbStore;
			downloads = new DownloadList(true);
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
			downloads = new DownloadList(true);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newFeed = true;
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
		/* To start off with, I'm only setting up this thread to download the podcast and
		 * create the database. After that I'll set this thread to check for the existance
		 * of the podcast database already and then it will only update.
		 */
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

		if (newFeed){
			// Grab the feed name from the podcast feed
			feedName = podcastXML.getFeedTitle();
			
			// Set download directory to default directory
			localStore=System.getProperty("user.home").concat("/Videos"+"/"+feedName);
			File localDir = new File(localStore);
			if (!localDir.exists()){
				localDir.mkdirs();
			}
			
			try {
				// The following lines are used to create a md5
				// hash for the filename.
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] bytesFeedName = feedName.getBytes("UTF-8");
				md.update(bytesFeedName, 0, feedName.length());
				// The feedFilename is a md5 hash.
				feedDbName = new BigInteger(1, md.digest()).toString().substring(0, 8);
				
				String feedFilename=System.getProperty("user.home").concat("/.bgdownloader/"+feedDbName+".pod");
				
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
			// Feed is Already is the system
			
			// Create connection to sqlite db
			String feedFilename=System.getProperty("user.home").concat("/.bgdownloader/"+feedDbName+".pod");
			feedDb = new SQLiteConnection (new File(feedFilename));
			try {
				feedDb.open();
				SQLiteStatement sql = feedDb.prepare("SELECT * FROM shows;");
				while (sql.step()){
					Episode ep = new Episode(sql.columnString(1),
											 sql.columnString(2),
											 sql.columnString(3),
											 sql.columnInt(4),
											 sql.columnString(5));

					downloadData.add(ep);
					downloads.addDownload(ep.title,ep.date,ep.url,"0%");
				}
				
			} catch (SQLiteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		for (int counter=0; counter < podcastXML.getDownloadCount(); counter++){
			try {
				boolean inList=false;
				
				for (int dlc=0; dlc < downloadData.size(); dlc++){
					if (downloadData.get(dlc).url.equals(podcastXML.getDownloadValue(counter, "enclosure", "url")))
						inList=true;
				}

				String description = podcastXML.getDownloadValue(counter, "description", null);
				// Need to change the ' to a html friendly version, otherwise we can't add it to the database.
				description=description.replaceAll("\'", "&apos;");
				// Removing new lines from data, as we don't need it
				description=description.replaceAll("\n", "");
				
				// If the file is not in our list already
				if (!inList){
					// Insert it into the database.
					SQLiteStatement sql = feedDb.prepare("INSERT INTO shows(published,title,url,size,description) VALUES('"+podcastXML.getDownloadValue(counter,"pubDate",null)+"'," +
							 							 "'"+podcastXML.getDownloadValue(counter,"title",null)+"'," +
							 							 "'"+podcastXML.getDownloadValue(counter, "enclosure", "url")+"'," +
							 							 podcastXML.getDownloadValue(counter, "enclosure", "length")+"," +
							 							 "'"+description+"');");
					sql.stepThrough();
					sql.dispose();
					// Add the episode to our array
					Episode ep = new Episode(description, description, description, counter, description);
					downloadData.add(ep);
					// Add the episode to the download list
					downloads.addDownload(podcastXML.getDownloadValue(counter,"title",null),
							  podcastXML.getDownloadValue(counter,"pubDate",null),
							  podcastXML.getDownloadValue(counter, "enclosure", "url"),
							  "0%");
				}
				
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

	public String getdb() {
		return feedDbName;
	}
}
