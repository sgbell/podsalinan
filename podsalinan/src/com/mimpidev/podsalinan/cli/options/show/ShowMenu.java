/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.show;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
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
		String command="";
		if (debug) Podsalinan.debugLog.logInfo(this, 27, "Command: "+command);
		
		if (command.equalsIgnoreCase("menu")){
			if (!data.getSettings().addSetting("menuVisible", "true"))
				data.getSettings().updateSetting("menuVisible", "true");
            returnObject.methodCall=CLInterface.cliGlobals.globalSelectionToString();
			returnObject.execute=true;
		}
		return returnObject;
	}

}
