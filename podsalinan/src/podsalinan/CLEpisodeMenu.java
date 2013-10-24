/**
 * 
 */
package podsalinan;

/**
 * @author sbell
 *
 */
public class CLEpisodeMenu extends CLMenu {

	private Episode episode;
	private String podcastName;
	
	/**
	 * @param newMenuList
	 * @param newMenuName
	 */
	public CLEpisodeMenu(ProgSettings newMenuList) {
		super(newMenuList, "episode_selected");
		String[] menuList = {"1. Download episode",
							 "2. Delete episode from Drive",
							 "3. Cancel download of episode",
							 "",
							 "9. Return to Podcast menu"};
		setMainMenuList(menuList);
	}
	
	public void printMainMenu(){
		if (episode!=null){
			System.out.println ("Podcast: "+getPodcastName());
			System.out.println ("Episode: "+episode.getTitle());
			System.out.println ("Date: "+episode.getDate());
			switch (episode.getStatus()){
				case Episode.NOT_STARTED:
					System.out.println ("Status: Not Downloaded");
					break;
				case Episode.CURRENTLY_DOWNLOADING:
				case Episode.PREVIOUSLY_STARTED:
					System.out.println ("Status: Queued to Download");
					break;
				case Episode.FINISHED:
					System.out.println ("Status: Completed Download");
					break;
			}
			super.printMainMenu();
		}
	}

	public Episode getEpisode() {
		return episode;
	}

	public void setEpisode(Episode episode, String podcastName) {
		this.episode = episode;
		setPodcastName(podcastName);
	}

	public String getPodcastName() {
		return podcastName;
	}

	public void setPodcastName(String podcastName) {
		this.podcastName = podcastName;
	}
}
