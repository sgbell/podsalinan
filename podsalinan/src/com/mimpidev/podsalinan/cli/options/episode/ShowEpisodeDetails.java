/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.episode;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.ReturnObject;

/**
 * @author sbell
 *
 */
public class ShowEpisodeDetails extends BaseEpisodeOption {

	/**
	 * @param newData
	 */
	public ShowEpisodeDetails(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(String command) {
		debug=true;
		if (debug) Podsalinan.debugLog.logInfo(this,"command: "+command);
		// TODO flesh out ShowEpisodeDetails
		return returnObject;
	}

}
