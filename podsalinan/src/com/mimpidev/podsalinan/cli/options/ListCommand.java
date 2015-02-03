/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnCall;
import com.mimpidev.podsalinan.cli.options.List.ListDetails;
import com.mimpidev.podsalinan.cli.options.List.ListDownloads;
import com.mimpidev.podsalinan.cli.options.List.ListPodcasts;
import com.mimpidev.podsalinan.cli.options.List.ListPreferences;
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
	public ReturnCall execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo(this,command);
		
		if (options.containsKey(command.toLowerCase())){
			options.get(command).execute(command);
		} else {
			
		}
		/*String menuInput = command.replaceAll("(?i)list ", "");
		if (menuInput.toLowerCase().startsWith("podcast")){
			menuList.clear();
			menuList.add(new MenuPath("mainMenu", "podcast"));
			((CLPodcastMenu)(mainMenu.findSubmenu("podcast"))).listPodcasts();
		} else if (menuInput.toLowerCase().startsWith("episode")){
			if ((menuList.isValidSetting("mainMenu"))&&
				((menuList.findSetting("mainMenu").equalsIgnoreCase("podcast"))&&
				 (menuList.findSetting("selectedPodcast")!=null))){
				((CLPodcastSelectedMenu)(mainMenu.findSubmenu("podcast_selected"))).printEpisodeList();
			//} else {
			//	System.out.println("Error: No podcast selected.");
			//}
		} else if (menuInput.toLowerCase().startsWith("select")){
		//	Podcast selectedPodcast=null;
		//	System.out.println("Current selection");
			for (MenuPath currentItem : menuList){
				if (currentItem.name.equalsIgnoreCase("selectedPodcast")){
					for (Podcast podcast : data.getPodcasts().getList())
						if (podcast.getDatafile().equalsIgnoreCase(currentItem.value)){
							selectedPodcast=podcast;
							System.out.println(currentItem.name+": "+podcast.getName());
						}
				} else if (currentItem.name.equalsIgnoreCase("selectedEpisode")){
					if (selectedPodcast!=null){
						Episode currentEpisode = selectedPodcast.getEpisodes().get(Integer.parseInt(currentItem.value));
						System.out.println(currentItem.name+": "+currentEpisode.getTitle()+" : "+currentEpisode.getDate());
					}
			    }else
					System.out.println(currentItem.name+": "+currentItem.value);
			}
		} else if (menuInput.toLowerCase().startsWith("details")){
			//mainMenu.process(98);
		} else if (menuInput.toLowerCase().startsWith("downloads")){
			//((CLDownloadMenu)mainMenu.findSubmenu("downloads")).listDownloads();
		} else if (menuInput.toLowerCase().startsWith("preferences")){
			//((CLPreferencesMenu)mainMenu.findSubmenu("preferences")).printList();
		} else
			System.out.println("Error: Invalid user input.");*/
		return returnObject;

	}

}
