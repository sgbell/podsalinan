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
public class UpdatePodcast extends CLIOption {

	/**
	 * @param newData
	 */
	public UpdatePodcast(DataStorage newData) {
		super(newData);
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		String command="";
		debug=true;
		if (debug) Podsalinan.debugLog.logInfo("["+getClass().getName()+"]");
		
		String tempDir=data.getSettingsDir();
		if (debug) Podsalinan.debugLog.logInfo(this, "tempDir:"+tempDir);
		
		Podcast selectedPodcast = data.getPodcasts().getPodcastByUid(command.split(" ")[0]);
		if (selectedPodcast!=null){
			if (debug) Podsalinan.debugLog.logInfo(this, "Updating Podcast");
			selectedPodcast.updateList(tempDir, true);
		}
		returnObject = new ReturnObject();
		returnObject.methodCall = "podcast";
		//returnObject.methodParameters = command.split(" ")[0];
		returnObject.execute=true;
		
		return returnObject;
	}

}
