/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.settings;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.cli.options.HideCommand;
import com.mimpidev.podsalinan.cli.options.show.ShowMenu;

/**
 * @author sbell
 *
 */
public class MenuVisibility extends CLIOption {

	/**
	 * @param newData
	 */
	public MenuVisibility(DataStorage newData) {
		super(newData);
		options.put("show", new ShowMenu(data));
		options.put("hide", new HideCommand(data));
	}

	public ReturnObject execute(Map<String, String> functionParms) {

		if (debug) Podsalinan.debugLog.logMap(this, functionParms);
		
		Set<String> trueList = new HashSet<String>(Arrays.asList(new String[] {"true","1","yes","y"}));
		Set<String> falseList = new HashSet<String>(Arrays.asList(new String[] {"false","0","no","n"}));
		if (trueList.contains(functionParms.get("userInput"))){
			//returnObject=options.get("show").execute("menu");
		} else if (falseList.contains(functionParms.get("userInput"))){
			//returnObject=options.get("hide").execute("menu");
		} else {
			System.out.println("Error: Invalid Command.");
		}
		returnObject.methodCall="settings";
		returnObject.parameterMap.clear();
		returnObject.execute=true;
		
		return returnObject;
	}

}
