/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ObjectCall;

/**
 * @author sbell
 *
 */
public class HideCommand extends CLIOption {

	/**
	 * @param newData
	 * @param returnObject 
	 */
	public HideCommand(DataStorage newData, ObjectCall returnObject) {
		super(newData,returnObject);
	}

	@Override
	public ObjectCall execute(String command) {
		if (command.equalsIgnoreCase("hide menu"))
			if (!data.getSettings().addSetting("menuVisible", "false"))
				data.getSettings().updateSetting("menuVisible", "false");
		return null;
	}

}
