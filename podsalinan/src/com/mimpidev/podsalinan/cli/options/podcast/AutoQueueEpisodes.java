/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.podcast;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
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

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		debug=true;
		if (debug) Podsalinan.debugLog.logInfo(this,"Called");
		returnObject.debug(debug);
		
		if (functionParms.containsKey("podcastId")){
			Podcast selectedPodcast = data.getPodcasts().getPodcastByUid(functionParms.get("podcastId"));
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
				returnObject.execute=false;
			}
		}
		
		return returnObject;
	}
}
