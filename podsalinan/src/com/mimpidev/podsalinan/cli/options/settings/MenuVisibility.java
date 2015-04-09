/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.settings;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;

/**
 * @author sbell
 *
 */
public class MenuVisibility extends CLIOption {

	/**
	 * @param newData
	 */
	public MenuVisibility(DataStorage newData) {
		super(newData);
	}

	public ReturnObject execute(String command) {
		//TODO: 1.6 Flesh out MenuVisibility
		return returnObject;
	}

}
