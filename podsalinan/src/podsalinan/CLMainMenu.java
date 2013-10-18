/**
 * 
 */
package podsalinan;

import java.util.ArrayList;
import java.util.Vector;

/**
 * @author bugman
 *
 */
public class CLMainMenu extends CLMenu {
	
	public CLMainMenu(ProgSettings parentMenuList) {
		super(parentMenuList,"Main Menu");
		
		String[] mainMenuList = {
				"1. Podcasts Menu",
				"2. Downloads Menu",
				"3. Preferences",
				"4. Quit"};
		setMainMenuList(mainMenuList);
	}
	
	public void printMainMenu(){
		super.printMainMenu();
	}

	/** process will take the user menu input and will pass it through the menus to the correct menu level and
	 * do as requested.
	 * 
	 * @param inputInt
	 */
	public void process(int userInputInt) {
	   /**  
		 *  mainMenu.process(user_input){
		 *  	if (menuList.size = 0){
		 *  		switch (user_input){
		 *  			case 1:
		 *  				new MenuList item: name = mainMenu; value = "pocast"
		 *  			case 2:
		 *  				new MenuList item: name = mainMenu; value = "downloads"
		 *  			case 3:
		 *  				new MenuList item: name = mainMenu; value = "preferences"
		 *  		}
		 *  		user_input="";
		 * 		}
		 *  	if (menuList.getByName("mainMenu")=="podcast")
		 *  		podcastMenu(userInput);
		 *  	else if (menuList.getByName("mainMenu")=="downloads")
		 *  		downloadsMenu(userInput);
		 *  	else if (menuList.getByName("mainMenu")=="preferences")
		 *  		preferencesMenu(userInput);
		 *  }
		 */
		if (menuList.size()==0){
			switch (userInputInt){
				case 1:
					menuList.addSetting("mainMenu","podcast");
					break;
				case 2:
					menuList.addSetting("mainMenu","podcast");
					break;
				case 3:
					menuList.addSetting("mainMenu","podcast");
					break;
			}
			userInputInt=-1000;
		}
						
	}
}
