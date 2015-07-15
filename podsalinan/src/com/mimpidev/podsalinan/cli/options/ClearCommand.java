/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import java.util.Map;

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

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		returnObject = new ReturnObject();
		CLInterface.cliGlobals.getGlobalSelection().clear();
		System.out.println("Selection Cleared.");

		return returnObject;
	}

}
