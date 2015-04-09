/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.cli.options.generic.ChangeDestination;
import com.mimpidev.podsalinan.cli.options.settings.AutoQueueEpisodes;
import com.mimpidev.podsalinan.cli.options.settings.DownloadDirectory;
import com.mimpidev.podsalinan.cli.options.settings.DownloadSpeedLimit;
import com.mimpidev.podsalinan.cli.options.settings.MaxDownloaders;
import com.mimpidev.podsalinan.cli.options.settings.MenuVisibility;
import com.mimpidev.podsalinan.cli.options.settings.PodcastUpdateRate;

/**
 * @author sbell
 *
 */
public class SetCommand extends CLIOption {

	/**
	 * @param newData
	 */
	public SetCommand(DataStorage newData) {
		super(newData);
		options.put("updateinterval", new PodcastUpdateRate(newData));
		options.put("downloadlimit", new DownloadSpeedLimit(newData));
		options.put("maxdownloaders", new MaxDownloaders(newData));
		options.put("autoqueue", new AutoQueueEpisodes(newData));
		options.put("defaultdirectory", new DownloadDirectory(newData));
		ChangeDestination changeDestination = new ChangeDestination(newData);
		options.put("destination", changeDestination); 
		options.put("podcast", changeDestination);
		options.put("menuvisible", new MenuVisibility(newData));
	}

	@Override
	public ReturnObject execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo(this, "Command: "+command);
		String[] commandOptions = command.split(" ");
		if (options.containsKey(commandOptions[0].toLowerCase())){
			returnObject = options.get(commandOptions[0].toLowerCase()).execute(command);
		} else {
			System.out.println("Error: Invalid user input.");
		}
		
		return returnObject;
		/*menuInput = menuInput.replaceFirst(menuInput.split(" ")[0]+" ", "");
		
        if (menuInput.toLowerCase().startsWith("downloadlimit")){
			String name = menuInput.split(" ")[0];
			menuInput = menuInput.replaceFirst(menuInput.split(" ")[0]+" ", "");
			int value=-1;
			try {
				value = Integer.parseInt(menuInput);
			} catch (NumberFormatException e){
				value = -1;
			}
			if (value>-1){
				data.getSettings().updateSetting(name, menuInput);
				System.out.println(name+" value updated");
			}
		} else if (menuInput.toLowerCase().startsWith("maxdownloaders")){
			String name = menuInput.split(" ")[0];
			menuInput = menuInput.replaceFirst(menuInput.split(" ")[0]+" ", "");
			int value=-1;
			try {
				value = Integer.parseInt(menuInput);
			} catch (NumberFormatException e){
				value = -1;
			}
			if (value>-1){
				data.getSettings().updateSetting(name, menuInput);
				System.out.println(name+" value updated");
			} else
				System.out.println("Error: Invalid user input.");
		} else if ((menuInput.toLowerCase().startsWith("autoqueue"))||
				   (menuInput.toLowerCase().startsWith("menuvisible"))){
			String name = menuInput.split(" ")[0];
			menuInput = menuInput.replaceFirst(menuInput.split(" ")[0]+" ", "");
			if (menuInput.equalsIgnoreCase("true")){
				data.getSettings().updateSetting(name, "true");
				System.out.println(name+" value updated");
			} else if (menuInput.equalsIgnoreCase("false")){
				data.getSettings().updateSetting(name, "false");
				System.out.println(name+" value updated");
			} else
				System.out.println("Error: Invalid user input.");
		} else if (menuInput.toLowerCase().startsWith("defaultdirectory")){
			String name = menuInput.split(" ")[0];
			menuInput = menuInput.replaceFirst(menuInput.split(" ")[0]+" ", "");
			
			File directory = new File(menuInput);
			if ((directory.exists())&&(directory.isDirectory())){
				data.getSettings().updateSetting(name, menuInput);
				System.out.println(name+" value updated");
			} else
				System.out.println("Error: Invalid directory.");
		} else if (menuInput.toLowerCase().startsWith("destination")){
			menuInput = menuInput.replaceFirst(menuInput.split(" ")[0]+" ", "");
			
			if ((menuList.size()>0)&&(menuList.get(menuList.size()-1).name.equalsIgnoreCase("selectedDownload"))){
				if (!menuInput.toLowerCase().startsWith("destination")){
					CLDownloadSelectedMenu downloadMenu = (CLDownloadSelectedMenu)mainMenu.findSubmenu("downloadSelected_menu");
					downloadMenu.changeDirectory(null,menuInput);
				}
			} else {
				String checkForDownload = menuInput.split(" ")[0];
				if ((checkForDownload.length()>0)&&(checkForDownload.length()<3)){
					menuInput = menuInput.replaceFirst(menuInput.split(" ")[0]+" ", "");
					int selection = mainMenu.convertCharToNumber(checkForDownload);
					URLDownload urlDownload = data.getUrlDownloads().getDownloads().get(selection);
					CLDownloadSelectedMenu downloadMenu = (CLDownloadSelectedMenu)mainMenu.findSubmenu("downloadSelected_menu");
					downloadMenu.changeDirectory(urlDownload,menuInput);
				} else
					System.out.println("Error: No download selected.");
			}
		} else if (menuInput.toLowerCase().startsWith("podcast")){
			// This is used for changing the podcast download directory
			menuInput = menuInput.replaceFirst(menuInput.split(" ")[0]+" ", "");
			if (menuInput.toLowerCase().startsWith("directory")){
				menuInput = menuInput.replaceFirst(menuInput.split(" ")[0]+" ", "");
				
				if ((!menuInput.toLowerCase().startsWith("directory"))&&
					(menuList.isValidSetting("selectedPodcast"))){
					CLPodcastSelectedMenu podcastSelectedMenu = (CLPodcastSelectedMenu)mainMenu.findSubmenu("podcast_selected");
					Podcast podcast = podcastSelectedMenu.getSelectedPodcast();
					podcastSelectedMenu.changeDirectory(podcast, menuInput);
				} else
					System.out.println("Error: Invalid user input.");
			}
		} else
			System.out.println("Error: Invalid user input.");*/
	}

}
