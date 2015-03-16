/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.downloads;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;

/**
 * @author sbell
 *
 */
public class ShowDownloadDetails extends CLIOption {

	/**
	 * @param newData
	 */
	public ShowDownloadDetails(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(String command) {
		// TODO flesh out ShowDownloadDetails
		return null;
	}

}
