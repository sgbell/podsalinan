/**
 * 
 */
package bgdownloader;

import java.io.File;
import java.util.Vector;

import javax.swing.JPanel;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

/**
 * @author bugman
 *
 */
public class DataStorage {
	private String settingsDir;
	private Object syncObject;
	
	/**
	 * 
	 */
	public DataStorage(Object syncObject){
		this.syncObject=syncObject;
		
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
	
	/** loadSettings loads the settings from the database.
	 * 
	 * @return
	 */
	public int loadSettings(Vector<RssFeedDetails> podcasts, TreePane tree, JPanel card){
		boolean firstRun = true;
		SQLiteStatement sql;
		
		String configFile = settingsDir.concat("/podcast.db");
		
		if (new File(configFile).exists()){
			firstRun = false;
		}

		SQLiteConnection settingsDB = new SQLiteConnection(new File(configFile));
		
		try {
			settingsDB.open(true);
			
			if (firstRun){
				sql = settingsDB.prepare("CREATE TABLE IF NOT EXISTS podcasts (" +
						"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
						"name TEXT, " +
						"localFile TEXT, " +
						"url TEXT, " +
						"directory TEXT);");
				sql.stepThrough();
			} else {
				// Do a search in the podcasts table for podcasts stored in the system
				sql = settingsDB.prepare("SELECT * from podcasts;");
				while (sql.step()){
					if (sql.hasRow()){
						RssFeedDetails newPodcast = new RssFeedDetails(new Details(sql.columnString(1),
													  							   sql.columnString(2),
													  							   sql.columnString(3),
													  							   sql.columnString(4)),
													  							   this,
													  							   tree,
													  							   card,
													  							   syncObject);
						podcasts.add(newPodcast);
					}
				}
			}
			sql.dispose();
			settingsDB.dispose();
		} catch (SQLiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		}
		return 0;
	}
	
	/**
	 * 
	 * @param feedFilename
	 * @return
	 */
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

	/**
	 * 
	 */
	public void saveSettings(Vector<RssFeedDetails> podcasts) {
		SQLiteStatement sql;
		
		String configFile = settingsDir.concat("/podcast.db");
		SQLiteConnection settingsDB = new SQLiteConnection(new File(configFile));

		try {
			settingsDB.open(true);
			for (int pcc=0; pcc < podcasts.size(); pcc++){
				Details currentPodcast=podcasts.get(pcc).getDetails();
				if ((!currentPodcast.added)&&(!currentPodcast.remove)){
					sql = settingsDB.prepare("INSERT INTO podcasts(name, localfile, url, directory) VALUES('"+currentPodcast.name+"'," +
												"'"+currentPodcast.datafile+"'," +
												"'"+currentPodcast.url+"'," +
												"'"+currentPodcast.directory+"');");
					sql.stepThrough();
					sql.dispose();
					currentPodcast.added=true;
				} else if (currentPodcast.remove){
					sql = settingsDB.prepare("DELETE FROM podcasts WHERE localfile='"+currentPodcast.datafile+"';");
					sql.stepThrough();
					sql.dispose();
				} else if (currentPodcast.changed){
					sql = settingsDB.prepare("UPDATE podcasts SET directory='"+currentPodcast.directory+"';");
					sql.stepThrough();
					sql.dispose();
				}
			}
			settingsDB.dispose();
		} catch (SQLiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loadPodcastDB(Vector<Episode> episodes, String feedDbName, DownloadList downloads){
		String feedFilename=settingsDir.concat("/"+feedDbName+".pod");
		SQLiteConnection feedDb = new SQLiteConnection (new File(feedFilename));
		try {
			feedDb.open();
			SQLiteStatement sql = feedDb.prepare("SELECT * FROM shows;");
			while (sql.step()){
				Episode ep = new Episode(sql.columnString(1),
										 sql.columnString(2),
										 sql.columnString(3),
										 sql.columnString(4),
										 sql.columnString(5));
				ep.added=true;
				episodes.add(ep);
				downloads.addDownload(ep.title,ep.date,ep.url,"0%");
			}
		} catch (SQLiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param episodes
	 * @param feedDBName
	 */
	public void savePodcastDB(Vector<Episode> episodes, String feedDBName){
		boolean feedDBExists=false;
		SQLiteStatement sql;
		
		String feedFilename=settingsDir.concat("/"+feedDBName+".pod");
		
		if (new File(feedFilename).exists())
			feedDBExists = true;
		
		SQLiteConnection feedDB = new SQLiteConnection(new File(feedFilename));
		try {
			feedDB.open(true);
			
			if (!feedDBExists){
				sql = feedDB.prepare("CREATE TABLE IF NOT EXISTS shows(" +
										"id INTEGER PRIMARY KEY AUTOINCREMENT," +
										"published TEXT," +
										"title TEXT," +
										"url TEXT," +
										"size INTEGER," +
										"description TEXT);");
				sql.stepThrough();
				sql.dispose();
			}
			for (int epc=0; epc < episodes.size(); epc++){
				if (!episodes.get(epc).added){
					sql = feedDB.prepare("INSERT INTO shows(published,title,url,size,description) VALUES('"+
														episodes.get(epc).date+"'," +
							 							 "'"+episodes.get(epc).title+"'," +
							 							 "'"+episodes.get(epc).url+"'," +
							 							 "'"+episodes.get(epc).size+"'," +
							 							 "'"+episodes.get(epc).description+"');");
					sql.stepThrough();
					sql.dispose();
					episodes.get(epc).added=true;
				}					
			}
			feedDB.dispose();
		} catch (SQLiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSettingsDir(){
		return settingsDir;
	}

}
