/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.cli.options.list.ListDetails;
import com.mimpidev.podsalinan.cli.options.list.ListDownloads;
import com.mimpidev.podsalinan.cli.options.list.ListPodcasts;
import com.mimpidev.podsalinan.cli.options.list.ListPreferences;
import com.mimpidev.podsalinan.cli.options.podcast.ListEpisodes;

/**
 * @author sbell
 *
 */
public class ListCommand extends CLIOption {

	public ListCommand(DataStorage newData) {
		super(newData);
		options.put("podcasts",new ListPodcasts(newData));
		options.put("episodes",new ListEpisodes(newData));
		options.put("downloads",new ListDownloads(newData));
		options.put("preferences",new ListPreferences(newData));
		options.put("details",new ListDetails(newData));
	}

	@Override
	public ReturnObject execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo(this,command);
		
		if (options.containsKey(command.toLowerCase().split(" ")[0])){
			options.get(command.toLowerCase().split(" ")[0]).execute((command.split(" ", 2).length>1?command.split(" ",2)[1]:command));
		} else {
			System.out.println("Error: Invalid command");
		}

		return returnObject;
	}
}
