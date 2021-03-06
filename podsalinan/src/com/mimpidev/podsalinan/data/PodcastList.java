/**
 * 
 */
package com.mimpidev.podsalinan.data;

import java.util.ArrayList;
import java.util.Vector;

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
		synchronized (podcasts){
			for (Podcast currentPodcast: podcasts)
				if (currentPodcast.getDatafile().equals(podcastUid))
				   return currentPodcast;
		}
		return null;
	}
	
	/**
	 * 
	 * @param podcastName
	 * @return
	 */
	public Vector<Podcast> getPodcastListByName(String podcastName){
		Vector<Podcast> results = new Vector<Podcast>();
		
		synchronized (podcasts){
			for (Podcast currentPodcast: podcasts){
				if ((currentPodcast.getName().toLowerCase().contains(podcastName.toLowerCase()))&&
					(!currentPodcast.isRemoved())){
					results.add(currentPodcast);
				}
			}
		}
		return results;
	}

	/**
	 * @return the settingsDir
	 */
	public String getSettingsDir() {
		return settingsDir;
	}

	/**
	 * @param settingsDir the settingsDir to set
	 */
	public void setSettingsDir(String settingsDir) {
		this.settingsDir = settingsDir;
	}

	/**
	 * 
	 * @param convertCharToNumber
	 * @return
	 */
	public Podcast getActivePodcast(int selectedPodcastCount) {
		Podcast selectedPodcast=null;
		int count=0,
		    activeCount=0;
		
		while (selectedPodcast==null && count<podcasts.size()){
			if (selectedPodcastCount==activeCount &&
				!podcasts.get(count).isRemoved()){
				selectedPodcast=podcasts.get(count);
			} else if (!podcasts.get(count).isRemoved()){
					activeCount++;
			}
			count++;
		}
		
		return selectedPodcast;
	}

	/**
	 *   The following method is used to clone the array of objects, right down to it's little itty bittys
	 *   to prevent concurrent errors
	 */
	public ArrayList<Podcast> clone(){
		ArrayList<Podcast> cloneList = new ArrayList<Podcast>();
		synchronized (podcasts){
		   for (Podcast podcast : podcasts){
			   cloneList.add(new Podcast(podcast));
           }
		}
		return cloneList;
	}
}
