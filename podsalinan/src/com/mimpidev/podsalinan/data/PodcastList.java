/**
 * 
 */
package com.mimpidev.podsalinan.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.tmatesoft.sqljet.core.table.SqlJetDb;

import com.mimpidev.dev.debug.Log;
import com.mimpidev.dev.sql.SqlException;
import com.mimpidev.dev.sql.TableView;
import com.mimpidev.dev.sql.data.definition.TableDefinition;
import com.mimpidev.podsalinan.Podsalinan;

/**
 * @author bugman
 *
 */
public class PodcastList extends TableDefinition {

	/**
	 * 
	 */
	private Vector<Podcast> podcasts;

	/**
	 * 
	 */
	public PodcastList() {
		podcasts = new Vector<Podcast>();
		
		tableName = "podcasts";
		String[] columnNames = {"id","name","localfile","url","directory","auto_queue"};
		String[] columnTypes = {"INTEGER PRIMARY KEY AUTOINCREMENT"
				               ,"TEXT","TEXT","TEXT","TEXT","INTEGER"};
		createColumnList(columnNames,columnTypes);
	}

	public void add(Podcast newPodcast) {
		podcasts.add(newPodcast);
	}

	public Vector<Podcast> getList() {
		return podcasts;
	}

	/**
	 * 
	 */
	public void readTable() {
		ArrayList<Map<String,String>> recordSet = readFromTable();
		
		if ((recordSet!=null)&&(recordSet.size()>0))
		for (Map<String,String> record: recordSet){
			// Traverse the Map and create a podcast object
			Podcast newPodcast = new Podcast(record.get("name"),
					 						 record.get("url"),
					 						 record.get("directory"),
					 						 record.get("localFile").replaceAll("&apos;", "\'"),
					 						 (Integer.parseInt(record.get("auto_queue"))==1));
			newPodcast.setAdded(true);
			podcasts.add(newPodcast);
		}
	}

	public void updateDatabase() {
		final Integer podcastCount= new Integer(0);
		for (final Podcast podcast : podcasts){
			int sqlType=TableView.NOTHING_CHANGED;
			if (!podcast.isAdded()){
				// Used to set the correct flag
				try {
					dbTable.insert(new HashMap<String,Object>(){{
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
					dbTable.delete(new HashMap<String, Object>(){{
						put("url",podcast.getURL());
					}});
				} catch (SqlException e) {
					Podsalinan.debugLog.printStackTrace(e.getStackTrace());
				}
				sqlType=TableView.ITEM_REMOVED_FROM_DATABASE;
			} else if (podcast.isChanged()){
				try {
					dbTable.update(new HashMap<String, Object>(){{
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
					podcasts.get(podcasts.indexOf(podcast)).setAdded(true);
					break;
				case TableView.ITEM_UPDATED_IN_DATABASE:
					podcasts.get(podcasts.indexOf(podcast)).setChanged(false);
					break;
			}
			podcast.setSettingsDir(this.getDbFile().getParent());
			
			podcast.updateDatabase();
		}
	}
}
