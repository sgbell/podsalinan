/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnCall;

/**
 * @author sbell
 *
 */
public class SelectEpisode extends CLIOption {

	public SelectEpisode(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnCall execute(String command) {
		// TODO Flesh out Select Episode option
		return returnObject;
	}

}
