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

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * @author bugman
 *
 */
public class RssFeedDetails implements Runnable{

	private String feedName; // String storing the feed.
	private URL address; // The Address for the podcast
	private String localStore; // Directory to download files to.
	private String feedDbName; // filename of Database.
	private DataStorage settings;
	private DownloadList downloads; // Gui list for the feed
	private boolean newFeed;  // Is this a creation of a brand new feed?
	private Vector<Episode> downloadData = new Vector<Episode>(); // Used to store the the downloads, seperate from the DownloadList
	private TreePane tree;
	private JPanel cards;

	/** This will create a podcast from previously saved database.
	 * 
	 * @param feedName - A String used in the Tree table
	 * @param address - the URL
	 * @param localStore - the local system address for the files to be downloaded to
	 * @param cardPanel 
	 * @param treePanel 
	 * @param downloads - The DownloadList
	 */
	public RssFeedDetails(String feedName, String dbStore, String address, String localStore,
						  DataStorage settings, TreePane treePanel, JPanel cardPanel) {
		try {
			this.feedName = feedName;
			this.address = new URL(address);
			this.localStore = localStore;
			this.feedDbName = dbStore;
			this.settings = settings;
			tree = treePanel;
			cards = cardPanel;
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
	 * @param directory 
	 * @param url 
	 * @param datafile 
	 * @param settings 
	 * @param cardPane 
	 * @param treePane 
	 * @param podcastQueue 
	 */
	public RssFeedDetails(String address, DataStorage settings, TreePane treePane, JPanel cardPane){
		try {
			this.address = new URL(address);
			downloads = new DownloadList(true);
			tree = treePane;
			cards=cardPane;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newFeed = true;
		this.settings=settings;
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
	
	public int setLocalStore(String localDir){
		File directory = new File(localDir);
		System.out.println(localDir);
		if (directory.exists()){
			if (directory.isDirectory()){
				localStore = localDir;
				return 0;
			} else {
				JOptionPane.showMessageDialog(null, "Error in moving Podcast folder.", "bgDownloader", JOptionPane.ERROR_MESSAGE);
				return 1;
			}
		}
		return 1;
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

	/** Function to download Feed
	 * 
	 */
	public void downloadFeed(){
		int outputCount=1;
		// temporary download destination of podcast xml file
		String outputFile = settings.getSettingsDir().concat("/temp.xml");
		
		while (new File(outputFile).exists()){
			outputFile = outputFile.concat("("+outputCount+")");
			outputCount++;
		}
		
		// Following 3 lines of code download podcast XML file
		Downloader d = new Downloader(address,outputFile);
		// I don't want to start another thread, as this is already being executed
		// in a thread, and we can't continue without the file.
		d.getFile();
		XmlReader podcastXML = new XmlReader(outputFile);
	
		if (newFeed){
			// Grab the feed name from the podcast feed
			feedName = podcastXML.getFeedTitle();

			try {
				// The following lines are used to create a md5  hash for the filename.
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] bytesFeedName = feedName.getBytes("UTF-8");
				md.update(bytesFeedName, 0, feedName.length());
				// The feedFilename is a md5 hash.
				feedDbName = new BigInteger(1, md.digest()).toString().substring(0, 8);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		for (int counter=0; counter < podcastXML.getDownloadCount(); counter++){
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
			
			String title=podcastXML.getDownloadValue(counter,"title",null);
			title=title.replaceAll("\'", "&apos;");
			
			// If the file is not in our list already
			if (!inList){
				// Add the episode to our array
				Episode ep = new Episode(podcastXML.getDownloadValue(counter,"pubDate",null),
										 title,
										 podcastXML.getDownloadValue(counter, "enclosure", "url"), 
										 podcastXML.getDownloadValue(counter, "enclosure", "length"), description);
				downloadData.add(ep);
				// Add the episode to the download list
				downloads.addDownload(podcastXML.getDownloadValue(counter,"title",null),
						  			  podcastXML.getDownloadValue(counter,"pubDate",null),
						  			  podcastXML.getDownloadValue(counter, "enclosure", "url"),
						  			  "0%");
			}
		}
		new File(outputFile).delete();
		settings.savePodcastDB(downloadData, feedDbName);
	}
	
	/**
	 * This will start a thread, which will initialize the podcast information,
	 * by either loading it from a current stored file, or by downloading the podcast
	 * information from the internet, if the podcast is not already in the system.
	 */
	public void run(){
		if (newFeed){
			
			downloadFeed();
			// Set download directory to the default directory
			localStore=System.getProperty("user.home").concat("/Videos/"+feedName);
			File localDir = new File(localStore);
			if (!localDir.exists()){
				localDir.mkdirs();
			}
			settings.addPodcast(this);
		} else {
			// Load podcast from sqlite database
			settings.loadPodcastDB(downloadData,feedDbName,downloads);
		}
		
		tree.addrssFeed(this);
		cards.add(getDownloadList(),getFeedName());
		queueDownloads();
	}
	
	public String getdb() {
		return feedDbName;
	}
	
	/** This will search through the array of downloads for the feed, mark the files that are already downloaded
	 *  and then queues the files that are not downloaded yet. 
	 */
	public void queueDownloads(){
		// Travel through the array
		for (int dlc=0; dlc<downloadData.size(); dlc++){
			if (!downloadData.get(dlc).downloaded){
				String filename=downloadData.get(dlc).url;
				filename=localStore+"/"+filename.substring(filename.lastIndexOf('/')+1);
				File checkFile = new File(filename);
				if (checkFile.exists())
					if (checkFile.length()==Long.parseLong(downloadData.get(dlc).size))
						downloadData.get(dlc).downloaded=true;
			}
		}
	}
}
