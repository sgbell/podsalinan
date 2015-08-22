/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.podcast;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInterface;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.data.Podcast;

/**
 * @author sbell
 *
 */
public class UpdatePodcast extends CLIOption {

	/**
	 * @param newData
	 */
	public UpdatePodcast(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		if (debug) Podsalinan.debugLog.logMap(this, functionParms);
		
		String tempDir=data.getSettingsDir();
		if (debug) Podsalinan.debugLog.logInfo(this, "tempDir:"+tempDir);
		Podcast selectedPodcast = null;
		
        if (functionParms.containsKey("uid"))
        	selectedPodcast = data.getPodcasts().getPodcastByUid(functionParms.get("uid"));
        if (selectedPodcast==null && CLInterface.cliGlobals.getGlobalSelection().containsKey("podcastid")){
        	selectedPodcast = data.getPodcasts().getPodcastByUid(CLInterface.cliGlobals.getGlobalSelection().get("podcastid"));
        }
		if (selectedPodcast!=null){
			System.out.println("Updating Podcast.");
			selectedPodcast.updateList(tempDir, true);
			returnObject.methodCall = "podcast "+selectedPodcast.getDatafile();
		} else {
			System.out.println("Error: Invalid podcast passed to command");
			returnObject.methodCall = "podcast showmenu";
		}
		returnObject.execute=true;
		
		return returnObject;
	}

}
