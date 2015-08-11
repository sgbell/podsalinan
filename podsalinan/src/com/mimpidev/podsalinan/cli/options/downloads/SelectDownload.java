/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.downloads;

import java.util.Map;

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
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		debug=true;
		if (debug) Podsalinan.debugLog.logMap(this, functionParms);

		if (functionParms.containsKey("userInput")){
			String userInput=functionParms.get("userInput");
			String selectedDownload=null;
			
			if (data.getUrlDownloads().getNumberOfQueuedDownloads()>convertCharToNumber(userInput)){
				selectedDownload = data.getUrlDownloads().getDownloadUid(convertCharToNumber(userInput));
			}
			if (selectedDownload==null){
				System.out.println("Error: Invalid download requested.");
				returnObject.methodCall="downloads showmenu";
			} else {
				
			}
		}
		/*
		if (commandOptions[0].length()<=2){
			if (debug) Podsalinan.debugLog.logInfo(this, "Download ID: "+commandOptions[0]);
			int downloadId = convertCharToNumber(commandOptions[0]);
			String downloadUid = data.getUrlDownloads().getDownloadUid(downloadId);
			if (debug) Podsalinan.debugLog.logInfo(this, "Download UID: "+downloadUid);
			if (downloadUid!=null)
				commandOptions[0]=downloadUid;
		}
		for (String option : commandOptions){
			if (command.length()>0){
				command+=" ";
			}
			command+=option;
		}
        if (!(CLInterface.cliGlobals.getGlobalSelection().containsKey("downloads") &&
        	commandOptions[0].equals(CLInterface.cliGlobals.getGlobalSelection().get("downloads")))){
        	if (command.split(" ").length==1 && command.length()==1){
        		if (command.equals("9") && CLInterface.cliGlobals.getGlobalSelection().size()>0){
            		CLInterface.cliGlobals.getGlobalSelection().clear();
            		command="";
            		if (debug) Podsalinan.debugLog.logInfo(this, "Cleared Download selection");
        		}
       		} else {
        		URLDownload selectedDownload = data.getUrlDownloads().findDownloadByUid(commandOptions[0]);
        		if (selectedDownload!=null){
        			CLInterface.cliGlobals.getGlobalSelection().clear();
        			CLInterface.cliGlobals.getGlobalSelection().put("downloads", commandOptions[0]);
        		}
        	}
        }
		if (debug) Podsalinan.debugLog.logInfo(this, 76, "Command: "+command);
		if (commandOptions.length==1){
			if (commandOptions[0].length()>1){
			   //returnObject = options.get("").execute(command);
			} else {
				System.out.println("Error: Invalid Command");
			}
		} else if (commandOptions.length > 1){
			if (commandOptions[1].equals("9")){
				// If the command is 9 (exit the selected download menu & clear the selected download)
				CLInterface.cliGlobals.getGlobalSelection().clear();
				returnObject.methodCall="downloads";
				returnObject.execute=true;
			} else {
				//if it doesn't equal 9 call the command
				if (options.containsKey(commandOptions[1])){
					//returnObject = options.get(commandOptions[1]).execute(command);
				} else {
					System.out.println("Error: Command does not exist.");
				}
			}
		}*/
		return returnObject;
	}

}
