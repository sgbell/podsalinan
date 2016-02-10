/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.podcast;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.dev.debug.Log;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInterface;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.cli.options.list.ListPodcasts;

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
		if (debug) if (Log.isDebug())Log.logMap(this, functionParms);
		System.out.println();
		
		if (!data.getSettings().isValidSetting("menuVisible") || data.getSettings().getSettingValue("menuVisible").equalsIgnoreCase("true")){
			ListPodcasts list = new ListPodcasts(data);
			returnObject = new ReturnObject();
			returnObject.parameterMap.put("showcount", "");
			list.execute(returnObject.parameterMap);

			System.out.println();
			System.out.println("(A-Z) Enter Podcast letter to select Podcast.");
			System.out.println();
			System.out.println("9. Return to Main Menu");
		}
        returnObject.methodCall="podcast";
        returnObject.parameterMap.clear();
		returnObject.execute=false;
		CLInterface.cliGlobals.getGlobalSelection().clear();
		return returnObject;
	}

}
