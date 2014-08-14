/**
 * 
 */
package com.mimpidev.podsalinan;

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

}
