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
		
		if (functionParms.containsKey("uid") || CLInterface.cliGlobals.getGlobalSelection().containsKey("podcastid")){
			Podcast podcast=null;
			if (functionParms.containsKey("uid")){
				podcast=data.getPodcasts().getPodcastByUid(functionParms.get("uid"));
			} else {
			   podcast=data.getPodcasts().getPodcastByUid(CLInterface.cliGlobals.getGlobalSelection().get("podcastid"));
			}
			if (functionParms.containsKey("command") &&
				functionParms.get("command").equalsIgnoreCase("selectedMenu") && 
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
