/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.cli.options.list.ListDetails;
import com.mimpidev.podsalinan.cli.options.show.ShowMenu;

/**
 * @author sbell
 *
 */
public class ShowCommand extends CLIOption {

	/**
	 * @param newData
	 */
	public ShowCommand(DataStorage newData) {
		super(newData);
		options.put("menu", new ShowMenu(newData));
		options.put("details", new ListDetails(newData));
	}

	@Override
	public ReturnObject execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo(this, 31, "Command: "+command);
		//TODO: 1. Finish improving this code
		//TODO: 2. Check in ShowMenu Class
		String[] commandOptions = command.split(" ");
		if (options.containsKey(commandOptions[0])){
			returnObject=options.get(commandOptions[0]).execute(command);
		}
		
		/*
		if (menuInput.equalsIgnoreCase("show menu")){
			if (!data.getSettings().addSetting("menuVisible", "true"))
				data.getSettings().updateSetting("menuVisible", "true");
			mainMenu.process(99);
		} else if (menuInput.toLowerCase().startsWith("show")){
			menuInput= menuInput.replaceFirst(menuInput.split(" ")[0]+" ","");
			if (menuInput.toLowerCase().equalsIgnoreCase("details")){
				mainMenu.process(98);
			} else {
				System.out.println("Error: Invalid User Input");
			}
		}*/
		return returnObject;
	}

}
