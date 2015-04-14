/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.settings;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInput;
import com.mimpidev.podsalinan.cli.ReturnObject;

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
	
	public String executeMenuOption(){
		System.out.println ();
		System.out.print ("Enter Number of Simultaneous Downloads["+data.getSettings().findSetting("maxDownloaders")+"]: ");
		/* Take user input.
		 * Make sure it is between 1 and 30
		 * If not, get the user to enter it again.
		 */
		return input.getValidNumber(1,30);
	}
	
	public ReturnObject execute(String command) {
		debug=true;
		if (debug) Podsalinan.debugLog.logInfo(this,"Command: "+command);
		String numDownloaders="";

		String[] commandOptions = command.split(" ");
        if (commandOptions.length==1){
        	numDownloaders=executeMenuOption();
        	returnObject.methodCall="settings";
        	returnObject.execute=true;
        } else {
        	numDownloaders=commandOptions[1];
        }		
		
		if (numDownloaders!=null)
			if(!data.getSettings().updateSetting("maxDownloaders",numDownloaders))
				data.getSettings().addSetting("maxDownloaders",numDownloaders);
		System.out.println("Simultaneous Downloads: "+data.getSettings().findSetting("maxDownloaders"));		
		
		return returnObject;
	}

}
