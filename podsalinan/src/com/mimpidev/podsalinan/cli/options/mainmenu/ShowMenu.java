/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.mainmenu;

import java.util.HashMap;
import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnCall;

/**
 * @author sbell
 *
 */
public class ShowMenu extends CLIOption {

    private Map<String,String> menuCommands;
	/**
	 * @param newData
	 */
	public ShowMenu(DataStorage newData) {
		super(newData);
		menuCommands = new HashMap<String,String>();
		menuCommands.put("1","podcast");
		menuCommands.put("2", "downloads");
		menuCommands.put("3", "settings");
		menuCommands.put("4", "quit");
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ReturnCall execute(String command) {
		
		return null;
	}

}
