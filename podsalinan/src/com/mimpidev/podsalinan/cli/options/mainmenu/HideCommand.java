/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.mainmenu;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;

/**
 * @author sbell
 *
 */
public class HideCommand extends CLIOption {

	/**
	 * @param newData
	 */
	public HideCommand(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		String command="";
		if (debug) Podsalinan.debugLog.logInfo(this, 27, "Command: "+command);
		
		if (command.equalsIgnoreCase("menu"))
			if (!data.getSettings().addSetting("menuVisible", "false"))
				data.getSettings().updateSetting("menuVisible", "false");
		return returnObject;
	}

}
