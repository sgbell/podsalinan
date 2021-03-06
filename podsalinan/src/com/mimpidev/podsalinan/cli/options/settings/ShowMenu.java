/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.settings;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.dev.debug.Log;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInterface;
import com.mimpidev.podsalinan.cli.ReturnObject;

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
	public ReturnObject execute(Map<String, String> functionParms) {
		if (debug) if (Log.isDebug())Log.logMap(this,functionParms);

		if (!data.getSettings().isValidSetting("menuVisible") || data.getSettings().getSettingValue("menuVisible").equalsIgnoreCase("true")){
			System.out.println();
			System.out.println("1. Change Podcast Update Rate");
			System.out.println("2. Number of Downloaders");
			System.out.println("3. Default Download Directory");
			System.out.println("4. Automatically Download New Podcast Episodes");
			System.out.println("5. Set Download Speed Limit");
			System.out.println("6. Limit download to time of day");
			System.out.println("7. Episode Status Update on Start");
			System.out.println();
			System.out.println("9. Return to Preferences Menu");
		}

		if (!CLInterface.cliGlobals.getGlobalSelection().isEmpty()){
			CLInterface.cliGlobals.getGlobalSelection().put("settings", "");
		}
		returnObject.methodCall="settings";
		returnObject.execute=false;
		
		return returnObject;
	}

}
