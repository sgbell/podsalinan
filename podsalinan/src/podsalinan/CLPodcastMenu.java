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
		addSubmenu(new CLPodcastSelectedMenu(menuList,podcasts));
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
		if (menuList.size()==1)
			switch (userInputInt){
				case 9:
					menuList.clear();
					break;
			}
		else {
			// go through the submenus
		}
		super.process(userInputInt);
	}

	public Vector<Podcast> getPodcasts() {
		return podcasts;
	}

	public void setPodcasts(Vector<Podcast> podcasts) {
		this.podcasts = podcasts;
	}
	
	public void process(String userInput){
		if (menuList.size()==1){
			if (userInput.length()<3){
				int podcastNumber=convertCharToNumber(userInput);
				
				if ((podcastNumber>podcasts.size())&&
					(podcastNumber<0))
					System.out.println("Error: Invalid Podcast");
				else{
					menuList.addSetting("podcast", Integer.toString(podcastNumber));
					
				}
			}
		}
	}
}
