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

	private PodDetails details;
	private DataStorage settings;
	private DownloadList downloads; // Gui list for the feed
	private boolean newFeed;  // Is this a creation of a brand new feed?
	private Vector<Episode> downloadData = new Vector<Episode>(); // Used to store the the downloads, seperate from the DownloadList
	private TreePane tree;
	private JPanel cards;
	private Object syncObject;

	/** This is used when creating a brand new feed
	 *
	 * @param newPodcast
	 * @param settings 
	 * @param cardPane 
	 * @param treePane 
	 * @param podcastQueue 
	 */
	public RssFeedDetails(PodDetails newPodcast, DataStorage settings, TreePane treePane, JPanel cardPane, Object syncObject){
		details=newPodcast;
		
		tree = treePane;
		cards=cardPane;
		downloads = new DownloadList(true);
		this.settings=settings;
		this.syncObject=syncObject;
		
		if (details.name.isEmpty())
			newFeed = true;
		else
			newFeed = false;
	}

	public PodDetails getDetails(){
		return details;
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
	
	public Vector<Episode> getDownloadData(){
		return downloadData;
	}
	
	public void setFeedName(String feed){
		details.name = feed;
	}
	
	public void setURL(String url){
		details.url=url;
	}
	
	public int setLocalStore(String localDir){
		File directory = new File(localDir);
		if (directory.exists()){
			if (directory.isDirectory()){
				details.directory = localDir;
				details.changed=true;
				return 0;
			} else {
				JOptionPane.showMessageDialog(null, "Error in moving Podcast folder.", "bgDownloader", JOptionPane.ERROR_MESSAGE);
				return 1;
			}
		}
		return 1;
	}
	
	public String getFeedName(){
		return details.name;
	}
	
	public String getURL(){
		return details.url;
	}
	
	public String getLocalStore(){
		return details.directory;
	}
	
	public String toString(){
		return details.name;
	}

	/** Function to download Feed
	 * 
	 */
	public void downloadFeed(){
		int outputCount=1;
		// temporary download destination of podcast xml file
		String outputFile = details.directory.concat("/temp.xml");
		
		while (new File(outputFile).exists()){
			outputFile = outputFile.concat("("+outputCount+")");
			outputCount++;
		}
		
		// Following 3 lines of code download podcast XML file
		Downloader d;
		try {
			d = new Downloader(new URL(details.url),outputFile);
			// I don't want to start another thread, as this is already being executed
			// in a thread, and we can't continue without the file.
			d.getFile();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		XmlReader podcastXML = new XmlReader(outputFile);
	
		if (newFeed){
			// Grab the feed name from the podcast feed
			details.name = podcastXML.getFeedTitle();

			try {
				// The following lines are used to create a md5  hash for the filename.
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] bytesFeedName = details.name.getBytes("UTF-8");
				md.update(bytesFeedName, 0, details.name.length());
				// The feedFilename is a md5 hash.
				details.datafile = new BigInteger(1, md.digest()).toString().substring(0, 8);
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
		settings.savePodcastDB(downloadData, details.datafile);
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
			details.directory=System.getProperty("user.home").concat("/Videos/"+details.name);
			File localDir = new File(details.directory);
			if (!localDir.exists()){
				localDir.mkdirs();
			}
		} else {
			// Load podcast from sqlite database
			settings.loadPodcastDB(downloadData,details.datafile,downloads);
			details.added=true;
			details.remove=false;
			details.changed=false;
		}
		
		tree.addrssFeed(this);
		cards.add(getDownloadList(),getFeedName());
		checkDownloads();

		// The following Tells the DownloadQueue to wake up, cos there's something there
		synchronized (syncObject){
			syncObject.notify();
		}

	}
	
	public String getdb() {
		return details.datafile;
	}
	
	/** This will search through the array of downloads for the feed, mark the files that are already downloaded
	 *  and then queues the files that are not downloaded yet. 
	 */
	public void checkDownloads(){
		// Travel through the array
		for (int dlc=0; dlc<downloadData.size(); dlc++){
			if (downloadData.get(dlc).downloaded!=downloadData.get(dlc).FINISHED){
				String filename=downloadData.get(dlc).url;
				filename=details.directory+"/"+filename.substring(filename.lastIndexOf('/')+1);
				File checkFile = new File(filename);
				if (checkFile.exists()){
					if (checkFile.length()==Long.parseLong(downloadData.get(dlc).size)){
						downloadData.get(dlc).downloaded=downloadData.get(dlc).FINISHED;
						downloads.downloadProgress(dlc, 100);
					}
					else if (checkFile.length()<Long.parseLong(downloadData.get(dlc).size)){
						downloadData.get(dlc).downloaded=downloadData.get(dlc).PREVIOUSLY_STARTED;
						double temppercent=((double)checkFile.length()/Double.parseDouble(downloadData.get(dlc).size));
						int percentage=(int)((temppercent)*100);
						downloads.downloadProgress(dlc,percentage);
					}
				} else {
					downloadData.get(dlc).downloaded=downloadData.get(dlc).NOT_STARTED;
				}
			}
		}
	}
}
