/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.podcast;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInput;
import com.mimpidev.podsalinan.cli.ReturnCall;
import com.mimpidev.podsalinan.data.Episode;
import com.mimpidev.podsalinan.data.Podcast;

/**
 * @author sbell
 *
 */
public class ListEpisodes extends CLIOption {

	/**
	 * @param newData
	 */
	public ListEpisodes(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnCall execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo("ListEpisodes Class called");
		if (debug) Podsalinan.debugLog.logInfo("Command Value: "+command);
		
		CLInput input = new CLInput();
		
		System.out.println ();
		int epCount=1;
		Podcast selectedPodcast = data.getPodcasts().getPodcastByUid(command.split(" ")[0]);
		if (selectedPodcast!=null){
			synchronized (selectedPodcast.getEpisodes()){
				for (Episode episode : selectedPodcast.getEpisodes()){
					System.out.println (getEncodingFromNumber(epCount)+" - " +
							episode.getTitle()+" : "+episode.getDate());
					epCount++;
					if ((epCount%20)==0){
						System.out.println("-- Press any key to continue, q to quit --");
						char charInput=input.getSingleCharInput();
						if (charInput=='q')
							break;
					}
				}
			}
		}
		returnObject = new ReturnCall();
		returnObject.methodCall = "podcast";
		returnObject.methodParameters = command.split(" ")[0];
		
		return returnObject;
	}

}
