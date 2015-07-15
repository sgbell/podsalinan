/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInterface;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.cli.options.downloads.IncreasePriority;

/**
 * @author sbell
 *
 */
public class IncreaseCommand extends CLIOption {

	/**
	 * @param newData
	 */
	public IncreaseCommand(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		String command="";
		String menuInput = command.replaceFirst(command.split(" ")[0]+" ", "");

		IncreasePriority increase = new IncreasePriority(data);
		if (menuInput.equalsIgnoreCase("increase")){
			if (CLInterface.cliGlobals.getGlobalSelection().containsKey("download")){
				//increase.execute(CLInterface.cliGlobals.getGlobalSelection().get("download"));
			} else {
				System.out.println("No Download selected");
			}
		} else if (((menuInput.toLowerCase().startsWith("download")))||
				 	((menuInput.length()>0)&&(menuInput.length()<3))){
			menuInput = menuInput.replaceFirst(menuInput.split(" ")[0]+" ", "");
			
			if ((menuInput.length()>0)&&(menuInput.length()<3)){
				int select = convertCharToNumber(menuInput);
				if ((select>=0)&&(select<data.getUrlDownloads().size())){
						//increase.execute(data.getUrlDownloads().getDownloadUid(select));
				}
			}
		} else 
			System.out.println("Error: Invalid user input.");
		return returnObject;
	}

}
