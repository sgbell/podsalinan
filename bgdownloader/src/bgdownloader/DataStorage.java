/**
 * 
 */
package bgdownloader;

import java.io.File;

import javax.swing.JPanel;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

/**
 * @author bugman
 *
 */
public class DataStorage {
	String settingsDir;
	SQLiteConnection settingsDB;
	
	/**
	 * 
	 */
	public DataStorage(){
		
		// This if Block checks to see if it's windows or linux, and sets the
		// settings directory appropriately.
		if (System.getProperty("os.name").equalsIgnoreCase("linux"))
			settingsDir = System.getProperty("user.home").concat("/.bgdownloader");
		else if (System.getProperty("os.name").startsWith("Windows"))
			settingsDir = System.getProperty("user.home").concat("\\appdata\\local\\bgdownloader");

		// Check if the settings directory exists, and if not, create it.
		File settings = new File(settingsDir);
		if (!settings.exists()){
			settings.mkdir();
		}
	}
	
	
	/**
	 * 
	 * @return
	 */
	public int openSettings(){
		boolean firstRun = true;
		String configFile = settingsDir.concat("/podcast.db");
		
		if (new File(configFile).exists()){
			firstRun = false;
		}

		settingsDB= new SQLiteConnection(new File(configFile));
		
		try {
			settingsDB.open(true);
			
			if (firstRun){
				SQLiteStatement sql = settingsDB.prepare("CREATE TABLE IF NOT EXISTS podcasts (" +
						"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
						"name TEXT, " +
						"localFile TEXT, " +
						"url TEXT, " +
						"directory TEXT);");
				sql.stepThrough();
				sql.dispose();
			}	
		} catch (SQLiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		}
			
		return 0;
	}
	
	/**
	 * 
	 * @param tree
	 * @param cards
	 * @return
	 */
	public int getPodcasts (TreePane tree, JPanel cards){
		try {
			// Do a search in the podcasts table for podcasts stored in the system
			SQLiteStatement sql = settingsDB.prepare("SELECT * from podcasts;");
			while (sql.step()){
				if (sql.hasRow()){
					// With the podcasts found, import the information into the system
					// and load the podcast data from the database.
					RssFeedDetails podcast = new RssFeedDetails(sql.columnString(1),
																sql.columnString(2),
																sql.columnString(3),
																sql.columnString(4),
																this);
					podcast.start();
					tree.addrssFeed(podcast);
					cards.add(podcast.getDownloadList(),podcast.getFeedName());
				} else {
					// If there are no podcasts in the system yet
				}
			}
			sql.dispose();
		} catch (SQLiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		}		

		return 0;
	}
	
	/**
	 * 
	 * @return
	 */
	public int addPodcast(RssFeedDetails newPodcast, String newFeed){
		try {
			SQLiteStatement sql = settingsDB.prepare("INSERT INTO podcasts(name,localfile,url,directory) VALUES (" +
													"'"+newPodcast.getFeedName()+"'," +
													"'"+newPodcast.getdb()+"'," +
													"'"+newFeed+"'," +
													"'"+newPodcast.getLocalStore()+"');");
			sql.stepThrough();
			sql.dispose();
		} catch (SQLiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}
	
	public int createFeedDB(String feedFilename){
		SQLiteConnection feedDb = new SQLiteConnection (new File(feedFilename));
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
		return 0;
	}

}
