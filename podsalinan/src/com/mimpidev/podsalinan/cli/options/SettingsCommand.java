/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.cli.options.settings.*;

/**
 * @author sbell
 *
 */
public class SettingsCommand extends CLIOption {

	/**
	 * @param newData
	 */
	public SettingsCommand(DataStorage newData) {
		super(newData);
		ShowMenu showMenu = new ShowMenu(newData);
		options.put("1", new PodcastUpdateRate(newData));
		options.put("2", new MaxDownloaders(newData));
		options.put("3", new DownloadDirectory(newData));
		options.put("4", new AutoQueueEpisodes(newData));
		options.put("5", new DownloadSpeedLimit(newData));
		options.put("", showMenu);
		options.put("showMenu", showMenu);
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		String command="";
		debug=true;
		if (debug) Podsalinan.debugLog.logInfo(this,"Command: "+command);
		
		if (options.containsKey(command.toLowerCase())){
			//returnObject=options.get(command).execute(command);
		} else {
			try {
				Integer.parseInt(command);
				if (command.equals("9")){
					returnObject.methodCall="";
				}
			} catch (NumberFormatException e){
				System.out.println("Error: Invalid Command.");
			}
		}
		
		return returnObject;
	}

}
