/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInterface;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.cli.options.podcast.*;
import com.mimpidev.podsalinan.data.Podcast;

/**
 * @author sbell
 *
 */
public class PodcastCommand extends CLIOption {

	/**
	 * @param newData
	 */
	public PodcastCommand(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		debug=true;
		returnObject.methodCall="podcast";
		returnObject.parameterMap.clear();
		returnObject.execute=false;

		if (functionParms.containsKey("userInput")){
			String command=functionParms.get("userInput");
			
			if (convertCharToNumber(command)<data.getPodcasts().getList().size()){
				Podcast currentPodcast = data.getPodcasts().getList().get(convertCharToNumber(command));
				returnObject.parameterMap.put("podcastId",currentPodcast.getDatafile());
				CLInterface.cliGlobals.getGlobalSelection().put("podcast", returnObject.parameterMap.get("podcastId"));
				returnObject.methodCall+=" <aaaaaaaa>";
				if (debug) Podsalinan.debugLog.logInfo("Found podcast: "+command);
			} else {
				System.out.println("Error: Invalid Podcast number");
				
			}
			returnObject.execute=true;
		}
		
		if (debug) Podsalinan.debugLog.logInfo(this, 111, "Leaving PodcastCommand.");
		returnObject.debug(debug);
		return returnObject;
	}
}
