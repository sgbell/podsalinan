/**
 * 
 */
package com.mimpidev.podsalinan.data;

import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

import com.mimpidev.dev.sql.data.definition.TableDefinition;

/**
 * @author bugman
 *
 */
public class PodcastList extends TableDefinition {

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
}
