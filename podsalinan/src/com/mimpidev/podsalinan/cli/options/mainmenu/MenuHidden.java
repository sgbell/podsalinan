/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.mainmenu;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.dev.debug.Log;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;

/**
 * @author sbell
 *
 */
public class MenuHidden extends CLIOption {

	/**
	 * @param newData
	 */
	public MenuHidden(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		if (debug) if (Log.isDebug())Log.logMap(this, functionParms);
		
		if (!data.getSettings().addSetting("menuVisible", "false"))
			data.getSettings().updateSetting("menuVisible", "false");

		return returnObject;
	}

}
