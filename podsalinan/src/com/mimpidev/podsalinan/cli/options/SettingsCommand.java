/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ObjectCall;
import com.mimpidev.podsalinan.cli.options.settings.*;

/**
 * @author sbell
 *
 */
public class SettingsCommand extends CLIOption {

	/**
	 * @param newData
	 * @param returnObject 
	 */
	public SettingsCommand(DataStorage newData, ObjectCall returnObject) {
		super(newData, returnObject);
		ShowMenu showMenu = new ShowMenu(newData);
		options.put("1", new PodcastUpdateRate(newData));
		options.put("2", new MaxDownloaders(newData));
		options.put("3", new DownloadDirectory(newData));
		options.put("4", new AutoQueueEpisodes(newData));
		options.put("5", new DownloadSpeedLimit(newData));
		options.put("", showMenu);
		options.put("showMenu", showMenu);
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ObjectCall execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo("["+getClass().getName()+"] command: "+command);
		returnObject.methodCall="settings";
		
		if (options.containsKey(command.toLowerCase())){
			options.get(command).execute(command);
		} else {
			try {
				Integer.parseInt(command);
				if (command.equals("9")){
					returnObject.methodCall="";
					returnObject.methodParameters="";
				}
			} catch (NumberFormatException e){
				
			}
		}
		
		return returnObject;
	}

}
