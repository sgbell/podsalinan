/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.list;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;

/**
 * @author sbell
 *
 */
public class ListDetails extends CLIOption {

	/**
	 * @param newData
	 */
	public ListDetails(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(String command) {
		debug=true;

		if (debug) Podsalinan.debugLog.logInfo("["+getClass().getName()+"] command: "+command);
		
		
		return returnObject;
	}

}
