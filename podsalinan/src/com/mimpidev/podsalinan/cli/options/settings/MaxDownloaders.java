/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.settings;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInput;
import com.mimpidev.podsalinan.cli.ReturnCall;

/**
 * @author sbell
 *
 */
public class MaxDownloaders extends CLIOption {

	private CLInput input = new CLInput();
	/**
	 * @param newData
	 */
	public MaxDownloaders(DataStorage newData) {
		super(newData);
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ReturnCall execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo("["+getClass().getName()+"] command: "+command);

		System.out.println ();
		System.out.print ("Enter Number of Simultaneous Downloads["+data.getSettings().findSetting("maxDownloaders")+"]: ");
		/* Take user input.
		 * Make sure it is between 1 and 30
		 * If not, get the user to enter it again.
		 */
		String numDownloaders = input.getValidNumber(1,30);
		if (debug) Podsalinan.debugLog.logInfo("Number of downloaders:"+numDownloaders);
		if (numDownloaders!=null)
			if(!data.getSettings().updateSetting("maxDownloaders",numDownloaders))
				data.getSettings().addSetting("maxDownloaders",numDownloaders);
		System.out.println("Simultaneous Downloads: "+data.getSettings().findSetting("maxDownloaders"));		
		
		return returnObject;
	}

}
