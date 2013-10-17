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
	
	public CLMainMenu(ArrayList<Setting> parentMenuList) {
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
}
