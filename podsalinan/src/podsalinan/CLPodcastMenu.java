/**
 * 
 */
package podsalinan;

import java.util.Vector;

/**
 * @author bugman
 *
 */
public class CLPodcastMenu extends CLMenu implements CLMenuInterface {
	private Vector<Podcast> podcasts;
	
	/**
	 * 
	 */
	public CLPodcastMenu(ProgSettings parentMenuList, Vector<Podcast> newPodcasts) {
		super(parentMenuList, "Podcast Menu");
		String[] mainMenuList = {
				"(A-Z) Enter Podcast letter to select Podcast.",
				"",
				"9. Return to Main Menu"};
		setMainMenuList(mainMenuList);
		setPodcasts(newPodcasts);
	}
	
	public void printMainMenu(){
		System.out.println("Printing list of Podcasts here ----");
		super.printMainMenu();
	}

	@Override
	public void process(int userInputInt) {
		// TODO Auto-generated method stub
		
	}

	public Vector<Podcast> getPodcasts() {
		return podcasts;
	}

	public void setPodcasts(Vector<Podcast> podcasts) {
		this.podcasts = podcasts;
	}
}
