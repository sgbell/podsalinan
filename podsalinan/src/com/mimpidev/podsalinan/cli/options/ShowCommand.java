/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import java.util.Map;

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
	/*	options.put("menu", new ShowMenu(newData));
		options.put("details", new ListDetails(newData));*/
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		String command="";
		if (debug) Podsalinan.debugLog.logInfo(this, 31, "Command: "+command);
		String[] commandOptions = command.split(" ");
		/*if (options.containsKey(commandOptions[0])){
			//returnObject=options.get(commandOptions[0]).execute(command);
		} else {
			System.out.println("Error: Invalid user Input.");
		}*/

		return returnObject;
	}

}
