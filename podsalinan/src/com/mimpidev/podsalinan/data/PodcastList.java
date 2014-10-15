/**
 * 
 */
package com.mimpidev.podsalinan.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import com.mimpidev.dev.sql.SqlException;
import com.mimpidev.dev.sql.TableView;
import com.mimpidev.dev.sql.data.definition.TableDefinition;
import com.mimpidev.podsalinan.Podsalinan;

/**
 * @author bugman
 *
 */
public class PodcastList {

	/**
	 * 
	 */
	private Vector<Podcast> podcasts;
	/**
	 * 
	 */
    private String settingsDir;
	
	/**
	 * 
	 */
	public PodcastList() {
		podcasts = new Vector<Podcast>();
	}

	public void add(Podcast newPodcast) {
		podcasts.add(newPodcast);
	}

	public Vector<Podcast> getList() {
		return podcasts;
	}

	public Podcast getPodcastByUid(String podcastUid) {
		for (Podcast currentPodcast: podcasts)
			if (currentPodcast.getDatafile().equals(podcastUid))
			   return currentPodcast;
		return null;
	}
}
