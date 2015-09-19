/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.list;

import java.util.Map;
import java.util.Set;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.dev.debug.Log;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;

/**
 * @author sbell
 *
 */
public class ListPreferences extends CLIOption {

	/**
	 * @param newData
	 */
	public ListPreferences(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		if (debug) if (Log.isDebug())Log.logMap(this, functionParms);
		if (debug) if (Log.isDebug())Log.logInfo(this, "Size of Preferences: "+data.getSettings().getMap().size());
		Set<String> settings = data.getSettings().getMap().keySet();
		System.out.println("Settings");
		System.out.println("--------");
		for (String setting : settings){
			System.out.println(setting+":"+data.getSettings().findSetting(setting));
		}
		return returnObject;
	}
}
