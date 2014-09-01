/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnCall;
import com.mimpidev.podsalinan.data.Podcast;

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
		
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ReturnCall execute(String command) {
		return null;
	}

	@Override
	public void showMenu() {
		System.out.println();
		int podcastCount=1;
		
		for (Podcast podcast : data.getPodcasts().getList()){
			if (!podcast.isRemoved())
				System.out.println(getEncodingFromNumber(podcastCount)+". "+podcast.getName());
			podcastCount++;
		}

		System.out.println();
		System.out.println("(A-Z) Enter Podcast letter to select Podcast.");
		System.out.println();
		System.out.println("9. Return to Main Menu");
	}

}
