/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.podcast;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObjcet;
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
	public ReturnObjcet execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo("["+getClass().getName()+"]");
		
		String tempDir=data.getSettingsDir();
		Podcast selectedPodcast = data.getPodcasts().getPodcastByUid(command.split(" ")[0]);
		if (selectedPodcast!=null){
			if (debug) Podsalinan.debugLog.logInfo("["+getClass().getName()+"] updating Podcast");
			selectedPodcast.updateList(tempDir, true);
		}
		returnObject = new ReturnObjcet();
		returnObject.methodCall = "podcast";
		returnObject.methodParameters = command.split(" ")[0];
		
		return returnObject;
	}

}
