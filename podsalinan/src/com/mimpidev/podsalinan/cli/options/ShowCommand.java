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
public class ShowCommand extends CLIOption {

	/**
	 * @param newData
	 */
	public ShowCommand(DataStorage newData) {
		super(newData);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ReturnCall execute(String command) {
		returnObject = new ReturnCall();
		
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
