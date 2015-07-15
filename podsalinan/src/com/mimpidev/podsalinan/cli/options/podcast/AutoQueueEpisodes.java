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
		debug=true;
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		String command="";
		if (debug) Podsalinan.debugLog.logInfo("["+getClass().getName()+"] command: "+command);
		
		if (command.split(" ").length>1){
			Podcast selectedPodcast = data.getPodcasts().getPodcastByUid(command.split(" ")[0]);
			if (selectedPodcast!=null){
				System.out.println ();
		    	if (selectedPodcast.isAutomaticQueue()){
		    		selectedPodcast.setAutomaticQueue(false);
		    		System.out.println(selectedPodcast.getName()+" podcast autoqueue disabled.");
		    	}else{
		    		selectedPodcast.setAutomaticQueue(true);
		    		System.out.println(selectedPodcast.getName()+"podcast autoqeue enabled.");
		    	}
				returnObject.methodCall = "podcast";
				//returnObject.methodParameters = command.split(" ")[0];
			}
		}
		
		return returnObject;
	}

}
