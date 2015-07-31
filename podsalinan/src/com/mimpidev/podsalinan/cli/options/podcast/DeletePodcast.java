/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.podcast;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
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
		debug=true;
		

/*     if called from the command line, print the podcast details first before
 *     asking the user to delete the podcast
 * 		if (commandOptions.length==1){
			ShowPodcastDetails printDetails = new ShowPodcastDetails(data);
			//printDetails.execute(podcastUid);
		}*/
		
		if (functionParms.containsKey("podcastId")){
			Podcast selectedPodcast = data.getPodcasts().getPodcastByUid(functionParms.get("podcastId"));
			if (selectedPodcast!=null){
				CLInput input = new CLInput();
				if(input.confirmRemoval()){
		    		selectedPodcast.setRemoved(true);
					if ((CLInterface.cliGlobals.getGlobalSelection().containsKey("podcastId"))&&
						(CLInterface.cliGlobals.getGlobalSelection().get("podcastId").equalsIgnoreCase(functionParms.get("podcastId")))){
						CLInterface.cliGlobals.getGlobalSelection().clear();
					}
		    		returnObject.methodCall = "podcast showmenu";
		    	} else {
		    		returnObject.methodCall = "podcast <podcastId>";
		    	}
	    		returnObject.parameterMap.clear();
	    		returnObject.execute=true;
			} else {
				System.out.println("Podcast does not exist");
	    		returnObject.methodCall = "podcast";
	    		returnObject.parameterMap.clear();
	    		returnObject.execute=true;
			}
		}
		
		return returnObject;
	}

}
