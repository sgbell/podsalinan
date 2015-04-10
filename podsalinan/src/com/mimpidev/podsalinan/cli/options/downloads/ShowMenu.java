/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.downloads;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.cli.options.list.ListDownloads;

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

	@Override
	public ReturnObject execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo("["+getClass().getName()+"] command: "+command);
		
		ListDownloads listDownloads = new ListDownloads(data);
		
		listDownloads.execute(command);
		System.out.println();
		System.out.println("(A-ZZ) Enter Download letter to select Download.");
		System.out.println("To add a new download to the queue just enter the the url to be downloaded.");
		System.out.println();
		System.out.println("9. Return to Main Menu");
		returnObject.methodCall="downloads";
		
		return returnObject;
	}

}
