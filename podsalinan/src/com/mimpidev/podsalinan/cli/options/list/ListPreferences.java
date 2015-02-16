/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.list;

import java.util.Set;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObjcet;

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
	public ReturnObjcet execute(String command) {
		debug=true;
		if (debug) Podsalinan.debugLog.logInfo("["+getClass().getName()+"] command: "+command);
		if (debug) Podsalinan.debugLog.logInfo("["+getClass().getName()+"] Size of Preferences: "+data.getSettings().getMap().size());
		Set<String> settings = data.getSettings().getMap().keySet();
		System.out.println("Settings");
		System.out.println("--------");
		for (String setting : settings){
			System.out.println(setting+":"+data.getSettings().findSetting(setting));
		}
		return returnObject;
	}
}
