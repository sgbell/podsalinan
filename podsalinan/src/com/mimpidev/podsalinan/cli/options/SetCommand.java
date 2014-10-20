/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnCall;

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
		// TODO Auto-generated constructor stub
	}

	@Override
	public ReturnCall execute(String command) {
		return null;
		/*
		menuInput = menuInput.replaceFirst(menuInput.split(" ")[0]+" ", "");
		
		if (menuInput.toLowerCase().startsWith("updateinterval")){
			String name = menuInput.split(" ")[0];
			menuInput = menuInput.replaceFirst(menuInput.split(" ")[0]+" ", "");
			int value=-1;
			try {
				value = Integer.parseInt(menuInput);
			} catch (NumberFormatException e){
				value=-1;
			}
			if ((value>0)&&(value<=6)){
				switch (value){
					case 1:
						// 1 Hour
						menuInput="60";
						break;
					case 2:
						//2 Hours
						menuInput="120";
						break;
					case 3:
						// 3 Hours
						menuInput="180";
						break;
					case 4:
						// 6 Hours
						menuInput="360";
						break;
					case 5:
						// 12 Hours
						menuInput="720";
						break;
					case 6:
						// 24 Hours
						menuInput="1440";
						break;
				}
				data.getSettings().updateSetting(name, menuInput);
				System.out.println(name+" value updated");
			} else {
				System.out.println("Error: Invalid user input.");
				System.out.println("Valid values: (1)Hourly, (2)Every 2 Hours, (3)Every 3 Hours, (4)Every 6 Hours, (5)Every 12 Hours, (6)Daily");
			}
		} else if (menuInput.toLowerCase().startsWith("downloadlimit")){
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
