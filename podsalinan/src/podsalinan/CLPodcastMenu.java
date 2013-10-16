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
	private Vector<Podcast> podcasts;
	
	/**
	 * 
	 */
	public CLPodcastMenu(ArrayList<Setting> parentMenuList, Vector<Podcast> podcasts) {
		super(parentMenuList, "Podcast Menu");
		this.podcasts = podcasts;
		String[] mainMenuList = {
				"(A-Z) Enter Podcast letter to select Podcast.",
				"",
				"9. Return to Main Menu"};
		setMainMenuList(mainMenuList);
	}
	
	public void printMainMenu(){
		int podcastCount=1;
		
		for (Podcast podcast : podcasts){
			if (!podcast.isRemoved())
				System.out.println(getEncodingFromNumber(podcastCount)+". "+podcast.getName());
			podcastCount++;
		}

		super.printMainMenu();
	}
}
