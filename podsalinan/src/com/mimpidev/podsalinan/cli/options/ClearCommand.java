/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObjcet;

/**
 * @author sbell
 *
 */
public class ClearCommand extends CLIOption {

	/**
	 * @param newData
	 * @param returnObject 
	 */
	public ClearCommand(DataStorage newData, ReturnObjcet returnObject) {
		super(newData,returnObject);
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ReturnObjcet execute(String command) {
		System.out.println("Selection Cleared.");
		return null;
	}

}
