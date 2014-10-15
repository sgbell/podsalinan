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
import java.util.Map;
import java.util.Vector;

import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

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
	private PodcastList podcastList;
	/**
	 * @param podsalinanDB 
	 * 
	 */
	public PodcastLoader(PodcastList podcasts, SqlJetDb dbConnection) {
		setPodcastList(podcasts);
		tableName = "podcasts";
		createColumnList(new Podcast().getDatabaseRecord());
		setdbTable(dbConnection);
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
			Podcast newPodcast = new Podcast(record.get("name"),
					 						 record.get("url"),
					 						 record.get("directory"),
					 						 record.get("localfile").replaceAll("&apos;", "\'"),
					 						 record.get("auto_queue").equalsIgnoreCase("1"));
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

				newPodcast.setdbTable(podcastDB);
				newPodcast.readTable();
			} else {
				Podsalinan.debugLog.logError("File does not exist");
			}
		}		
	}
	
	public void updateDatabase(){
		
	}
}
