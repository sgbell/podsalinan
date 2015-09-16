/*******************************************************************************
 * Copyright (c) Sam Bell.
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
package com.mimpidev.podsalinan.data.loader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mimpidev.dev.sql.SqlException;
import com.mimpidev.dev.sql.TableView;
import com.mimpidev.dev.sql.field.FieldDetails;
import com.mimpidev.dev.sql.field.StringType;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.data.Podcast;
import com.mimpidev.podsalinan.data.PodcastList;
import com.mimpidev.sql.sqlitejdbc.Database;
import com.mimpidev.sql.sqlitejdbc.exceptions.SqliteException;

/**
 * @author bugman
 *
 */
public class PodcastLoader extends TableLoader {

	/**
	 * 
	 */
	private ArrayList<EpisodeLoader> episodeLoaders;
	/**
	 * 
	 */
	private PodcastList podcastList;
	/**
	 * @param podsalinanDB 
	 * @throws ClassNotFoundException 
	 * 
	 */
	public PodcastLoader(PodcastList podcasts, Database dbConnection) throws ClassNotFoundException {
		super("podcasts",dbConnection);
		setPodcastList(podcasts);
		setTableName("podcasts");
		createColumnList(new Podcast().getDatabaseRecord()); // make sure this is getting set
		episodeLoaders = new ArrayList<EpisodeLoader>();
	}
	/**
	 * @return the podcastList
	 */
	public PodcastList getPodcastList() {
		return podcastList;
	}
	/**
	 * @param podcastList the podcastList to set
	 */
	public void setPodcastList(PodcastList podcastList) {
		this.podcastList = podcastList;
	}
	
	public void readTable() throws ClassNotFoundException{
		ArrayList<Map<String,String>> recordSet = readFromTable();
		
		if ((recordSet!=null)&&(recordSet.size()>0)){
			for (Map<String,String> record: recordSet){
				// Traverse the Map and create a podcast object
				if (isDebug())
					for (final Map.Entry<String, String> entry : record.entrySet()){
						Podsalinan.debugLog.logError(entry.getKey()+" - "+entry.getValue());
					}
				final Podcast newPodcast = new Podcast(record);
				// If we are working with an older version of the database, bring the value of localFile over to datafile
				if ((!newPodcast.getDatabaseRecord().containsKey("localFile"))&&
						(record.containsKey("localFile") && record.get("localFile")!=null)&&
						(newPodcast.getDatafile().equals("")||newPodcast.getDatafile()==null)){
					    newPodcast.setDatafile(record.get("localFile"));
					    try {
							update(newPodcast.getDatabaseRecord(), 
								"localFile="+newPodcast.getDatafile());
						} catch (SqlException e) {
							Podsalinan.debugLog.printStackTrace(e.getStackTrace());
						}
				}
				// If the podcast record is faulty. delete it
				if (newPodcast.getDatafile().equals("")){
					try {
						delete(new HashMap<String, FieldDetails>(){/**
							 * 
							 */
							private static final long serialVersionUID = -7040466909383675903L;

						{
							put("url",new StringType(newPodcast.getURL()));
						}});
					} catch (SqlException e) {
						Podsalinan.debugLog.printStackTrace(e.getStackTrace());
					}
				} else {
					newPodcast.setAdded(true);
					podcastList.add(newPodcast);
					final File podcastFile = new File(this.getDbFile().getParent()+"/"+newPodcast.getDatafile()+".pod");
					if (podcastFile.exists()){
						// To increase the speed of start up time, I have moved the episode loading to the background
						Database podcastDB=null;
						try {
							podcastDB = new Database(podcastFile.getAbsolutePath());
						} catch (SqliteException e){
							Podsalinan.debugLog.printStackTrace(e.getStackTrace());
						} catch  (ClassNotFoundException e) {
							Podsalinan.debugLog.printStackTrace(e.getStackTrace());
						}

						if (podcastDB!=null){
							EpisodeLoader episodeLoader;
							try {
								episodeLoader = new EpisodeLoader(newPodcast,podcastDB);
								//episodeLoader.readTable();
								episodeLoaders.add(episodeLoader);
							} catch (ClassNotFoundException e) {
								Podsalinan.debugLog.printStackTrace(e.getStackTrace());
							}
						}
					} else {
						Podsalinan.debugLog.logError("File does not exist");
					}
				}
			}
			// Loading the episodes in 1 background thread
			Thread episodeLoader = new Thread("EpisodeLoader"){
				public void run(){
					for (EpisodeLoader loader: episodeLoaders){
						loader.readTable();
					}
				}
			};
			episodeLoader.start();
		} else {
			Podsalinan.debugLog.logError("Error reading from Podcast Table");
		}
	}

	/**
	 * 
	 */
	public void updateDatabase(){
		if (isDbOpen()){
			for (final Podcast podcast : podcastList.getList()){
				int sqlType=TableView.NOTHING_CHANGED;
				if (!podcast.isAdded()){
					// Used to set the correct flag
					try {
						insert(podcast.getDatabaseRecord());
						sqlType=TableView.ITEM_ADDED_TO_DATABASE;
						final File podcastFile = new File(this.getDbFile().getParent()+"/"+podcast.getDatafile()+".pod");
						if (!podcastFile.exists()){
							Database podcastDB=null;
							try {
								podcastDB = new Database(podcastFile.getAbsolutePath());
								if (podcastDB!=null){
									EpisodeLoader episodeLoader = new EpisodeLoader(podcast,podcastDB);
									episodeLoaders.add(episodeLoader);
								}
							} catch (ClassNotFoundException
									| SqliteException e) {
								Podsalinan.debugLog.printStackTrace(e.getStackTrace());
							}
						}
					} catch (SqlException e) {
						Podsalinan.debugLog.printStackTrace(e.getStackTrace());
					}
				} else if (podcast.isRemoved()){
					try {
						delete(new HashMap<String, FieldDetails>(){/**
							 * 
							 */
							private static final long serialVersionUID = -7040466909383675903L;

						{
							put("url",new StringType(podcast.getURL()));
						}});
					} catch (SqlException e) {
						Podsalinan.debugLog.printStackTrace(e.getStackTrace());
					}
					sqlType=TableView.ITEM_REMOVED_FROM_DATABASE;
				} else if (podcast.isUpdated()){
					try {
						update(podcast.getDatabaseRecord(), 
							new HashMap<String, FieldDetails>(){/**
								 * 
								 */
								private static final long serialVersionUID = -3602277504930209966L;

							{
								put("datafile",new StringType(podcast.getDatafile()));
						}});
					} catch (SqlException e) {
						Podsalinan.debugLog.printStackTrace(e.getStackTrace());
					}
					sqlType=TableView.ITEM_UPDATED_IN_DATABASE;
				}
				switch (sqlType){
					case TableView.ITEM_ADDED_TO_DATABASE:
						podcastList.getList().get(podcastList.getList().indexOf(podcast)).setAdded(true);
						break;
					case TableView.ITEM_UPDATED_IN_DATABASE:
						podcastList.getList().get(podcastList.getList().indexOf(podcast)).setUpdated(false);
						break;
				}
				for (EpisodeLoader currentLoader : episodeLoaders){
					currentLoader.updateDatabase();
				}
			}
		} else {
			Podsalinan.debugLog.logError("Error db connection is closed");
		}
	}
}
