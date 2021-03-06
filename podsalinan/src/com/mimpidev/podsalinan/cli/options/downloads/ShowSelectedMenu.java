/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.downloads;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.dev.debug.Log;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;

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
	}
	
	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		if (debug) if (Log.isDebug())Log.logMap(this, functionParms);

		if (functionParms.containsKey("uid")){
			if (!data.getSettings().isValidSetting("menuVisible") || data.getSettings().getSettingValue("menuVisible").equalsIgnoreCase("true")){
				ShowDownloadDetails printDetails = new ShowDownloadDetails(data);
				functionParms.put("menuCall", "true");
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
			}

			returnObject.methodCall="downloads "+functionParms.get("uid");
		} else {
			System.out.println("Error: Invalid input.");
			returnObject.methodCall="downloads showmenu";
		}
		returnObject.parameterMap.clear();
        returnObject.execute=false;
		
		return returnObject;
	}

}
