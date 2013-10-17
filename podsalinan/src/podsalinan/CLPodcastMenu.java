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
public class CLPodcastMenu extends CLMenu {
	/**
	 * 
	 */
	public CLPodcastMenu(ArrayList<Setting> parentMenuList) {
		super(parentMenuList, "Podcast Menu");
		this.podcasts = podcasts;
		String[] mainMenuList = {
				"(A-Z) Enter Podcast letter to select Podcast.",
				"",
				"9. Return to Main Menu"};
		setMainMenuList(mainMenuList);
	}
	
	public void printMainMenu(){

		super.printMainMenu();
	}
}
