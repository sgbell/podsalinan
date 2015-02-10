/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.help;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnCall;

/**
 * @author sbell
 *
 */
public class HelpSet extends CLIOption {

	/**
	 * @param newData
	 */
	public HelpSet(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnCall execute(String command) {
		// If the user enters "help set"
		System.out.println("set is used to change settings");
		System.out.println("");
		System.out.println("   set <preference name> <value>  this will update the preference in the system");
		return null;
	}

}
