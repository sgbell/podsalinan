/**
 * 
 */
package podsalinan;

import java.io.File;

/**
 * @author sbell
 *
 */
public class CLEpisodeMenu extends CLMenu {

	private Episode episode;
	private Podcast podcast;
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
			System.out.println ("Podcast: "+podcast.getName());
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

	public void setEpisode(Episode episode, Podcast selectedPodcast) {
		setPodcast(selectedPodcast);
		this.episode = episode;
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

	private void cancelDownloadEpisode() {
		urlDownloads.cancelDownload(urlDownloads.findDownload(episode.getURL()));
		// urlDownload.removeDownload will do all of the podcast status alterations, so that it is uniform and
		// in the 1 place.
	}

	private void deleteEpisodeFromDrive() {
		System.out.println("Local File: "+podcast.getDirectory()+episode.getURL().getFile());
		//File localFile = new File(podcast.getDirectory()+);
	}

	private void downloadEpisode() {
		episode.setStatus(Details.CURRENTLY_DOWNLOADING);
		urlDownloads.addDownload(episode.getURL(), podcast, episode.getSize());
	}

	/**
	 * @return the podcast
	 */
	public Podcast getPodcast() {
		return podcast;
	}

	/**
	 * @param podcast the podcast to set
	 */
	public void setPodcast(Podcast podcast) {
		this.podcast = podcast;
	}

	public void process(int userInput){
		//System.out.println("CLEpisodeMenu.process(int)");
		if (menuList.size()==3){
			switch (userInput){
				case 1:
					// Download the episode
					downloadEpisode();
					break;
				case 2:
					// Delete the episode from the drive
					deleteEpisodeFromDrive();
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
			userInput=-1000;
		}
		super.process(userInput);
	}
	
	public void process(String userInput){
		//System.out.println("CLEpisodeMenu.process(String)");
		super.process(userInput);
	}
}
