/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInterface;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.cli.options.downloads.DeleteDownload;
import com.mimpidev.podsalinan.cli.options.podcast.DeletePodcast;

/**
 * @author sbell
 *
 */
public class RemoveCommand extends CLIOption {

	/**
	 * @param newData
	 */
	public RemoveCommand(DataStorage newData) {
		super(newData);
		DeleteDownload deleteDownload = new DeleteDownload(newData);
		options.put("downloads", deleteDownload);
		options.put("download", deleteDownload);
		options.put("podcast", new DeletePodcast(newData));
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		String command="";
		debug=true;

		if (debug) Podsalinan.debugLog.logInfo(this, "command: "+command);
		
		if (command.length()==0){
			String[] deleteType = {"downloads","podcast"};
			boolean found=false;
			for (String deleteItem: deleteType){
				if (debug) Podsalinan.debugLog.logInfo(this, "Check: "+deleteItem);
				if (!found && CLInterface.cliGlobals.getGlobalSelection().containsKey(deleteItem)){
				    if (debug) Podsalinan.debugLog.logInfo(this, "Global Selection contains: "+deleteItem);
				    //returnObject=options.get(deleteItem).execute(CLInterface.cliGlobals.getGlobalSelection().get(deleteItem));
				    found=true;
				}
			}
			if (!found){
				System.out.println("Error: Invalid User Input, no Podcast or Download currently selected");
				returnObject.execute=true;
			}
		} else {
			String[] commandOptions = command.split(" ");
			if (options.containsKey(commandOptions[0])){
				//returnObject=options.get(commandOptions[0]).execute(command.replaceFirst(commandOptions[0]+" ", ""));
			} else {
				System.out.println("Error: Invalid User Input");
				returnObject.execute=true;
			}
		}
		
		if (debug) Podsalinan.debugLog.logInfo(this, "methodCall: "+returnObject.methodCall);
		
		return returnObject;
	}
}
