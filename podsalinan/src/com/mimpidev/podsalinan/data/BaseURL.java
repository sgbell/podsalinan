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
 * 
 */
package com.mimpidev.podsalinan.data;

import java.net.URL;
import java.util.Map;

import com.mimpidev.dev.sql.DataRecord;
import com.mimpidev.dev.sql.field.FieldDetails;
import com.mimpidev.dev.sql.field.StringType;

/**
 * @author bugman
 *
 */
public class BaseURL extends DataRecord{

	public BaseURL (){
		fields.put("url", new StringType());
		fields.put("directory", new StringType());
	}
	
	public BaseURL (String url){
		this();
		setURL(url);
	}
	
	public BaseURL (URL url){
		this(url.toString());
	}
	
	public BaseURL(String url, boolean added){
		this(url);
		setAdded(added);
	}
	
	public BaseURL(URL url, boolean added){
		this(url.toString(),added);
	}
	
	public String getURL(){
		return fields.get("url").getValue();
	}
	
	public void setURL(String url){
		fields.get("url").setValue(url);
	}
	
	/**
	 * 
	 * @param newDirectory
	 */
	public void setDirectory(String newDirectory){
		fields.get("directory").setValue(newDirectory);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDirectory(){
		return fields.get("directory").getValue();
	}

	/**
	 * @return the fields
	 */
	public Map<String,FieldDetails> getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(Map<String,FieldDetails> fields) {
		this.fields = fields;
	}
}
/*
 * Changes to make
 * ===============
 * 1. Switch data storage to Mapped Variables - This will include upgrading the database from using
 *    autoincremented primary key to uid primary key for URLDownload and Podcasts
 * 2. Make URLDownload & Podcast extend a Database Item Object (may even extend an objecxt the extends the db item object)
 * 3. Have New Classes make the connection to the database, linking URLDownload and Podcast objects to the database
 * 4. DataStorage will be an array of these new db connection classes
 * 
 * 
 * URLDownload properties										Podcast Properties
 * ----------------------										------------------
 * variables													variables
 * ======================										==================
 * File destination												String directory
 * String podcastId												
 * String uid													
 * Url url														String url
 * String size
 * boolean added												boolean added												
 *         remove												boolean remove
 *         updated												boolean changed
 * int     status
 * 																String image
 * 																boolean automaticQueue
 * 																Vector<Episode> episodeList
 *																DateFormat df
 *																String settingsDir
 *																String name
 *																String datafile
 * ======================										===========================
 * methods														methods
 * ======================										===========================
 * String getDestination()										String getDirectory()
 * setDestination(String)										setDirectory(String)
 * setDestination(File)											
 * setURL(URL)													
 * setURL(String)												setURL(String)
 * URL getURL()													String getURL()
 * String getPodcastId()										
 * setPodcastId(String)											
 * String getFilenameDownload()									
 * File getDestinationFile()									
 * String getUid()												
 * setUid(String)												
 * char getCharStatus()
 * String getStatusString(int)
 * String getCurrentStatus()
 * setUpdated(boolean)											setChanged(boolean)
 * boolean isUpdated()											boolean isChanged()
 * setStatus(int)
 * int getStatus()
 * boolean isAdded()											boolean isAdded()
 * setAdded(boolean)											setAdded(boolean)
 * boolean isRemoved()											boolean isRemoved()
 * setRemoved(boolean)											setRemove(boolean)
 * String getSize()
 * setSize(String)
 * 																Vector<Episode> getEpisodes()
 * 																String getImage()
 * 																setImage(String)
 * 																updateList(String)
 * 																updateList(String, boolean)
 * 																int deleteEpisodeFromDrive(int)
 * 																int deleteEpisodeFromDrive(Episode)
 * 																int addEpisode(Episode)
 * 																Episode getEpisodeByURL(String)
 * 																Vector<Episode> getEpisodesByDate(Date)
 * 																int getEpisodeId(Episode)
 * 																Vector<Episode> getEpisodesByStatus(int)
 * 																scanDirectory(DataStorage)
 * 																boolean isAutomaticQueue()
 * 																setAutomaticQueue(boolean)
 * 																updateDatabase()
 * 																setSettingsDir(String)
 * 																readTable()
 * 																setDatafile(String)
 * 																String getDatafile()
 */ 
