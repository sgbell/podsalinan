/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.cli.options.downloads.DecreasePriority;

/**
 * @author sbell
 *
 */
public class DecreaseCommand extends CLIOption {

	/**
	 * @param newData
	 */
	public DecreaseCommand(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(String command) {
		String menuInput = command.replaceFirst(command.split(" ")[0]+" ", "");

		DecreasePriority decrease = new DecreasePriority(data);
		if (menuInput.equalsIgnoreCase("decrease")){
			if (globalSelection.containsKey("download")){
				decrease.execute(globalSelection.get("download"));
			} else {
				System.out.println("No Download selected");
			}
		} else if ((menuInput.toLowerCase().startsWith("download"))||
				   ((menuInput.length()>0)&&(menuInput.length()<3))){
			menuInput = menuInput.replaceFirst(menuInput.split(" ")[0]+" ", "");
			//TODO: get this to use DecreasePriority
			if ((menuInput.length()>0)&&(menuInput.length()<3)){
				int select = convertCharToNumber(menuInput);
				if ((select>=0)&&(select<data.getUrlDownloads().size())){
					if (data.getUrlDownloads().decreasePriority(select))
					   System.out.println("Decreased Priority: "+data.getUrlDownloads().getDownloads().get(select+1).getURL().toString());
					else
						System.out.println("Error: Download already at the bottom of the list.");
				}
			}
		} else 
			System.out.println("Error: Invalid user input.");
		return returnObject;	
	}
}
