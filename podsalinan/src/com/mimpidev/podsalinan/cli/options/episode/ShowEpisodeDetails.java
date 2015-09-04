/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.episode;

import java.net.MalformedURLException;
import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.data.Episode;

/**
 * @author sbell
 *
 */
public class ShowEpisodeDetails extends BaseEpisodeOption {

	/**
	 * @param newData
	 */
	public ShowEpisodeDetails(DataStorage newData) {
		super(newData);
	}

	public void printDetails(Episode episode, boolean showAll){
		if (episode!=null){
			System.out.println ("Podcast: "+getPodcast().getName());
			System.out.println ("Episode: "+episode.getTitle());
			System.out.println ("Date: "+episode.getDate());
			System.out.println ("Status: "+episode.getCurrentStatus());
			if (showAll){
				System.out.println ("File size: "+episode.getSize());
				System.out.println ("URL: "+episode.getURL());
				try {
					System.out.println ("Destination: "+episode.getFilename());
				} catch (MalformedURLException e) {
					Podsalinan.debugLog.logError(this, "Faulty URL");
					Podsalinan.debugLog.logError(this, "URL: "+episode.getURL());
					Podsalinan.debugLog.printStackTrace(e.getStackTrace());
				}
			}
		}
	}
	
	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		if (debug) Podsalinan.debugLog.logMap(functionParms);
		Episode episode=null;
		
		if (functionParms.containsKey("uid") && functionParms.containsKey("userInput")){
			episode = this.getEpisode(functionParms.get("uid"), functionParms.get("userInput"));
		} 
		if (episode!=null){
			printDetails(episode,!functionParms.containsKey("menuCalled"));
		}
		
		return returnObject;
	}
}
