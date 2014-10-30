/**
 * 
 */
package com.mimpidev.podsalinan.data.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.tmatesoft.sqljet.core.table.SqlJetDb;

import com.mimpidev.dev.sql.SqlException;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.data.Episode;
import com.mimpidev.podsalinan.data.Podcast;

/**
 * @author sbell
 *
 */
public class EpisodeLoader extends TableLoader {

	/**
	 * 
	 */
	private Podcast podcast;
	/**
	 * 
	 */
	public EpisodeLoader(Podcast newPodcast, SqlJetDb dbConnection) {
		setTableName("shows");
		createColumnList(new Episode().getDatabaseRecord());
		setdbTable(dbConnection);
		podcast = newPodcast;
	}

	/**
	 * 
	 */
	public void updateDatabase() {

		if (podcast.getSettingsDir()!=null){
			for (final Episode episode : podcast.getEpisodes()){
				if (!episode.isAdded()){
					try {
						insert(new HashMap<String,Object>(){{
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
						update(new HashMap<String,Object>(){{
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
	 */
	public void readTable() {
		ArrayList<Map<String,String>> recordSet = readFromTable();
		
		if ((recordSet!=null)&&(recordSet.size()>0))
			for (Map<String,String> record: recordSet){
				Episode newEpisode = new Episode(record);
				newEpisode.setAdded(true);
				newEpisode.setUpdated(false);
				podcast.addEpisode(newEpisode);
			}
	}
}
