/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.podcast;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInput;
import com.mimpidev.podsalinan.cli.CLInterface;
import com.mimpidev.podsalinan.cli.ReturnObject;
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
		debug=true;
	}

	@Override
	public ReturnObject execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo(this,30,"ListEpisodes Class called");
		if (debug) Podsalinan.debugLog.logInfo(this,31,"Command Value: "+command);
		
		CLInput input = new CLInput();
		
		int epCount=1;
		Podcast selectedPodcast = data.getPodcasts().getPodcastByUid(command.split(" ")[0]);
		if (selectedPodcast==null && CLInterface.cliGlobals.getGlobalSelection().containsKey("podcast")){
			selectedPodcast = data.getPodcasts().getPodcastByUid(CLInterface.cliGlobals.getGlobalSelection().get("podcast"));
		}
		if (selectedPodcast!=null){
			System.out.println ();
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
		} else {
			if (command.equalsIgnoreCase("episodes")){
				System.out.println("Error: No podcast has been selected");
			}
		}
		returnObject = new ReturnObject();
		returnObject.methodCall = "podcast";
		returnObject.methodParameters = command.split(" ")[0];
		returnObject.execute=true;
		
		return returnObject;
	}

}
