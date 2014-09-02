/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnCall;

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
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ReturnCall execute(String command) {
		if (command.equalsIgnoreCase("hide menu"))
			if (!data.getSettings().addSetting("menuVisible", "false"))
				data.getSettings().updateSetting("menuVisible", "false");
		return null;
	}

}
