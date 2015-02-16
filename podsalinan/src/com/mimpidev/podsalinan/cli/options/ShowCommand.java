/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObjcet;

/**
 * @author sbell
 *
 */
public class ShowCommand extends CLIOption {

	/**
	 * @param newData
	 * @param returnObject 
	 */
	public ShowCommand(DataStorage newData, ReturnObjcet returnObject) {
		super(newData,returnObject);
	}

	@Override
	public ReturnObjcet execute(String command) {
		/*TODO: flesh out ShowCommand
		 * 
		 */
		/*
		if (menuInput.equalsIgnoreCase("show menu")){
			if (!data.getSettings().addSetting("menuVisible", "true"))
				data.getSettings().updateSetting("menuVisible", "true");
			mainMenu.process(99);
		} else if (menuInput.toLowerCase().startsWith("show")){
			menuInput= menuInput.replaceFirst(menuInput.split(" ")[0]+" ","");
			if (menuInput.toLowerCase().equalsIgnoreCase("details")){
				mainMenu.process(98);
			} else {
				System.out.println("Error: Invalid User Input");
			}
		}*/
		return returnObject;
	}

}
