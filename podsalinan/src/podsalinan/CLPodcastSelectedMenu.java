/**
 * 
 */
package podsalinan;

import java.util.Vector;

/**
 * @author sbell
 *
 */
public class CLPodcastSelectedMenu extends CLMenu {
    private Podcast selectedPodcast;
	
	/**
	 * @param newMenuList
	 * @param "podcast"
	 */
	public CLPodcastSelectedMenu(ProgSettings newMenuList) {
		super(newMenuList, "podcast_selected");
		String[] mainMenuList = {"1. List Episodes",
								 "2. Update List",
								 "3. Remove Podcast",
								 "<AA>. Select Episode",
								 "",
								 "9. Return to List of Podcasts"};
		setMainMenuList(mainMenuList);
	}

	public void printMainMenu(){
		if ((selectedPodcast!=null)&&(selectedPodcast.getDatafile().equalsIgnoreCase(menuList.findSetting("selectedPodcast").value))){
			System.out.println("Podcast: "+selectedPodcast.getName()+ " - Selected");
			super.printMainMenu();
		}
	}

	public Podcast getSelectedPodcast() {
		return selectedPodcast;
	}

	public void setSelectedPodcast(Podcast selectedPodcast) {
		this.selectedPodcast = selectedPodcast;
	}
	
	public void process(String userInput){
		super.process(userInput);
	}

	
	
	public void process(int userInputInt){
		System.out.println("menuList.size: "+menuList.size());
		if (menuList.size()==2){
			switch (userInputInt){
			    case 1:
			    	break;
			    case 2:
			    	break;
			    case 3:
			    	break;
				case 9:
					break;
			}
		}
		super.process(userInputInt);
	}
}
