/**
 * 
 */
package podsalinan;


/**
 * @author sbell
 *
 */
public class CLPodcastSelectedMenu extends CLMenu {
    private Podcast selectedPodcast;
    private CLInput input;
	
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
		input = new CLInput();
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
	
	private void printEpisodeList() {
		System.out.println ();
		int epCount=1;
		
		synchronized (selectedPodcast.getEpisodes()){
			for (Episode episode : selectedPodcast.getEpisodes()){
				System.out.println (getEncodingFromNumber(epCount)+" - " +
						episode.getTitle()+" : "+episode.getDate());
				epCount++;
				if ((epCount%20)==0){
					System.out.println("-- Press any key to continue, q to quit --");
					char charInput=input.getSingleCharInput();
					if (charInput=='q')
						break;
				}
			}
		}
	}

	public void process(int userInputInt){
		if (menuList.size()==2){
			switch (userInputInt){
			    case 1:
			    	printEpisodeList();
			    	printMainMenu();
			    	break;
			    case 2:
			    	// Update Podcast
			    	break;
			    case 3:
			    	// Delete Podcast
			    	break;
				case 9:
					menuList.removeSetting("selectedPodcast");
					break;
			}
		}
		super.process(userInputInt);
	}

	public void process(String userInput){
		super.process(userInput);
	}
}
