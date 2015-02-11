/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ObjectCall;

/**
 * @author sbell
 *
 */
public class ClearCommand extends CLIOption {

	/**
	 * @param newData
	 * @param returnObject 
	 */
	public ClearCommand(DataStorage newData, ObjectCall returnObject) {
		super(newData,returnObject);
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ObjectCall execute(String command) {
		System.out.println("Selection Cleared.");
		return null;
	}

}
