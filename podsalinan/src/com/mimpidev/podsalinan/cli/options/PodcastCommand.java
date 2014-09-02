/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import java.util.HashMap;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnCall;
import com.mimpidev.podsalinan.cli.options.podcast.*;

/**
 * @author sbell
 *
 */
public class PodcastCommand extends CLIOption {

	/**
	 * @param newData
	 */
	public PodcastCommand(DataStorage newData) {
		super(newData);
		options = new HashMap<String, CLIOption>();
		options.put("<a-zz>", new SelectPodcast(newData));
		options.put("showMenu", new ShowMenu(newData));
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ReturnCall execute(String command) {
		if (options.containsKey(command))
			options.get(command).execute(command);
		return null;
	}
}
