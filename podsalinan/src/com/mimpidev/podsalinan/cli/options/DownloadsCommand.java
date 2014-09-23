/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import java.util.HashMap;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnCall;
import com.mimpidev.podsalinan.cli.options.downloads.SelectDownload;
import com.mimpidev.podsalinan.cli.options.downloads.ShowMenu;

/**
 * @author sbell
 *
 */
public class DownloadsCommand extends CLIOption {

	/**
	 * @param newData
	 */
	public DownloadsCommand(DataStorage newData) {
		super(newData);
		ShowMenu showMenu = new ShowMenu(newData);
		options.put("<aa>", new SelectDownload(newData));
		options.put("showMenu", showMenu);
		options.put("", showMenu);
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ReturnCall execute(String command) {
		returnObject = new ReturnCall();
		returnObject.methodCall="downloads";
		
		if (options.containsKey(command))
			options.get(command).execute(command);
		else {
			try {
				Integer.parseInt(command);
				if (command.equals("9")){
					returnObject.methodCall="";
					returnObject.methodParameters="";
				}
			} catch (NumberFormatException e) {
				if (command.split(" ").length==1){
					if ((convertCharToNumber(command)<data.getUrlDownloads().visibleSize())&&
						(convertCharToNumber(command)>0))
						returnObject = options.get("<aa>").execute(command);
				}
			}
		}
		
		return returnObject;
	}

}
