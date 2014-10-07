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
/**
 *  This is the class used to store details about the rss feed
 */
package com.mimpidev.podsalinan.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.mimpidev.dev.sql.SqlException;
import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Downloader;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.XmlReader;
import com.mimpidev.podsalinan.data.fields.BooleanType;
import com.mimpidev.podsalinan.data.fields.StringType;

/**
 * @author bugman
 *
 */
public class Podcast extends DownloadDetails{

	/**
	 * 
	 */
	private String  image;
	/**
	 * 
	 */
	private boolean automaticQueue=false;
	/**
	 * 
	 */
	private Vector<Episode> episodeList = new Vector<Episode>(); // Used to store the the downloads, seperate from the DownloadList
	/**
	 * 
	 */
	private DateFormat df;
	/**
	 * 
	 */
	private String settingsDir = null;
	
	/**This is used to create a new Podcast with only a url.
	 * 
	 * @param newURL
	 */
	public Podcast(String newURL){
		super(null);
		fields.put("image", new StringType());
		fields.put("automaticQueue", new BooleanType());
		
		df = new SimpleDateFormat(Episode.getDateFormat());
		tableName = "shows";
		
		String[] columnNames = {"id","published","title","url","size","description","status"};
		String[] columnTypes = {"INTEGER PRIMARY KEY AUTOINCREMENT"
				               ,"TEXT","TEXT","TEXT","INTEGER","TEXT","INTEGER"};
		createColumnList(columnNames,columnTypes);
	}
	
	/** Used to create a Podcast from the information in the systems database. 
	 * 
	 * @param newName
	 * @param newURL
	 * @param newDirectory
	 * @param newDatastore
	 */
	public Podcast(String newName, String newURL, String newDirectory, String newDatastore){
		this(newURL);
		
		setName(newName);
		setDirectory(newDirectory);
		setDatafile(newDatastore);
	}
	
	public Podcast(String newName, String newURL, String newDirectory, String newDatastore, boolean autoDownload){
		this(newName,newURL,newDirectory,newDatastore);
		automaticQueue=autoDownload;
	}
	
	public Vector<Episode> getEpisodes(){
		return episodeList;
	}
	
	public void setURL(String url){
		this.url=url;
	}
	
	public String getURL(){
		return url;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getDirectory() {
		return directory;
	}
	
	/**
	 * @return the changed
	 */
	public boolean isChanged() {
		return changed;
	}

	/**
	 * @param changed the changed to set
	 */
	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	/**
	 * @return the remove
	 */
	public boolean isRemoved() {
		return remove;
	}

	/**
	 * @param remove the remove to set
	 */
	public void setRemove(boolean remove) {
		this.remove = remove;
	}

	/**
	 * @return the added
	 */
	public boolean isAdded() {
		return added;
	}

	/**
	 * @param added the added to set
	 */
	public void setAdded(boolean added) {
		this.added = added;
	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}
	
	/**
	 * This method calls updateList(String,boolean), but automatically sets manualUpdate to false so
	 * that the autoUpdater in the program will not have to be rewritten.
	 * @param tempDir
	 */
	public void updateList(String tempDir){
		updateList(tempDir,false);
	}
	
	/**
	 * This method is used when the user manually prompts to update the podcast
	 * @param tempDir
	 * @param manualUpdate
	 */
	public void updateList(String tempDir, boolean manualUpdate){
		if (tempDir!=null){
			File outputFile;
			if (!manualUpdate)
				outputFile = new File(tempDir+"/temp.xml");
			else
				outputFile = new File(tempDir+"/manualtemp.xml");
			
			if ((manualUpdate)&&(outputFile.exists()))
				outputFile.delete();
			
			/* Added this, so that if an update occurs while another update is already happening,
			 * the requested update wont occur. If the system is already in the middle of a full
			 * update of the podcasts, it wont matter if the user has requested for an update, as
			 * one will either have already occurred, or will occur shortly. 
			 */
			if (!outputFile.exists()){
				try {
					Downloader downloader = new Downloader(new URL(url), outputFile);
					int result = downloader.getFile();
					if (result==Downloader.DOWNLOAD_COMPLETE){
						XmlReader xmlfile = new XmlReader();
						
						// Read the episode list from the xml file.
						Vector<Episode> newEpisodeList = xmlfile.parseEpisodes(new FileInputStream(outputFile));
						synchronized(episodeList){
							if (episodeList!=null){
								for (Episode newEpisode : newEpisodeList){
									boolean foundEpisode=false;
									int episodeCount=0;
									// Using a while loop here, because we don't want to continue looking for an episode
									// if it is already found
									while ((!foundEpisode)&&(episodeCount<episodeList.size())){
										// This code does not seem to be working, as it should be finding the url :(
										Episode currentEpisode = episodeList.get(episodeCount);
										if (newEpisode.getURL().toString().equalsIgnoreCase(currentEpisode.getURL().toString()))
											foundEpisode=true;
										else
											episodeCount++;
									}
									if (!foundEpisode){
										addEpisode(newEpisode);
										//System.out.println("Not Found Episode: "+newEpisode.getURL().toString());
									}// else
									//	System.out.println("Found Episode: "+newEpisode.getURL().toString());
								}
							}
						}
					}
					// Delete the temp file from the filesystem
					outputFile.delete();
				} catch (MalformedURLException e) {
				} catch (FileNotFoundException e) {
				}
			}
		}
	}

	/**
	 * 
	 * @param episodeCount
	 * @return 0 if successfully deleted, -1 if error occurred
	 */
	public int deleteEpisodeFromDrive(int episodeCount) {
		synchronized(episodeList){
			if (episodeList.size()>0)
				if ((episodeCount<episodeList.size())&&
					(episodeCount>=0)){
					return deleteEpisodeFromDrive(episodeList.get(episodeCount));
				}
		}
		
		return -1;
	}
	
	/**
	 * 
	 * @param selectedEpisode
	 * @return
	 */
	public int deleteEpisodeFromDrive(Episode selectedEpisode){
		/* 
		 * The reason this method is not in episode is because the destination directory is stored this class
		 */
		if (selectedEpisode!=null)
			synchronized(selectedEpisode){
				File destinationFile=null;
				if (System.getProperty("os.name").equalsIgnoreCase("linux"))
					destinationFile = new File(getDirectory()+"/"+selectedEpisode.getURL().getFile());
				else if (System.getProperty("os.name").startsWith("Windows"))
					destinationFile = new File(getDirectory()+"\\"+selectedEpisode.getURL().getFile());
				if (destinationFile!=null){
					if (destinationFile.exists()){
						destinationFile.delete();
						selectedEpisode.setStatus(Episode.NOT_QUEUED);
						return 0;
					}
				}
				
			}
		
		return -1;
	}
	
	/**
	 * This Method is used to add the episodes to the array, sorted by date. Newest first.
	 * @param newEpisode
	 * @return
	 */
	public int addEpisode(Episode newEpisode){
		if (episodeList!=null)
			synchronized(episodeList){
				if (episodeList.size()>0){
					try {
						Date newEpisodeDate = df.parse(newEpisode.getDate());
						boolean found=false;
						int epCount=0;
						while ((!found)&&(epCount<episodeList.size())){
							Date currentEpisodeDate = df.parse(episodeList.get(epCount).getDate());
							if (newEpisodeDate.after(currentEpisodeDate))
								found=true;
							else
								epCount++;
						}
						if (found)
							episodeList.add(epCount, newEpisode);
						else
							episodeList.add(newEpisode);
						return 0;
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
				} else {
					episodeList.add(newEpisode);
					return 0;
				}
			}
		
		return -1;
	}

	public Episode getEpisodeByURL(String string) {
		for (Episode currentEpisode : episodeList)
			if (currentEpisode.getURL().toString().equalsIgnoreCase(string))
				return currentEpisode;
		
		return null;
	}
	
	public Vector<Episode> getEpisodesByDate(Date searchDate){
		Vector<Episode> searchResults = new Vector<Episode>();
		
		for (Episode currentEpisode : episodeList){
			if (currentEpisode.dateEquals(searchDate))
				searchResults.add(currentEpisode);
		}
		
		return searchResults;
	}

	public int getEpisodeId(Episode episode) {
		for (int episodeCount=0; episodeCount< episodeList.size(); episodeCount++)
			if (episodeList.get(episodeCount).equals(episode))
				return episodeCount;
		
		return -1;
	}
	
	public Vector<Episode> getEpisodesByStatus(int statusValue){
		Vector<Episode> searchResults = new Vector<Episode>();
		
		for (Episode episode : episodeList){
			if (episode.getStatus()==statusValue)
				searchResults.add(episode);
		}
		
		return searchResults;
	}
	
	/** scanDirectory will check the current directory for episode downloads that have completed
	 * 
	 */
	public void scanDirectory(DataStorage data){
		ArrayList<File> filesInDir = new ArrayList<File>();
        //int count=0;
		String directoryToScan;
		try {
			directoryToScan=directory.substring(0, directory.indexOf("Download"));
		} catch (StringIndexOutOfBoundsException e){
			directoryToScan = this.directory;
		}
		File directoryFile = new File (directoryToScan);
		data.scanDirectory(directoryFile, filesInDir);
		for (Episode episode : episodeList)
			if (episode.getStatus()==Details.FINISHED){
				String filename = episode.getURL().toString().split("/")[episode.getURL().toString().split("/").length-1];
				boolean found=false;
				int fileCount=0;
				File file=null;
				while ((fileCount<filesInDir.size())&&(!found)){
					file = filesInDir.get(fileCount);
					if (file.getName().equalsIgnoreCase(filename))
						found=true;
					fileCount++;
				}
				if (!found)
					episode.setStatus(Details.NOT_QUEUED);
			} else if (episode.getStatus()!=Details.FINISHED){
				String filename = episode.getURL().toString().split("/")[episode.getURL().toString().split("/").length-1];
				boolean found=false;
				int fileCount=0;
				File file=null;
				while ((fileCount<filesInDir.size())&&(!found)){
					file = filesInDir.get(fileCount);
					if ((file.getName().equalsIgnoreCase(filename))&&
						(file.length()>=Long.parseLong(episode.getSize()))){
						episode.setStatus(Details.FINISHED);
						found=true;
					} else
						fileCount++;
				}
				// The reason for removing the file from the filesInDir array, is it means 1 less record for the system to check against.
				if (found)
					filesInDir.remove(file);
			}
	}

	/**
	 * @return the automaticQueue
	 */
	public boolean isAutomaticQueue() {
		return automaticQueue;
	}

	/**
	 * @param automaticQueue the automaticQueue to set
	 */
	public void setAutomaticQueue(boolean automaticQueue) {
		this.automaticQueue = automaticQueue;
	}

	/**
	 * 
	 */
	public void updateDatabase() {

		if (settingsDir!=null){
			for (final Episode episode : episodeList){
				if (!episode.isAdded()){
					try {
						dbTable.insert(new HashMap<String,Object>(){{
							put("published",episode.getOriginalDate());
							put("title",episode.getTitle().replaceAll("\'", "&apos;"));
							put("url",episode.getURL().toString().replaceAll("\'", "&apos;"));
							put("size",episode.getSize());
							put("description",episode.getDescription().replaceAll("\'", "&apos;"));
							put("status",episode.getStatus());
						}});
						episode.setAdded(true);
					} catch (SqlException e) {
						Podsalinan.debugLog.printStackTrace(e.getStackTrace());
					}					
				} else if (episode.isUpdated()){
					try {
						dbTable.update(new HashMap<String,Object>(){{
							put("status",episode.getStatus());
							put("description",episode.getDescription().replaceAll("\'", "&apos;"));
							put("size",episode.getSize());
							put("published",episode.getOriginalDate());
						}}, 
								       new HashMap<String, Object>(){{
											put("url",episode.getURL().toString().replaceAll("\'", "&apos;"));
								       }});
						episode.setUpdated(false);
					} catch (SqlException e) {
						Podsalinan.debugLog.printStackTrace(e.getStackTrace());
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param string
	 */
	public void setSettingsDir(String newSettingsDir) {
		settingsDir = newSettingsDir;
	}

	/**
	 * 
	 */
	public void readTable() {
		ArrayList<Map<String,String>> recordSet = readFromTable();
		
		if ((recordSet!=null)&&(recordSet.size()>0))
			for (Map<String,String> record: recordSet){
				Episode newEpisode = new Episode(
						record.get("published"),
						record.get("title").replaceAll("&apos;", "\'"),
						record.get("url").replaceAll("&apos;", "\'"),
						record.get("size"),
						record.get("description").replaceAll("&apos;", "\'"),
						Integer.parseInt(record.get("status")));
				newEpisode.setAdded(true);
				newEpisode.setUpdated(false);
				addEpisode(newEpisode);
			}
	}	
}
