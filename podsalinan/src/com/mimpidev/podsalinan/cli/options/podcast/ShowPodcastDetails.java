/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.podcast;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInterface;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.data.Podcast;

/**
 * @author sbell
 *
 */
public class ShowPodcastDetails extends CLIOption {

	/**
	 * @param newData
	 */
	public ShowPodcastDetails(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		debug=true;
		
		if (CLInterface.cliGlobals.getGlobalSelection().containsKey("podcastId")){
			Podcast podcast=data.getPodcasts().getPodcastByUid(CLInterface.cliGlobals.getGlobalSelection().get("podcastId"));
			if (functionParms.get("command").equalsIgnoreCase("selectedMenu") && 
				(data.getSettings().findSetting("menuVisible")==null||
				 data.getSettings().findSetting("menuVisible").equalsIgnoreCase("true"))){
				System.out.println("Podcast: "+podcast.getName()+ " - Selected");
			} else {
			    System.out.println("Podcast: "+podcast.getName());
			    System.out.println("Episode Count: "+podcast.getEpisodes().size());
			    System.out.println("URL: "+podcast.getURL());
			}
		} else {
			System.out.println("Error: No Podcast selected");
		}
		return returnObject;
	}

}
