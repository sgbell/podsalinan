/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.downloads;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInput;
import com.mimpidev.podsalinan.cli.ReturnCall;

/**
 * @author sbell
 *
 */
public class ChangeDestination extends CLIOption {

	/**
	 * @param newData
	 */
	public ChangeDestination(DataStorage newData) {
		super(newData);
		debug=true;
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ReturnCall execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo("["+getClass().getName()+"] command: "+command);

		if (command.split(" ").length>1){
			System.out.println("Enter Download Destination ["+data.getUrlDownloads().findDownloadByUid(command.split(" ")[0]).getDestination()+"]: ");
			String userInput = (new CLInput()).getStringInput();
			
		}
		
		return returnObject;
	}
}
