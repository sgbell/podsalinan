/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.mainmenu;

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
public class MenuVisible extends CLIOption {

	/**
	 * @param newData
	 */
	public MenuVisible(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		if (debug) Podsalinan.debugLog.logMap(this, functionParms);
		
		if (!data.getSettings().addSetting("menuVisible", "true"))
			data.getSettings().updateSetting("menuVisible", "true");
        returnObject.methodCall=CLInterface.cliGlobals.globalSelectionToString();
        returnObject.execute=true;

        return returnObject;
	}

}
