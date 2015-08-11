/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.downloads;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.data.URLDownload;

/**
 * @author sbell
 *
 */
public class ShowSelectedMenu extends CLIOption {

	/**
	 * @param newData
	 */
	public ShowSelectedMenu(DataStorage newData) {
		super(newData);
		debug=true;
	}
	
	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		if (debug) Podsalinan.debugLog.logMap(this, functionParms);

		if (functionParms.containsKey("userInput")){
			String userInput=functionParms.get("userInput");
			String selectedDownload=null;
			if (data.getUrlDownloads().getNumberOfQueuedDownloads()>convertCharToNumber(userInput))
				selectedDownload=data.getUrlDownloads().getDownloadUid(convertCharToNumber(userInput));
			if (selectedDownload==null){
				System.out.println("Error: Download does not exist.");
				returnObject.methodCall="downloads showmenu";
			} else {
				if (debug) Podsalinan.debugLog.logInfo(this, 41, "Set selected Download:"+selectedDownload);
				
			}
			returnObject.parameterMap.clear();
			returnObject.execute=true;
		}
		ShowDownloadDetails printDetails = new ShowDownloadDetails(data);
		printDetails.execute(functionParms);

		System.out.println("1. Delete Download");
		System.out.println("2. Restart Download");
		System.out.println("3. Stop Download");
		System.out.println("4. Start Download (Add to active Queue)");
		System.out.println("5. Increase Priority");
		System.out.println("6. Decrease Priority");
		System.out.println("7. Change Destination");
		System.out.println();
		System.out.println("9. Return to Download List");
					
		returnObject.methodCall="downloads";
		//returnObject.methodParameters=""+downloadUid;
        returnObject.execute=false;
		
		return returnObject;
	}

}
