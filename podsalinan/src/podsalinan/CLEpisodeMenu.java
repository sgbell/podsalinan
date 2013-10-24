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
	private URLDownloadList urlDownloads;
	
	/**
	 * @param newMenuList
	 * @param urlDownloads 
	 * @param newMenuName
	 */
	public CLEpisodeMenu(ProgSettings newMenuList, URLDownloadList urlDownloads) {
		super(newMenuList, "episode_selected");
		String[] menuList = {"1. Download episode",
							 "2. Delete episode from Drive",
							 "3. Cancel download of episode",
							 "",
							 "9. Return to Podcast menu"};
		setMainMenuList(menuList);
		setUrlDownloads(urlDownloads);
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
					System.out.println ("Status: Download Incomplete");
					break;
				case Episode.FINISHED:
					System.out.println ("Status: Completed Download");
					break;
			}
			super.printMainMenu();
		} else {
			System.out.println("Error: Invalid Episode");
			menuList.removeSetting("selectedEpisode");
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
	
	/**
	 * @return the urlDownloads
	 */
	public URLDownloadList getUrlDownloads() {
		return urlDownloads;
	}

	/**
	 * @param urlDownloads the urlDownloads to set
	 */
	public void setUrlDownloads(URLDownloadList urlDownloads) {
		this.urlDownloads = urlDownloads;
	}

	public void process(int userInput){
		if (menuList.size()==3){
			switch (userInput){
				case 1:
					// Download the episode
					downloadEpisode();
					break;
				case 2:
					// Delete the episode from the drive
					deleteEpisode();
					break;
				case 3:
					// Cancel download of the episode
					cancelDownloadEpisode();
					break;
				case 9:
					// Exit episode menu, clearing the selected episode information
					setEpisode(null,null);
					menuList.removeSetting("selectedEpisode");
					break;
			}
		}
		super.process(userInput);
	}
	
	private void cancelDownloadEpisode() {
		// TODO Auto-generated method stub
		
	}

	private void deleteEpisode() {
		// TODO Auto-generated method stub
		
	}

	private void downloadEpisode() {
		// TODO Auto-generated method stub
		
	}

	public void process(String userInput){
		super.process(userInput);
	}
}
