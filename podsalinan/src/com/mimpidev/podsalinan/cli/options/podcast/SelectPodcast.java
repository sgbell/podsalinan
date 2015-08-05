/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.podcast;

import java.util.Map;
import java.util.Vector;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInput;
import com.mimpidev.podsalinan.cli.CLInterface;
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
	public ReturnObject execute(Map<String, String> functionParms) {
		debug=true;

        /* Only go through this code, if the podcast being passed in is different to the podcast stored in
		 * global selection
		 */
		if (!(CLInterface.cliGlobals.getGlobalSelection().containsKey("podcastId") && 
             (functionParms.get("uid").equals(CLInterface.cliGlobals.getGlobalSelection().get("podcastId"))))){
			Podcast selectedPodcast = data.getPodcasts().getPodcastByUid(functionParms.get("uid"));
	    	if (functionParms.containsKey("userInput") && (selectedPodcast==null)){
		    	if (functionParms.get("userInput").length()==1)
		    		selectedPodcast = data.getPodcasts().getList().get(convertCharToNumber(functionParms.get("userInput")));
			   	if (selectedPodcast==null){
					Vector<Podcast> podcastList = data.getPodcasts().getPodcastListByName(functionParms.get("userInput"));
					if (debug) Podsalinan.debugLog.logInfo(this, "Line:57, PodcastList.size="+podcastList.size());
					if (podcastList.size()==1){
						CLInterface.cliGlobals.getGlobalSelection().clear();
						CLInterface.cliGlobals.getGlobalSelection().put("podcast",podcastList.get(0).getDatafile());
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
				
			}
			if (selectedPodcast!=null){
				if (debug) Podsalinan.debugLog.logInfo(this, 87, "Set selected podcast:"+selectedPodcast.getDatafile());
				CLInterface.cliGlobals.getGlobalSelection().clear();
				CLInterface.cliGlobals.getGlobalSelection().put("podcastid",selectedPodcast.getDatafile());
			}
    	}

		if (functionParms.containsKey("uid") && 
			((data.getSettings().findSetting("menuVisible")==null)||
			  data.getSettings().findSetting("menuVisible").equalsIgnoreCase("true"))){
			//returnObject = options.get("").execute(command);
		} else if ((!data.getSettings().findSetting("menuVisible").equalsIgnoreCase("true"))&&
				   (data.getSettings().findSetting("menuVisible")!=null)){
			ShowPodcastDetails podcastDetail=new ShowPodcastDetails(data);
			//returnObject = podcastDetail.execute("");
		} else {
			//returnObject = options.get("").execute(command);
		}
		
		return returnObject;
	}

}
