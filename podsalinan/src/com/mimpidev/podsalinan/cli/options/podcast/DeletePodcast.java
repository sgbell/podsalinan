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
import com.mimpidev.podsalinan.cli.options.downloads.ShowDownloadDetails;
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
		debug=true;
		if (debug) Podsalinan.debugLog.logInfo(this, "command: "+command);
		
		String podcastUid = "";
		String[] commandOptions = command.split(" ");
		
        if (data.getPodcasts().getPodcastByUid(commandOptions[0])!=null){
        	podcastUid = commandOptions[0];
        }

		if (commandOptions.length==1){
			ShowPodcastDetails printDetails = new ShowPodcastDetails(data);
			printDetails.execute(podcastUid);
		}
		
		if (podcastUid.length()>0){
			Podcast selectedPodcast = data.getPodcasts().getPodcastByUid(podcastUid);
			if (selectedPodcast!=null){
				CLInput input = new CLInput();
				if(input.confirmRemoval()){
		    		selectedPodcast.setRemoved(true);
					if ((CLInterface.cliGlobals.getGlobalSelection().containsKey("podcast"))&&
						(CLInterface.cliGlobals.getGlobalSelection().get("podcast").equalsIgnoreCase(podcastUid))){
						CLInterface.cliGlobals.getGlobalSelection().clear();
					} else {
					    String[] selection = CLInterface.cliGlobals.globalSelectionToString().split(" ", 2);
					    returnObject.methodCall = selection[0];
					    returnObject.methodParameters= (selection.length>1?selection[1]:"");
					}
		    	}
				if (commandOptions.length>1){
		    		returnObject.methodCall = "podcast";
		    		returnObject.methodParameters = "";
		    	}
	    		returnObject.execute=true;
			} else {
				System.out.println("Podcast does not exist");
			}
		}
		
		return returnObject;
	}

}
