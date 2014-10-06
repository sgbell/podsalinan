/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.settings;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnCall;

/**
 * @author sbell
 *
 */
public class ShowMenu extends CLIOption {

	/**
	 * @param newData
	 */
	public ShowMenu(DataStorage newData) {
		super(newData);
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ReturnCall execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo("["+getClass().getName()+"] command: "+command);

		System.out.println();
		System.out.println("1. Change Podcast Update Rate");
		System.out.println("2. Number of Downloaders");
		System.out.println("3. Default Download Directory");
		System.out.println("4. Automatically Download New Podcast Episodes");
		System.out.println("5. Set Download Speed Limit");
		System.out.println();
		System.out.println("9. Return to Preferences Menu");
		
		return returnObject;
	}

}