/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.downloads;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInterface;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.cli.options.generic.ChangeDestination;
import com.mimpidev.podsalinan.data.URLDownload;

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
	}

	@Override
	public ReturnObject execute(String command) {
		debug=true;
		if (debug) Podsalinan.debugLog.logInfo("["+getClass().getName()+"] command: "+command);
		
		String[] commandOptions = command.split(" ");
		
		if (commandOptions[0].length()<=2){
			int downloadId = convertCharToNumber(commandOptions[0]);
			String downloadUid = data.getUrlDownloads().findActiveDownloadByCount(downloadId);
			if (downloadUid!=null){
				//TODO: finish working here
			} else {
				System.out.println("Error: Invalid Download Selected");
			}
		}
		
		if (debug) Podsalinan.debugLog.logInfo(this, 60, "Command: "+command);
		try {
			Integer.parseInt(command);
			if (command.equals("9")){
				CLInterface.cliGlobals.getGlobalSelection().clear();
				returnObject.methodCall="";
				returnObject.methodParameters="";
				returnObject.execute=true;
			}
		} catch (NumberFormatException e){
			if (command.split(" ")[1].equals("9")){
				returnObject.methodCall="downloads";
				returnObject.methodParameters="";
			} else {
				returnObject = options.get(command.split(" ")[1]).execute(command);
			}
		}
		return returnObject;
	}

}
