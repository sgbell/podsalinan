/**
 * 
 */
package bgdownloader;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

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
		
		// disables sqlite4java's logging
		Logger.getLogger("com.almworks.sqlite4java").setLevel(Level.OFF);
	}
	
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
		}
			
		return 0;
	}
}
