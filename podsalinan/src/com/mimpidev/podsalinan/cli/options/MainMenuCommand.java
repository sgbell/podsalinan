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

	/**
	 * @param newData
	 */
	public MainMenuCommand(DataStorage newData) {
		super(newData);
		options = new HashMap<String, CLIOption>();
		options.put("showMenu", new ShowMenu(newData));

	}

	public void showMenu(){
		System.out.println(data.getPodcasts().getList().size()+" - Podcasts. "+data.getUrlDownloads().visibleSize()+" - Downloads Queued");
		System.out.println();
		System.out.println("1. Podcasts Menu");
		System.out.println("2. Downloads Menu");
		System.out.println("3. Preferences");
		System.out.println();
		System.out.println("4. Quit");
	}
	
	@Override
	public ReturnCall execute(String command) {
		ReturnCall returnValue = new ReturnCall();
		
		/*returnValue.methodCall=menuCommands.get(command);
		returnValue.methodParameters="showMenu";
		returnValue.execute=true;*/
		
		return returnValue;
	}

}
