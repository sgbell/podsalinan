/**
 * 
 */
package podsalinan;

import java.util.Vector;

/**
 * @author bugman
 *
 */
public class CLPodcastMenu extends CLMenu{
	private Vector<Podcast> podcasts;
	
	/**
	 * 
	 */
	public CLPodcastMenu(ProgSettings parentMenuList, Vector<Podcast> newPodcasts) {
		super(parentMenuList, "podcast");
		String[] mainMenuList = {
				"(A-Z) Enter Podcast letter to select Podcast.",
				"",
				"9. Return to Main Menu"};
		setMainMenuList(mainMenuList);
		setPodcasts(newPodcasts);
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

	public void process(int userInputInt) {
		switch (userInputInt){
			case 9:
				menuList.clear();
				break;
		}
		super.process(userInputInt);
	}

	public Vector<Podcast> getPodcasts() {
		return podcasts;
	}

	public void setPodcasts(Vector<Podcast> podcasts) {
		this.podcasts = podcasts;
	}
}
