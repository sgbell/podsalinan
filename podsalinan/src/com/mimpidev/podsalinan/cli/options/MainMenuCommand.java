/*******************************************************************************
 * Copyright (c) Sam Bell.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or  any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See
 * the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Sam Bell - initial API and implementation
 ******************************************************************************/
/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import java.util.HashMap;
import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnCall;
import com.mimpidev.podsalinan.cli.options.mainmenu.ShowMenu;

/**
 * @author bugman
 *
 */
public class MainMenuCommand extends CLIOption {

    private Map<String,String> menuCommands;
	/**
	 * @param newData
	 */
	public MainMenuCommand(DataStorage newData) {
		super(newData);
		options = new HashMap<String, CLIOption>();
		options.put("showMenu", new ShowMenu(newData));
		
		menuCommands = new HashMap<String,String>();
		menuCommands.put("1","podcast");
		menuCommands.put("2", "downloads");
		menuCommands.put("3", "settings");
		menuCommands.put("4", "quit");
	}

	@Override
	public ReturnCall execute(String command) {
		ReturnCall returnValue=null;

		if (options.containsKey(command))
			options.get(command).execute(command);
		else if (menuCommands.containsKey(command)){
			returnValue = new ReturnCall();
			
			returnValue.methodCall=menuCommands.get(command);
			returnValue.methodParameters="";
			//returnValue.methodParameters="showMenu";
			returnValue.execute=true;
		} else if (command.length()==0) {
			options.get("showMenu").execute("");
		} else {
			System.out.println("Error: Invalid User Command");
		}
		return returnValue;
	}

}
