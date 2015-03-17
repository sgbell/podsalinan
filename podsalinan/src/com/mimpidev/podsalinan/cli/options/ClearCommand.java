/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInterface;
import com.mimpidev.podsalinan.cli.ReturnObject;

/**
 * @author sbell
 *
 */
public class ClearCommand extends CLIOption {

	/**
	 * @param newData
	 */
	public ClearCommand(DataStorage newData) {
		super(newData);
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ReturnObject execute(String command) {
		CLInterface.cliGlobals.getGlobalSelection().clear();
		System.out.println("Selection Cleared.");
		return returnObject;
	}

}
