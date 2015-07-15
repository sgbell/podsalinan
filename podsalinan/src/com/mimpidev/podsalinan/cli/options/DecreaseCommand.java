/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInterface;
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

	public ReturnObject execute(Map<String, String> functionParms) {
//		String menuInput = command.replaceFirst(command.split(" ")[0]+" ", "");
		String menuInput = "";

		DecreasePriority decrease = new DecreasePriority(data);
		if (menuInput.equalsIgnoreCase("decrease")){
			if (CLInterface.cliGlobals.getGlobalSelection().containsKey("download")){
				//decrease.execute(CLInterface.cliGlobals.getGlobalSelection().get("download"));
			} else {
				System.out.println("No Download selected");
			}
		} else if ((menuInput.toLowerCase().startsWith("download"))||
				   ((menuInput.length()>0)&&(menuInput.length()<3))){
			menuInput = menuInput.replaceFirst(menuInput.split(" ")[0]+" ", "");
			if ((menuInput.length()>0)&&(menuInput.length()<3)){
				int select = convertCharToNumber(menuInput);
				if ((select>=0)&&(select<data.getUrlDownloads().size())){
					//decrease.execute(data.getUrlDownloads().getDownloadUid(select));
				}
			}
		} else 
			System.out.println("Error: Invalid user input.");
		return returnObject;	
	}

}
