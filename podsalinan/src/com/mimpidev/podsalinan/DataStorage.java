/*******************************************************************************
 * Copyright (c) 2011 Sam Bell.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or  any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See
 * the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Sam Bell - initial API and implementation
 ******************************************************************************/
package com.mimpidev.podsalinan;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.schema.ISqlJetColumnDef;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import com.mimpidev.dev.debug.Log;
import com.mimpidev.dev.sql.SqlException;
import com.mimpidev.dev.sql.TableView;
import com.mimpidev.podsalinan.data.Episode;
import com.mimpidev.podsalinan.data.Podcast;
import com.mimpidev.podsalinan.data.PodcastList;
import com.mimpidev.podsalinan.data.ProgSettings;
import com.mimpidev.podsalinan.data.URLDownload;
import com.mimpidev.podsalinan.data.URLDownloadList;

/**
 * @author bugman
 *
 */
public class DataStorage {
    /**
     * URLDownloadList is storage area for all url downloads 
     */
	private URLDownloadList urlDownloads;
	/**
	 * PodcastList is the storage area for all of the podcasts
	 */
	private PodcastList podcasts;
	/**
	 * settings is where the program settings are listed
	 */
	private ProgSettings settings;
	private String settingsDir;
	private Object finishWait= new Object();
	private Log debugOutput=null;

	/**
	 *  SQL Statement for deleting all settings from main database file.
	 */
	private final String CLEAR_ALL_SETTINGS = "DELETE from settings;" +
								 			  "DELETE from sqlite_sequence " +
								 			  "WHERE name='settings';";
	
	/**
	 *  Used to Define the current File system slash.
	 */
	private String fileSystemSlash;
	
	public DataStorage(){
		podcasts = new PodcastList();
		urlDownloads = new URLDownloadList(podcasts);
		settings = new ProgSettings();
		
		checkSettingsDirectory();
	}
	
	public DataStorage(Vector<Podcast> podcasts, URLDownloadList urlDownloads,
			ProgSettings settings) {
		setPodcasts(podcasts);
		setUrlDownloads(urlDownloads);
		urlDownloads.setPodcasts(podcasts);
		setSettings(settings);
		
		checkSettingsDirectory();
	}
	
	/**
	 *  This is used to set settingsDir and fileSystemSlash for the current operating
	 *  system.
	 */
	public void checkSettingsDirectory(){
		// This if Block checks to see if it's windows or linux, and sets the
		// settings directory appropriately.
		if (System.getProperty("os.name").equalsIgnoreCase("linux")){
			settingsDir = System.getProperty("user.home").concat("/.podsalinan");
			fileSystemSlash = "/";
		}else if (System.getProperty("os.name").startsWith("Windows")){
			settingsDir = System.getProperty("user.home").concat("\\appdata\\local\\podsalinan");
			fileSystemSlash = "\\";
		}

		// Check if the settings directory exists, and if not, create it.
		File settingsLocation = new File(settingsDir);
		if (!settingsLocation.exists()){
			settingsLocation.mkdir();
		}
		
		File tempXMLFile = new File (settingsDir+fileSystemSlash+"temp.xml");
		if (tempXMLFile.exists())
			tempXMLFile.delete();

        // Initializing debugOutput File
		debugOutput = new Log(settingsDir+fileSystemSlash+"debug.log","rw");
	}

	/**
	 * Used to load this object's podcasts, downloads, and settings. 
	 * @return
	 */
	public int loadSettings(){
		return loadSettings(podcasts, urlDownloads, settings);
	}
	
	/**
 	 * Used to load the passed in podcast Vector, download array and settings.
	 * @param podcasts The Vector list used to store the values being read.
	 * @param downloads The download list used to store the values being read.
	 * @param settings The settings list used to store the values being read.
	 * @return Success status -1 is failure 0 is success
	 */
	public int loadSettings(PodcastList podcasts,
							URLDownloadList downloads,
							ProgSettings settings){
		
		File podsalinanDBFile = new File(settingsDir.concat("/podsalinan.db"));
		if (podsalinanDBFile.exists()){
			SqlJetDb podsalinanDB = new SqlJetDb(podsalinanDBFile,true);
			
			//TODO: After converting all of the table reads to similar lines as below, add them to an array to do all of them in a smaller bunch of code calls
			downloads.setdbTable(podsalinanDB, debugOutput);
			downloads.readTable();
			settings.setdbTable(podsalinanDB, debugOutput);
			settings.readTable();
			podcasts.setdbTable(podsalinanDB, debugOutput);
			podcasts.readTable();
			
			try {
				podsalinanDB.close();
			} catch (SqlJetException e) {
				debugOutput.printStackTrace(e.getStackTrace());
				return -1;
			}
		}
		if (!settings.isValidSetting("defaultDirectory")){
			if (System.getProperty("os.name").startsWith("Windows"))
			    settings.addSetting("defaultDirectory", System.getProperty("user.home").concat("\\Download"));
			else
			    settings.addSetting("defaultDirectory", System.getProperty("user.home").concat("/Download"));
		}
		return 0;
	}
	
	/**
	 * This is used to save the locally stored information to the databases
	 */
	public void saveSettings(){
		saveSettings(podcasts,urlDownloads,settings);
	}

    /**
     * This is used to save the passed in information to the databases 
     * @param podcasts The podcast Vector
     * @param downloads The downloads Array
     * @param settings The settings Array
     */
	public void saveSettings(PodcastList podcasts,
							 URLDownloadList downloads,
							 ProgSettings settings) {
		
		File podsalinanDBFile = new File(settingsDir.concat("/podsalinan.db"));
		SqlJetDb db = new SqlJetDb(podsalinanDBFile,true);
		downloads.setdbTable(db, debugOutput);
		downloads.updateDatabase();
		
		settings.setdbTable(db, debugOutput);
		settings.updateDatabase();
		
		podcasts.setdbTable(db, debugOutput);
		podcasts.updateDatabase();
		

		savePodcast(podcast);
	}

	/**
	 * This will populate a podcast object with all of the episodes stored in the
	 * podcast database file. The passed in Podcast object will have settings pre-populated
	 * before being passed in, like the dataFile name.
	 * @param podcast Podcast Vector to be populated.
	 */
	public void loadPodcast(Podcast podcast){
		String feedFilename=settingsDir.concat("/"+podcast.getDatafile()+".pod");
		SQLiteConnection feedDb = new SQLiteConnection (new File(feedFilename));
		SQLiteStatement sql;

		try {
			feedDb.open();
			
			addColumnToTable(feedDb,"shows","status","INTEGER");

			sql = feedDb.prepare("SELECT * FROM shows;");
			while (sql.step()){
				Episode ep = new Episode(sql.columnString(1),
										 sql.columnString(2).replaceAll("&apos;", "\'"),
										 sql.columnString(3).replaceAll("&apos;", "\'"),
										 sql.columnString(4),
										 sql.columnString(5).replaceAll("&apos;", "\'"),
										 sql.columnInt(6));
				ep.setAdded(true);
				ep.setUpdated(false);
				//podcast.getEpisodes().add(ep);
				podcast.addEpisode(ep);
				//sqlCount++;
			}
			sql.dispose();
			//System.out.println("Podcast: "+podcast.getName()+" - "+podcast.getEpisodes().size());
			//System.out.println("DB count: "+sqlCount);
		} catch (SqlJetException e) {
			debugOutput.printStackTrace(e.getStackTrace());
		}
	}
	
	/** 
	 * Used to check for a column in a table, and if it doesn't exist, add it.
	 */
	private boolean addColumnToTable(SqlJetDb dbConnection, String tableName, String columnName, String columnType){
		boolean columnFound=false;
		List<ISqlJetColumnDef> columnList = null;

		try {
			columnList = dbConnection.getSchema().getTable(tableName).getColumns();
		} catch (SqlJetException e) {
			debugOutput.printStackTrace(e.getStackTrace());
		}
		if (columnList!=null){
			for (ISqlJetColumnDef column: columnList){
				if (column.getName().contentEquals(columnName))
					columnFound=true;
			}
		} else {
			return false;
		}
		
		if (!columnFound){
			try {
				dbConnection.alterTable("ALTER TABLE "+tableName+" ADD COLUMN "+columnName+" "+columnType.toUpperCase()+";");
			} catch (SqlJetException e) {
				debugOutput.printStackTrace(e.getStackTrace());
				return false;
			}
			return true;
		} else {
			return true;
		}
	}
	
    /**
     * This will save the episodes stored in the Podcast Array to the dataFile.
     * @param savedPodcast The podcast to save
     */
	public void savePodcast(Podcast savedPodcast){
		boolean feedDBExists=false;
		
		String feedFilename=settingsDir.concat("/"+savedPodcast.getDatafile()+".pod");
		
		if (new File(feedFilename).exists())
			feedDBExists = true;
		
		SQLiteConnection feedDB = new SQLiteConnection(new File(feedFilename));
		try {
			feedDB.open(true);
		} catch (SqlJetException e) {
			debugOutput.printStackTrace(e.getStackTrace());
		}
			
		if (!feedDBExists){
			try {
				sql = feedDB.prepare(CREATE_SHOWS);
				sql.stepThrough();
				sql.dispose();
			} catch (SqlJetException e) {
				debugOutput.printStackTrace(e.getStackTrace());
			}
		}
			
		for (int epCount=0; epCount< savedPodcast.getEpisodes().size(); epCount++){
			Episode currentEpisode = savedPodcast.getEpisodes().get(epCount);
			if (!currentEpisode.isAdded()){
				try {
					//System.out.println("Adding URL to database: "+currentEpisode.getURL().toString());
					sql = feedDB.prepare("INSERT INTO shows(published,title,url,size,description,status)" +
									     "VALUES ('"+currentEpisode.getOriginalDate()+"'," +
							 				     "'"+currentEpisode.getTitle().replaceAll("\'", "&apos;")+"'," +
							 					 "'"+currentEpisode.getURL().toString().replaceAll("\'", "&apos;")+"'," +
							 					 "'"+currentEpisode.getSize()+"'," +
							 					 "'"+currentEpisode.getDescription().replaceAll("\'", "&apos;")+"'," +
							 					     currentEpisode.getStatus()+");");
					sql.stepThrough();
					sql.dispose();
					currentEpisode.setAdded(true);
				} catch (SqlJetException e) {
					debugOutput.printStackTrace(e.getStackTrace());
				}
			} else if (currentEpisode.isUpdated()) {
				try {
					// If the episode has been updated. it will up updated in the database.
					//System.out.println("Updating DB: "+currentEpisode.getURL().toString());
					sql = feedDB.prepare("UPDATE shows " +
					 		   			 "SET status="+currentEpisode.getStatus()+", " +
					 		   			 "description='"+currentEpisode.getDescription().replaceAll("\'", "&apos;")+"', " +
					 		   			 "size='"+currentEpisode.getSize()+"', " +
					 		   			 "published='"+currentEpisode.getOriginalDate()+"' " +
					 		   			 "WHERE url='"+currentEpisode.getURL().toString().replaceAll("\'", "&apos;")+"';");
					sql.stepThrough();
					sql.dispose();
					currentEpisode.setUpdated(false);
				} catch (SqlJetException e) {
					debugOutput.printStackTrace(e.getStackTrace());
				}
			}
				
			cleanDownloadsinDB(feedDB, currentEpisode.getURL(),"shows");
		}
		feedDB.dispose();
	}
	
	/**
	 * This method is used to clean duplicate episodes/downloads out of the database. I created this, because in changing the output of the
	 * episode string, I forgot to make the original format available. This caused every episode to be written to the database
	 * every time the program was executed :(
	 * 
	 * @param podsalinanDB Database connection to an sqlite file to delete duplicates
	 * @param currentUrl URL string to match on in the database   
	 * @param tableName Table name to be checked.
	 */
	private void cleanDownloadsinDB(SqlJetDb podsalinanDB, URL currentUrl, String tableName) {
		
		SQLiteStatement sql;
		
		if ((podsalinanDB!=null) && (currentUrl!=null) && 
			((tableName.equalsIgnoreCase("shows"))||(tableName.equalsIgnoreCase("downloads")))){
			// Looking for multiple copies of the 1 episode
			String urlID=null;
            // The following will search through the database for the current episode's minimum id
			try {
				sql = podsalinanDB.prepare("SELECT min(id) "
							           + "FROM   "+tableName+" "
							           + "WHERE  url='"+currentUrl.toString().replaceAll("\'", "&apos;")+"';");
				while (sql.step()){
					urlID=sql.columnString(0);
				}
				sql.dispose();
			} catch (SqlJetException e) {
				debugOutput.printStackTrace(e.getStackTrace());
			}
			// Using the current episode's db minimum id, it will remove all matching episodes except for the minimum id
				
			if (urlID!=null){
				/*
				int urlCount=0;
				sql = podsalinanDB.prepare("SELECT count(*) "
								   + "FROM "+tableName+" "
								   + "WHERE url='"+currentUrl.toString().replaceAll("\'", "&apos;")+"';");
				while(sql.step()){
					urlCount=Integer.parseInt(sql.columnString(0));
				}
				sql.dispose();
				if (urlCount>1){
					sql = podsalinanDB.prepare("SELECT id "
					           + "FROM "+tableName+" "
					           + "WHERE url='"+currentUrl.toString().replaceAll("\'", "&apos;")+"';");
					while(sql.step()){
						System.out.println("URL='"+currentUrl.toString().replaceAll("\'", "&apos;")+"' - "+
										   sql.columnString(0));
					}
						
				}*/
				
				try {
					sql = podsalinanDB.prepare("DELETE "
							           + "FROM  "+tableName+" "
							           + "WHERE url='"+currentUrl.toString().replaceAll("\'", "&apos;")+"' "
							           + "AND   id!="+urlID+";");
					sql.stepThrough();
					sql.dispose();
				} catch (SqlJetException e) {
					debugOutput.printStackTrace(e.getStackTrace());
				}
			}
		}
	}

	/**
	 * 
	 * @return String for settings directory 
	 */
	public String getSettingsDir(){
		return settingsDir;
	}

	/**
	 * @return Download List
	 */
	public URLDownloadList getUrlDownloads() {
		return urlDownloads;
	}

	/**
	 * @param urlDownloads Array of Downloads
	 */
	public void setUrlDownloads(URLDownloadList urlDownloads) {
		this.urlDownloads = urlDownloads;
	}

	/**
	 * @return Array list of Podcasts
	 */
	public Vector<Podcast> getPodcasts() {
		return podcasts;
	}

	/**
	 * @param podcasts Array of Podcasts
	 */
	public void setPodcasts(Vector<Podcast> podcasts) {
		this.podcasts = podcasts;
	}

	/**
	 * @return Array list of Settings
	 */
	public ProgSettings getSettings() {
		return settings;
	}

	/**
	 * @param settings Array of Program Settings
	 */
	public void setSettings(ProgSettings settings) {
		this.settings = settings;
	}

	/**
	 * @return the type of file system slash
	 */
	public String getFileSystemSlash() {
		return fileSystemSlash;
	}

	/**
	 * @param fileSystemSlash Defines the current file system slash
	 */
	public void setFileSystemSlash(String fileSystemSlash) {
		this.fileSystemSlash = fileSystemSlash;
	}

	/**
	 * @return object for waiting till the reading is finished
	 */
	public Object getFinishWait() {
		return finishWait;
	}

	/**
	 * @param finishWait the finishWait to set
	 */
	public void setFinishWait(Object finishWait) {
		this.finishWait = finishWait;
	}
	
	/**
	 * This is a directory scanning method, which will create a list of files recursive
	 * in the directory passed in.
	 * @param directory Directory to be scanned for files
	 * @param fileList Array of File objects found 
	 */
	public void scanDirectory(File directory, List<File> fileList){
		if (directory == null)
			return;
		if ((directory.exists())&&
			(directory.isDirectory())){
			File[] files = directory.listFiles();
			for (File file : files){
				if (file.isDirectory())
					scanDirectory(file,fileList);
				else
					fileList.add(file);
			}
		}
	}
	
	/**
	 * This is used as the main debug output, if it could not open the file, it will
	 * output the debug information to the screen.
	 * @return Log object, attached to file
	 */
	public Log getDebugFile(){
		return debugOutput;
	}
}
