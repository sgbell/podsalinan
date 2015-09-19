/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.episode;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.dev.debug.Log;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInterface;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.data.Podcast;

/**
 * @author sbell
 *
 */
public class SelectEpisode extends CLIOption {

	public SelectEpisode(DataStorage newData) {
		super(newData);

	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		Podcast selectedPodcast=null;
		
		if (debug) if (Log.isDebug())Log.logMap(this,functionParms);
		if (debug) if (Log.isDebug())Log.logMap(this, CLInterface.cliGlobals.getGlobalSelection());
		if (!functionParms.containsKey("uid") && 
			functionParms.containsKey("userInput") &&
			CLInterface.cliGlobals.getGlobalSelection().containsKey("podcastid")){
			functionParms.put("uid", CLInterface.cliGlobals.getGlobalSelection().get("podcastid"));
		}
		if (debug) if (Log.isDebug())Log.logMap(this,functionParms);
		if (functionParms.containsKey("uid") && functionParms.containsKey("userInput")){
			selectedPodcast=data.getPodcasts().getPodcastByUid(functionParms.get("uid"));
			
			if (selectedPodcast!=null){
				if (convertCharToNumber(functionParms.get("userInput"))<selectedPodcast.getEpisodes().size()){
					if ((CLInterface.cliGlobals.getGlobalSelection().containsKey("podcastid") &&
						!CLInterface.cliGlobals.getGlobalSelection().get("podcastid").equalsIgnoreCase(functionParms.get("uid")))||
						(!CLInterface.cliGlobals.getGlobalSelection().containsKey("podcastid"))){
						CLInterface.cliGlobals.getGlobalSelection().clear();
						CLInterface.cliGlobals.getGlobalSelection().put("podcastid", functionParms.get("uid"));
					}
					if ((CLInterface.cliGlobals.getGlobalSelection().containsKey("episode")&&
						!CLInterface.cliGlobals.getGlobalSelection().get("episode").equalsIgnoreCase(functionParms.get("userInput")))||
						(!CLInterface.cliGlobals.getGlobalSelection().containsKey("episode"))){
						CLInterface.cliGlobals.getGlobalSelection().put("episode", functionParms.get("userInput"));
					}
					returnObject.methodCall="podcast "+selectedPodcast.getDatafile()+" episode "+functionParms.get("userInput").toLowerCase()+" showmenu";
				} else {
					System.out.println("Error: Invalid Episode selected");
					returnObject.methodCall="podcast "+selectedPodcast.getDatafile();
				}
			} else {
				System.out.println("Error: Invalid Podcast");
				returnObject.methodCall="podcast showmenu";
			}
		}
		returnObject.parameterMap.clear();
		returnObject.execute=true;
		
		return returnObject;
	}
}
