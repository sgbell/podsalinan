/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.downloads;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInterface;
import com.mimpidev.podsalinan.cli.ReturnObject;

/**
 * @author sbell
 *
 */
public class DecreasePriority extends CLIOption {

	/**
	 * @param newData
	 */
	public DecreasePriority(DataStorage newData) {
		super(newData);
		debug=true;
	}

	@Override
	public ReturnObject execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo(this,"command: "+command);

		boolean decreased=false;
		if (command.split(" ").length>1){
			decreased=data.getUrlDownloads().decreasePriority(command.split(" ")[0]);
		} else {
			decreased=data.getUrlDownloads().decreasePriority(command);
		}
		if (decreased){
			   System.out.println("Decreased Priority: "+data.getUrlDownloads().findDownloadByUid(command));
		} else {
			System.out.println("Error: Download already at the bottom of the list.");
		}
		returnObject = CLInterface.cliGlobals.createReturnObject();
		returnObject.execute=true;
		
		return returnObject;
	}

}
