/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.downloads;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnCall;

/**
 * @author sbell
 *
 */
public class SelectDownload extends CLIOption {

	/**
	 * @param newData
	 */
	public SelectDownload(DataStorage newData) {
		super(newData);
		ShowSelectedMenu showMenu = new ShowSelectedMenu(newData);
		
		options.put("1", new DeleteDownload(newData));		
		options.put("2", new RestartDownload(newData));		
		options.put("3", new StopDownload(newData));		
		options.put("4", new StartDownload(newData));		
		options.put("5", new IncreasePriority(newData));		
		options.put("6", new DecreasePriority(newData));		
		options.put("7", new ChangeDestination(newData));
		options.put("showSelectedMenu", showMenu);
		options.put("", showMenu);
		
		debug=true;
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ReturnCall execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo("["+getClass().getName()+"] command: "+command);
		
		if (command.length()<=2){
			returnObject = options.get("").execute(command);
		} else {
			
		}
		return returnObject;
	}

}
