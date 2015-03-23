/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.podcast;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
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
	public ReturnObject execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo("["+getClass().getName()+"] command: "+command);
		
		if (command.split(" ").length>1){
			Podcast selectedPodcast = data.getPodcasts().getPodcastByUid(command.split(" ")[0]);
			if (selectedPodcast!=null){
				CLInput input = new CLInput();
				if(input.confirmRemoval()){
		    		selectedPodcast.setRemoved(true);
					if ((CLInterface.cliGlobals.getGlobalSelection().containsKey("podcast"))&&
						(CLInterface.cliGlobals.getGlobalSelection().get("podcast").equalsIgnoreCase(command.split(" ")[0]))){
						CLInterface.cliGlobals.getGlobalSelection().clear();
					}
					returnObject.methodCall = "podcast";
					returnObject.methodParameters = "";
					returnObject.execute=true;
		    	} else {
		    		returnObject.methodCall = "podcast";
		    		returnObject.methodParameters = command.split(" ")[0];
		    		returnObject.execute=true;
		    	}
			}
		}
		
		return returnObject;
	}

}
