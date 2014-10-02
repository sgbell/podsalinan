/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.podcast;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnCall;
import com.mimpidev.podsalinan.cli.options.generic.ChangeDestination;

/**
 * @author sbell
 *
 */
public class SelectPodcast extends CLIOption {

	public SelectPodcast(DataStorage newData) {
		super(newData);
		
		ShowSelectedMenu showMenu = new ShowSelectedMenu(newData);
		options.put("", showMenu);
		options.put("1", new ListEpisodes(newData));
		options.put("2", new UpdatePodcast(newData));
		options.put("3", new DeletePodcast(newData));
		options.put("4", new ChangeDestination(newData));
		options.put("5", new AutoQueueEpisodes(newData));
		options.put("showSelectedMenu", showMenu);
	}

	@Override
	public ReturnCall execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo(this.getClass().getName()+":"+command);
		if (command.length()==8)
			returnObject = options.get("").execute(command);
		else if (command.split(" ")[1].equals("9")){
			returnObject.methodCall="podcast";
			returnObject.methodParameters="";
		} else {
			returnObject = options.get(command.split(" ")[1]).execute(command);
		}
		
		return returnObject;
	}

}
