/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.podcast;

import java.util.Vector;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInput;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.cli.options.episode.SelectEpisode;
import com.mimpidev.podsalinan.cli.options.generic.ChangeDestination;
import com.mimpidev.podsalinan.data.Podcast;

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
	public ReturnObject execute(String command) {
		debug=true;
		if (debug) Podsalinan.debugLog.logInfo(this,"Line:41, Command :"+command);
		
		if (command.split(" ").length==1 && command.length()==1){
			if (command.equals("9") && globalSelection.size()>0){
				globalSelection.clear();
				command="";
			}
		} else {
			Podcast selectedPodcast = data.getPodcasts().getPodcastByUid(command);
			if (selectedPodcast==null){
				Vector<Podcast> podcastList = data.getPodcasts().getPodcastListByName(command);
				if (debug) Podsalinan.debugLog.logInfo(this, "Line:52, PodcastList.size="+podcastList.size());
				if (podcastList.size()==1){
					globalSelection.clear();
					globalSelection.put("podcast",podcastList.get(0).getDatafile());
					selectedPodcast=podcastList.get(0);
				} else if (podcastList.size()>1){
					int podcastCount=1;
					// If too many podcasts with text found
					System.out.println ("Matches Found: "+podcastList.size());
					for (Podcast foundPodcast : podcastList){
						System.out.println(getEncodingFromNumber(podcastCount)+". "+foundPodcast.getName());
					    podcastCount++;
					}
					System.out.print("Please select a podcast: ");
					// Ask user to select podcast
					CLInput input = new CLInput();
					String userInput = input.getStringInput();
					if ((userInput.length()>0)&&(userInput!=null)){
						int selection = convertCharToNumber(userInput);
						if ((selection>=0)&&(selection<podcastList.size())){
							selectedPodcast = podcastList.get(selection);
						} else
							System.out.println("Error: Invalid user input");
					} else 
						System.out.println("Error: Invalid user input");
				} else {
					System.out.println("Error: Podcast not found.");
				}
			}
			if (selectedPodcast!=null){
				globalSelection.clear();
				globalSelection.put("podcast",selectedPodcast.getDatafile());
				command=selectedPodcast.getDatafile();
			}
		}

		/*TODO: need to fix this, so that traversing the menu still works
		 */
		if (command.length()==8){
			returnObject = options.get("").execute(command);
			if (debug) Podsalinan.debugLog.logInfo(this,"Command Length:"+command.length());
		}else if (command.split(" ").length>1){
			if (debug) Podsalinan.debugLog.logInfo(this,"Command Length:"+command.length());
			if (command.split(" ")[1].equals("9")){
				returnObject.methodCall="podcast";
				returnObject.methodParameters="";
			} else {
				if (debug) Podsalinan.debugLog.logInfo(this, "Command: "+command.split(" ")[1]);
				if (debug) Podsalinan.debugLog.logInfo(this, "Podcast: "+convertCharToNumber(command.split(" ")[1]));
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
