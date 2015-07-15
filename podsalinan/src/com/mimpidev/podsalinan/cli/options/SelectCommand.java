/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.cli.options.downloads.SelectDownload;
import com.mimpidev.podsalinan.cli.options.episode.SelectEpisode;

/**
 * @author sbell
 *
 */
public class SelectCommand extends CLIOption {

	public SelectCommand(DataStorage newData) {
		super(newData);
		options.put("episode", new SelectEpisode(newData));
		options.put("podcast", new PodcastCommand(newData));
		options.put("download", new SelectDownload(newData));
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		String command="";
		if (debug) Podsalinan.debugLog.logInfo(this, "Command: "+command);
		
		String[] commandSplit = command.split(" ",2);
		if (commandSplit.length>1){
			if (debug) Podsalinan.debugLog.logInfo(this," next command: "+commandSplit[0]);
			if (options.containsKey(commandSplit[0].toLowerCase())){
				//returnObject=options.get(commandSplit[0].toLowerCase()).execute(commandSplit[1]);
			}
		} else {
			System.out.println("Error: Invalid input.");
		}
		return returnObject;
	}
}
