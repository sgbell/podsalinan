/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.list;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnCall;
import com.mimpidev.podsalinan.data.Podcast;

/**
 * @author sbell
 *
 */
public class ListPodcasts extends CLIOption {

	/**
	 * @param newData
	 */
	public ListPodcasts(DataStorage newData) {
		super(newData);
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ReturnCall execute(String command) {
		debug=true;
		if (debug) Podsalinan.debugLog.logInfo(this,command);
		int podcastCount=1;
		
		for (Podcast podcast : data.getPodcasts().getList()){
			if (!podcast.isRemoved())
				if (command.split(" ")[command.split(" ").length-1].equalsIgnoreCase("showCount"))
					System.out.print(getEncodingFromNumber(podcastCount));
				else
					System.out.print(podcast.getDatafile());
				System.out.println(". "+podcast.getName());
			podcastCount++;
		}
		
		return returnObject;
	}

	public void printList(boolean showCount){
	}
}
