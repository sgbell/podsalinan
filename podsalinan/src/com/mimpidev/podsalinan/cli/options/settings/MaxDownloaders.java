/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.settings;

import java.util.Map;

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
	
	public ReturnObject execute(Map<String, String> functionParms) {
		if (debug) Podsalinan.debugLog.logMap(this, functionParms);
		String numDownloaders="";

		if (functionParms.containsKey("userInput")){
			numDownloaders=functionParms.get("userInput");
			returnObject.methodCall="";
			returnObject.execute=false;
		} else {
        	numDownloaders=executeMenuOption();
        	returnObject.methodCall="settings";
        	returnObject.execute=true;
        }		
		
		if (numDownloaders!=null)
			if(!data.getSettings().updateSetting("maxDownloaders",numDownloaders))
				data.getSettings().addSetting("maxDownloaders",numDownloaders);
		System.out.println("Simultaneous Downloads: "+data.getSettings().findSetting("maxDownloaders"));
        // This is used to wake up the downloadQueue to change the number of downloaders running.
		synchronized (Podsalinan.downloadQueueSyncObject){
			Podsalinan.downloadQueueSyncObject.notify();
		}
		
		returnObject.parameterMap.clear();
		
		return returnObject;
	}

}
