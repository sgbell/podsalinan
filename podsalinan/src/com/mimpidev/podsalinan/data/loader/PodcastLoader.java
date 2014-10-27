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
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import com.mimpidev.dev.sql.SqlException;
import com.mimpidev.dev.sql.TableView;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.data.Podcast;
import com.mimpidev.podsalinan.data.PodcastList;

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
	 * 
	 */
	public PodcastLoader(PodcastList podcasts, SqlJetDb dbConnection) {
		setPodcastList(podcasts);
		setTableName("podcasts");
		createColumnList(new Podcast().getDatabaseRecord()); // make sure this is getting set
		setdbTable(dbConnection);
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
	
	public void readTable(){
		ArrayList<Map<String,String>> recordSet = readFromTable();
		
		if ((recordSet!=null)&&(recordSet.size()>0))
		for (Map<String,String> record: recordSet){
			// Traverse the Map and create a podcast object
			for (final Map.Entry<String, String> entry : record.entrySet()){
				Podsalinan.debugLog.logError(entry.getKey()+" - "+entry.getValue());
			}
			Podcast newPodcast = new Podcast(record);
			newPodcast.setAdded(true);
			podcastList.add(newPodcast);
			File podcastFile = new File(this.getDbFile().getParent()+"/"+newPodcast.getDatafile()+".pod");
			if (podcastFile.exists()){
				SqlJetDb podcastDB = new SqlJetDb(podcastFile,true);
				try {
					podcastDB.open();
				} catch (SqlJetException e) {
					Podsalinan.debugLog.printStackTrace(e.getStackTrace());
				}

				EpisodeLoader episodeLoader = new EpisodeLoader(newPodcast,podcastDB);
				episodeLoader.readTable();
				episodeLoaders.add(episodeLoader);
			} else {
				Podsalinan.debugLog.logError("File does not exist");
			}
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
						insert(new HashMap<String,Object>(){{
							put("name",podcast.getName());
							put("localFile",podcast.getDatafile());
							put("url",podcast.getURL());
							put("directory",podcast.getDirectory());
							put("auto_queue",(podcast.isAutomaticQueue()?1:0));
						}});
						sqlType=TableView.ITEM_ADDED_TO_DATABASE;
					} catch (SqlException e) {
						Podsalinan.debugLog.printStackTrace(e.getStackTrace());
					}
				} else if (podcast.isRemoved()){
					try {
						delete(new HashMap<String, Object>(){{
							put("url",podcast.getURL());
						}});
					} catch (SqlException e) {
						Podsalinan.debugLog.printStackTrace(e.getStackTrace());
					}
					sqlType=TableView.ITEM_REMOVED_FROM_DATABASE;
				} else if (podcast.isUpdated()){
					try {
						update(new HashMap<String, Object>(){{
							put("name",podcast.getName());
							put("directory",podcast.getDirectory());
							put("url",podcast.getURL());
							put("auto_queue",(podcast.isAutomaticQueue()?1:0));
						}}, 
							new HashMap<String, Object>(){{
								put("localfile",podcast.getDatafile());
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
