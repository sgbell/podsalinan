/**
 * 
 */
package com.mimpidev.podsalinan.data.loader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mimpidev.dev.sql.SqlException;
import com.mimpidev.dev.sql.field.FieldDetails;
import com.mimpidev.dev.sql.field.StringType;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.data.Episode;
import com.mimpidev.podsalinan.data.Podcast;
import com.mimpidev.sql.sqlitejdbc.Database;

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
	 * @throws ClassNotFoundException 
	 * 
	 */
	public EpisodeLoader(Podcast newPodcast, Database dbConnection) throws ClassNotFoundException {
		super("shows",dbConnection);
		setTableName("shows");
		createColumnList(new Episode().getDatabaseRecord());
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
						insert(episode.getDatabaseRecord());
						episode.setAdded(true);
					} catch (SqlException e) {
						Podsalinan.debugLog.printStackTrace(e.getStackTrace());
					}					
				} else if (episode.isUpdated()){
					try {
						update(episode.getDatabaseRecord(), 
								       new HashMap<String, FieldDetails>(){{
											put("url", new StringType(episode.getURL().toString().replaceAll("\'", "&apos;")));
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
