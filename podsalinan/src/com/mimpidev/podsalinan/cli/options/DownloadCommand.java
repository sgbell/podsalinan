/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import java.net.MalformedURLException;
import java.net.URL;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnCall;

/**
 * @author sbell
 *
 */
public class DownloadCommand extends CLIOption {

	public DownloadCommand(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnCall execute(String command) {
		/*TODO: Finish converting this code from the old system to the current system
		 * of execution
		 */
		
		boolean downloading=false;
		String menuInput= command.replaceFirst(command.split(" ")[0]+" ","");
		if ((menuInput.toLowerCase().contentEquals("download"))||
			(menuInput.toLowerCase().equalsIgnoreCase("episode"))){
			/*if (menuList.get(menuList.size()-1).name.equalsIgnoreCase("selectedEpisode")){
				// If user enters "download" or "download episode" and user has selected episode, download the episode
				CLEpisodeMenu episodeMenu = (CLEpisodeMenu)mainMenu.findSubmenu("episode_selected");
				episodeMenu.downloadEpisode();
				System.out.println("Downloading Episode: "+episodeMenu.getEpisode().getTitle()+" - "+episodeMenu.getEpisode().getDate());
				downloading=true;
			}*/
		} else if (menuInput.length()>6){
			try {
				// newURL is only used to confirm that the user input is a url
				URL newURL = new URL(menuInput);
				data.getUrlDownloads().addDownload(menuInput, data.getSettings().getSettingValue("defaultDirectory"),"-1",false);
				System.out.println("Downloading URL: "+menuInput);
				downloading=true;
			} catch (MalformedURLException e) {
				// menuInput is not a url
				System.out.println("Error: Invalid Input");
			}
		}
		
		if (!downloading){
			System.out.println("Error: Invalid user Input");
		}
		return null;
	}

}
