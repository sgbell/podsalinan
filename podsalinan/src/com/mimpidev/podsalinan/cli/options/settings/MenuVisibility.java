/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.settings;

import java.util.Arrays;
import java.util.HashSet;
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

	public ReturnObject execute(String command) {
		debug=true;
		if (debug) Podsalinan.debugLog.logInfo(this, "Command:"+ command);
		String[] commandOptions = command.split(" ");
		
		Set<String> trueList = new HashSet<String>(Arrays.asList(new String[] {"true","1","yes","y"}));
		Set<String> falseList = new HashSet<String>(Arrays.asList(new String[] {"false","0","no","n"}));
		if (trueList.contains(commandOptions[1].toLowerCase())){
			returnObject=options.get("show").execute("menu");
		} else if (falseList.contains(commandOptions[1].toLowerCase())){
			returnObject=options.get("hide").execute("menu");
		} else {
			System.out.println("Error: Invalid Command.");
		}
		returnObject.methodCall="settings";
		returnObject.methodParameters="";
		returnObject.execute=true;
		
		return returnObject;
	}

}
