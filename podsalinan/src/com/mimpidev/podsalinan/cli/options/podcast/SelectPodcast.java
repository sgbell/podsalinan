/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.podcast;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnCall;
import com.mimpidev.podsalinan.cli.options.episode.SelectEpisode;
import com.mimpidev.podsalinan.cli.options.generic.ChangeDestination;

/**
 * @author sbell
 *
 */
public class SelectPodcast extends CLIOption {

	public SelectPodcast(DataStorage newData) {
		super(newData);
		debug=true;
		
		ShowSelectedMenu showMenu = new ShowSelectedMenu(newData);
		options.put("", showMenu);
		options.put("1", new ListEpisodes(newData));
		options.put("2", new UpdatePodcast(newData));
		options.put("3", new DeletePodcast(newData));
		options.put("4", new ChangeDestination(newData));
		options.put("5", new AutoQueueEpisodes(newData));
		options.put("showSelectedMenu", showMenu);
		options.put("<aa>", new SelectEpisode(newData));
	}

	@Override
	public ReturnCall execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo(this.getClass().getName()+":"+command);
		if (command.length()==8)
			returnObject = options.get("").execute(command);
		else if (command.split(" ").length>1){
			if (command.split(" ")[1].equals("9")){
				returnObject.methodCall="podcast";
				returnObject.methodParameters="";
			} else {
				if (debug) Podsalinan.debugLog.logInfo(this, "Command: "+command.split(" ")[1]);
				if (debug) Podsalinan.debugLog.logInfo(this, "Command: "+convertCharToNumber(command.split(" ")[1]));
				if (convertCharToNumber(command.split(" ")[1])>=0){
					returnObject = options.get("<aa>").execute(command);
				} else {
					returnObject = options.get("").execute(command);
				}
			}
		} else {
			returnObject = options.get("").execute(command);
		}
		
		return returnObject;
	}

}
