/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.episode;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.data.Episode;
import com.mimpidev.podsalinan.data.Podcast;

/**
 * @author sbell
 *
 */
public abstract class BaseEpisodeOption extends CLIOption {

	private Podcast podcast=null;
	/**
	 * @param newData
	 */
	public BaseEpisodeOption(DataStorage newData) {
		super(newData);
	}

	public Episode getEpisode(String podcastUid, String episodeId){
		return getEpisode(podcastUid,convertCharToNumber(episodeId));
	}
	
	public Episode getEpisode(String podcastUid, int episodeId){
		Episode episode=null;
		setPodcast(podcastUid);
		if ((podcast!=null)&&(podcast.getEpisodes().size()>episodeId)){
			episode=podcast.getEpisodes().get(episodeId);
		}
		return episode;
	}
	
	public Podcast getPodcast(){
		return podcast;
	}
	
	public void setPodcast(String podcastUid){
		podcast = data.getPodcasts().getPodcastByUid(podcastUid);
	}

	public void setPodcast(Podcast selectedPodcast){
		podcast = selectedPodcast;
	}
	
	public abstract ReturnObject execute(String command);
}
