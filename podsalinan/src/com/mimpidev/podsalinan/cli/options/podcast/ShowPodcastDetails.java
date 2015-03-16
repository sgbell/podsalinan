/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.podcast;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;

/**
 * @author sbell
 *
 */
public class ShowPodcastDetails extends CLIOption {

	/**
	 * @param newData
	 */
	public ShowPodcastDetails(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(String command) {
		// TODO flesh out ShowPodcastDetails
		return null;
	}

}
