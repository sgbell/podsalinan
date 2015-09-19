/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.podcast;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.dev.debug.Log;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.data.Podcast;

/**
 * @author sbell
 *
 */
public class AutoQueueEpisodes extends CLIOption {

	/**
	 * @param newData
	 */
	public AutoQueueEpisodes(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		if (debug) if (Log.isDebug())Log.logInfo(this,"Called");
		returnObject.debug(debug);
		
		if (functionParms.containsKey("uid")){
			Podcast selectedPodcast = data.getPodcasts().getPodcastByUid(functionParms.get("uid"));
			if (selectedPodcast!=null){
				System.out.println ();
		    	if (selectedPodcast.isAutomaticQueue()){
		    		selectedPodcast.setAutomaticQueue(false);
		    		System.out.println(selectedPodcast.getName()+" podcast autoqueue disabled.");
		    	}else{
		    		selectedPodcast.setAutomaticQueue(true);
		    		System.out.println(selectedPodcast.getName()+"podcast autoqeue enabled.");
		    	}
				returnObject.methodCall = "podcast <podcastid>";
				returnObject.parameterMap.clear();
				returnObject.execute=true;
			}
		}
		
		return returnObject;
	}
}
