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
	 * @param settings 
	 * @param urlDownloads 
	 * 
	 */
	public CLPodcastMenu(ProgSettings parentMenuList, Vector<Podcast> newPodcasts, ProgSettings settings, URLDownloadList urlDownloads) {
		super(parentMenuList, "podcast");
		String[] mainMenuList = {
				"(A-Z) Enter Podcast letter to select Podcast.",
				"",
				"9. Return to Main Menu"};
		setMainMenuList(mainMenuList);
		setPodcasts(newPodcasts);
		addSubmenu(new CLPodcastSelectedMenu(menuList, settings, urlDownloads));
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

	public Vector<Podcast> getPodcasts() {
		return podcasts;
	}

	public void setPodcasts(Vector<Podcast> podcasts) {
		this.podcasts = podcasts;
	}
	
	public void process(int userInputInt) {
		//System.out.println("Debug: CLPodcastMenu.process("+userInputInt+")");
		if (menuList.size()==1)
			switch (userInputInt){
				case 9:
					menuList.clear();
					break;
			}
		if (menuList.size()>1)
			((CLPodcastSelectedMenu)findSubmenu("podcast_selected")).process(userInputInt);
		if (menuList.size()==1){
			userInputInt=-1000;
			super.process(userInputInt);
		}
	}

	public void process(String userInput){
		//System.out.println("Debug: CLPodcastMenu.process(String)");
		if (menuList.size()==1){
			if (userInput.length()<3){
				int podcastNumber=convertCharToNumber(userInput);
				
				if ((podcastNumber>podcasts.size())&&
					(podcastNumber<0))
					System.out.println("Error: Invalid Podcast");
				else{
					menuList.addSetting("selectedPodcast", podcasts.get(podcastNumber).getDatafile());
					((CLPodcastSelectedMenu)findSubmenu("podcast_selected")).setSelectedPodcast(podcasts.get(podcastNumber));
					System.out.println("Selected Podcast: "+podcasts.get(podcastNumber).getName());
				}
				userInput=null;
			}
		}
		if (menuList.size()>1){
			if (menuList.findSetting("selectedPodcast")!=null){
				((CLPodcastSelectedMenu)findSubmenu("podcast_selected")).process(userInput);
			}
		} else
		    super.process(userInput);
	}
}
