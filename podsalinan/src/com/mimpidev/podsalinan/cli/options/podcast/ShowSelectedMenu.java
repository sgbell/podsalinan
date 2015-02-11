/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.podcast;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ObjectCall;
import com.mimpidev.podsalinan.data.Podcast;

/**
 * @author sbell
 *
 */
public class ShowSelectedMenu extends CLIOption {

	public ShowSelectedMenu(DataStorage newData) {
		super(newData);
	}

	@Override
	public ObjectCall execute(String command) {
		returnObject = new ObjectCall();
		
		String podcastId = command.split(" ")[0];
		Podcast currentPodcast = data.getPodcasts().getPodcastByUid(podcastId);
			if (currentPodcast!=null){
				System.out.println();
				System.out.println("Podcast: "+currentPodcast.getName()+ " - Selected");
				System.out.println("1. List Episodes");
				System.out.println("2. Update List");
				System.out.println("3. Delete Podcast");
				System.out.println("4. Change Download Directory");
				System.out.println("5. Autoqueue Episodes");
				System.out.println("<AA>. Select Episode");
				System.out.println();
				System.out.println("9. Return to List of Podcasts");
				System.out.println();
				
				returnObject.methodCall = "podcast";
				returnObject.methodParameters = command;
				
				return returnObject;
			}
		
		return returnObject;
	}

}
