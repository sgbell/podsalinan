/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLDownloadMenu;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLPodcastMenu;
import com.mimpidev.podsalinan.cli.CLPodcastSelectedMenu;
import com.mimpidev.podsalinan.cli.CLPreferencesMenu;
import com.mimpidev.podsalinan.cli.CLInterface.MenuPath;
import com.mimpidev.podsalinan.data.Episode;
import com.mimpidev.podsalinan.data.Podcast;

/**
 * @author sbell
 *
 */
public class ListCommand extends CLIOption {

	public ListCommand(DataStorage newData) {
		super(newData);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public void execute(String command) {
		menuInput = menuInput.replaceAll("(?i)list ", "");
		if (menuInput.toLowerCase().startsWith("podcast")){
			menuList.clear();
			menuList.add(new MenuPath("mainMenu", "podcast"));
			((CLPodcastMenu)(mainMenu.findSubmenu("podcast"))).listPodcasts();
		} else if (menuInput.toLowerCase().startsWith("episode")){
			if ((menuList.isValidSetting("mainMenu"))&&
				((menuList.findSetting("mainMenu").equalsIgnoreCase("podcast"))&&
				 (menuList.findSetting("selectedPodcast")!=null))){
				((CLPodcastSelectedMenu)(mainMenu.findSubmenu("podcast_selected"))).printEpisodeList();
			} else {
				System.out.println("Error: No podcast selected.");
			}
		} else if (menuInput.toLowerCase().startsWith("select")){
			Podcast selectedPodcast=null;
			System.out.println("Current selection");
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
			mainMenu.process(98);
		} else if (menuInput.toLowerCase().startsWith("downloads")){
			((CLDownloadMenu)mainMenu.findSubmenu("downloads")).listDownloads();
		} else if (menuInput.toLowerCase().startsWith("preferences")){
			((CLPreferencesMenu)mainMenu.findSubmenu("preferences")).printList();
		} else
			System.out.println("Error: Invalid user input.");

	}

}
