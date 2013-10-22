/**
 * 
 */
package podsalinan;

import java.util.Vector;

/**
 * @author bugman
 *
 */
public class CLMainMenu extends CLMenu{
	private Vector<Podcast> podcasts;
	private URLDownloadList urlDownloads;
	
	public CLMainMenu(ProgSettings parentMenuList, Vector<Podcast> podcastList, URLDownloadList downloadList) {
		super(parentMenuList,"Main Menu");
		
		String[] mainMenuList = {
				"1. Podcasts Menu",
				"2. Downloads Menu",
				"3. Preferences",
				"4. Quit"};
		setMainMenuList(mainMenuList);
		podcasts = podcastList;
		urlDownloads = downloadList;
	}
	
	public void printMainMenu(){
		System.out.println(podcasts.size()+" - Podcasts. "+urlDownloads.getDownloads().size()+" - Downloads Queued");

		super.printMainMenu();
	}

	/** process will take the user menu input and will pass it through the menus to the correct menu level and
	 * do as requested.
	 * 
	 * @param inputInt
	 */
	public void process(int userInputInt) {
		// If there are no entries in the menuList, handle the 
		if (menuList.size()==0){
			switch (userInputInt){
				case 1:
					menuList.addSetting("mainMenu","podcast");
					break;
				case 2:
					menuList.addSetting("mainMenu","downloads");
					break;
				case 3:
					menuList.addSetting("mainMenu","preferences");
					break;
			}
			userInputInt=-1000;
		}
		if (menuList.size()>0){
			if (menuList.findSetting("mainMenu").value.equalsIgnoreCase("podcast")){
				((CLPodcastMenu)findSubmenu(menuList.findSetting("mainMenu").value)).process(userInputInt);
			} else if (menuList.findSetting("mainMenu").value.equalsIgnoreCase("downloads")){
				((CLDownloadMenu)findSubmenu(menuList.findSetting("mainMenu").value)).process(userInputInt);
			} else if (menuList.findSetting("mainMenu").value.equalsIgnoreCase("preferences")){
				((CLPreferencesMenu)findSubmenu(menuList.findSetting("mainMenu").value)).process(userInputInt);
			}
		} else
			System.out.println("Error: Invalid user entry.");
	}
}
