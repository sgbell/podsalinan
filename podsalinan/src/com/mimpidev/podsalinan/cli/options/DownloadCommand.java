/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import java.net.MalformedURLException;
import java.net.URL;

import com.mimpidev.podsalinan.CLEpisodeMenu;
import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;

/**
 * @author sbell
 *
 */
public class DownloadCommand extends CLIOption {

	public DownloadCommand(DataStorage newData) {
		super(newData);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(String command) {
		boolean downloading=false;
		menuInput= menuInput.replaceFirst(menuInput.split(" ")[0]+" ","");
		if ((menuInput.toLowerCase().contentEquals("download"))||
			(menuInput.toLowerCase().equalsIgnoreCase("episode"))){
			if (menuList.get(menuList.size()-1).name.equalsIgnoreCase("selectedEpisode")){
				// If user enters "download" or "download episode" and user has selected episode, download the episode
				CLEpisodeMenu episodeMenu = (CLEpisodeMenu)mainMenu.findSubmenu("episode_selected");
				episodeMenu.downloadEpisode();
				System.out.println("Downloading Episode: "+episodeMenu.getEpisode().getTitle()+" - "+episodeMenu.getEpisode().getDate());
				downloading=true;
			}
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
	}

}
