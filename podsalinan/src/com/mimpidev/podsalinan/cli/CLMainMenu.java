/**
 * 
 */
package com.mimpidev.podsalinan.cli;

import java.util.ArrayList;
import com.mimpidev.podsalinan.data.PodcastList;
import com.mimpidev.podsalinan.data.URLDownloadList;

/**
 * @author bugman
 *
 */
public class CLMainMenu extends CLMenu{
	private PodcastList podcasts;
	private URLDownloadList urlDownloads;
	
	public CLMainMenu(ArrayList<CLInterface.MenuPath> parentMenuList, PodcastList podcastList, URLDownloadList downloadList) {
		super(parentMenuList,"Main Menu");
		
		String[] mainMenuList = {
				"1. Podcasts Menu",
				"2. Downloads Menu",
				"3. Preferences",
				"",
				"4. Quit"};
		setMainMenuList(mainMenuList);
		podcasts = podcastList;
		urlDownloads = downloadList;
	}
	
	public void printMainMenu(){
		System.out.println(podcasts.getList().size()+" - Podcasts. "+urlDownloads.visibleSize()+" - Downloads Queued");

		super.printMainMenu();
	}

	/** process will take the user menu input and will pass it through the menus to the correct menu level and
	 * do as requested.
	 * 
	 * @param inputInt
	 */
	/*
	public void process(int userInputInt) {
		//System.out.println("Debug: CLMainMenu.process("+userInputInt+")");
		// If there are no entries in the menuList, handle the 
		if (menuList.size()==0){
			switch (userInputInt){
				case 1:
					menuList.add(new CLInterface.MenuPath("mainMenu","podcast"));
					break;
				case 2:
					menuList.add(new CLInterface.MenuPath("mainMenu","downloads"));
					break;
				case 3:
					menuList.add(new CLInterface.MenuPath("mainMenu","preferences"));
					break;
				case 99:
					printMainMenu();
			}
			userInputInt=-1000;
		}
		if (menuList.size()>0){
			if (menuList.findSetting("mainMenu").equalsIgnoreCase("podcast")){
				((CLPodcastMenu)findSubmenu(menuList.findSetting("mainMenu"))).process(userInputInt);
			} else if (menuList.findSetting("mainMenu").equalsIgnoreCase("downloads")){
				((CLDownloadMenu)findSubmenu(menuList.findSetting("mainMenu"))).process(userInputInt);
			} else if (menuList.findSetting("mainMenu").equalsIgnoreCase("preferences")){
				((CLPreferencesMenu)findSubmenu(menuList.findSetting("mainMenu"))).process(userInputInt);
			}
		} else {
			userInputInt=-1000;
			super.process(userInputInt);
		}
	}
	
	public void process(String userInput){
		//System.out.println("Debug: CLMainMenu.process(String)");
		if (menuList.size()>0){
			if (menuList.findSetting("mainMenu").equalsIgnoreCase("podcast")){
				((CLPodcastMenu)findSubmenu(menuList.findSetting("mainMenu"))).process(userInput);
			} else if (menuList.findSetting("mainMenu").equalsIgnoreCase("downloads")){
				((CLDownloadMenu)findSubmenu(menuList.findSetting("mainMenu"))).process(userInput);
			} else if (menuList.findSetting("mainMenu").equalsIgnoreCase("preferences")){
				((CLPreferencesMenu)findSubmenu(menuList.findSetting("mainMenu"))).process(userInput);
			}
		} else
			super.process(userInput);
	}*/
}
