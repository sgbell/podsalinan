/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInterface;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.cli.options.episode.DownloadEpisode;

/**
 * @author sbell
 *
 */
public class DownloadCommand extends CLIOption {

	public DownloadCommand(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		debug=true;
		
		boolean downloading=false;
		//String menuInput= command.replaceFirst(command.split(" ")[0]+" ","");
		String menuInput="";
		if ((menuInput.toLowerCase().contentEquals("download"))||
			(menuInput.toLowerCase().equalsIgnoreCase("episode"))){
			if (CLInterface.cliGlobals.getGlobalSelection().containsKey("episode")){
				// Use the command already set up to do the work.
				DownloadEpisode queueEpisode = new DownloadEpisode(data);
				queueEpisode.execute(CLInterface.cliGlobals.globalSelectionToString());
			} else {
				System.out.println("No episode selected");
			}
		} else if (menuInput.length()>6){
			URLCommand urlCommand = new URLCommand(data);
			//urlCommand.execute(menuInput);
		}
		return returnObject;
	}

}
