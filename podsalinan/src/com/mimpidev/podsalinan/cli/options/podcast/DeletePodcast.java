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
import com.mimpidev.podsalinan.data.Podcast;

/**
 * @author sbell
 *
 */
public class DeletePodcast extends CLIOption {

	/**
	 * @param newData
	 */
	public DeletePodcast(DataStorage newData) {
		super(newData);
	}


	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		if (debug) if (Log.isDebug())Log.logMap(this, functionParms);
		if (debug) if (Log.isDebug())Log.logInfo(this, "Global Selection");
		if (debug) if (Log.isDebug())Log.logMap(this, CLInterface.cliGlobals.getGlobalSelection());

		Podcast selectedPodcast = null;
		
		if (functionParms.containsKey("uid")){
			selectedPodcast = data.getPodcasts().getPodcastByUid(functionParms.get("uid"));
		}
		if (selectedPodcast==null && CLInterface.cliGlobals.getGlobalSelection().containsKey("podcastid")){
			selectedPodcast = data.getPodcasts().getPodcastByUid(CLInterface.cliGlobals.getGlobalSelection().get("podcastid"));
		}
		if (selectedPodcast!=null){
			CLInput input = new CLInput();
			if(input.confirmRemoval()){
				System.out.println("Podcast Removed.");
	    		selectedPodcast.setRemoved(true);
				if ((CLInterface.cliGlobals.getGlobalSelection().containsKey("podcastid"))&&
					(CLInterface.cliGlobals.getGlobalSelection().get("podcastid").equalsIgnoreCase(functionParms.get("uid")))){
					CLInterface.cliGlobals.getGlobalSelection().clear();
				}
	    		returnObject.methodCall = "podcast showmenu";
	    	} else {
	    		returnObject.methodCall = "podcast "+selectedPodcast.getDatafile();
	    	}
		} else {
			System.out.println("Podcast does not exist");
    		returnObject.methodCall = "podcast showmenu";
		}
		returnObject.parameterMap.clear();
		returnObject.execute=true;
		
		return returnObject;
	}

}
