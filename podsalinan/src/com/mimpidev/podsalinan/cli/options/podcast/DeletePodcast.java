/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.podcast;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnCall;

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
		debug=true;
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ReturnCall execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo("["+getClass().getName()+"]+command");
    	/*
		if(input.confirmRemoval()){
    		selectedPodcast.setRemove(true);
    		setSelectedPodcast(null);
    		menuList.removeSetting("selectedPodcast");
    	}*/
		
		return returnObject;
	}

}
