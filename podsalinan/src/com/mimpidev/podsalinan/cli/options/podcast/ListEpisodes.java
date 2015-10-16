/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.podcast;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.dev.debug.Log;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInput;
import com.mimpidev.podsalinan.cli.CLInterface;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.data.Episode;
import com.mimpidev.podsalinan.data.Podcast;

/**
 * @author sbell
 *
 */
public class ListEpisodes extends CLIOption {

	/**
	 * @param newData
	 */
	public ListEpisodes(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(final Map<String, String> functionParms) {
		
		CLInput input = new CLInput();
		
		int epCount=1;
		if (debug) if (Log.isDebug())Log.logMap(this, functionParms);
        if (functionParms.containsKey("uid") || CLInterface.cliGlobals.getGlobalSelection().containsKey("podcastid")){
    		Podcast selectedPodcast = data.getPodcasts().getPodcastByUid(functionParms.get("uid"));
    		if (selectedPodcast==null && CLInterface.cliGlobals.getGlobalSelection().containsKey("podcastid")){
    			selectedPodcast = data.getPodcasts().getPodcastByUid(CLInterface.cliGlobals.getGlobalSelection().get("podcastid"));
    		}
    		if (selectedPodcast!=null){
    			System.out.println ();
    			synchronized (selectedPodcast.getEpisodes()){
    				for (Episode episode : selectedPodcast.getEpisodes()){
    					System.out.println (getEncodingFromNumber(epCount)+" - " +
    							episode.getTitle()+" : "+episode.getDate());
    					epCount++;
    					if ((epCount%20)==0){
    						System.out.println("-- Press any key to continue, q to quit --");
    						char charInput=input.getSingleCharInput();
    						if (charInput=='q')
    							break;
    					}
    				}
    			}
    			returnObject.methodCall = "podcast "+selectedPodcast.getDatafile();
    		}
   		} else {
				System.out.println("Error: No podcast has been selected");
				returnObject.methodCall += " showmenu";
				returnObject.parameterMap.clear();
		}
		returnObject.execute=true;
		
		return returnObject;
	}

}
