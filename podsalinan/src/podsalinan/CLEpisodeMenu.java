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
	private CLInput input;
	
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
							 "4. Change Status",
							 "",
							 "9. Return to Podcast menu"};
		setMainMenuList(menuList);
		setUrlDownloads(urlDownloads);
		input = new CLInput();
	}
	
	public void printMainMenu(){
		if (episode!=null){
			printDetails();
			super.printMainMenu();
		}
	}

	public void printDetails() {
		System.out.println ("Podcast: "+podcast.getName());
		System.out.println ("Episode: "+episode.getTitle());
		System.out.println ("Date: "+episode.getDate());
		System.out.println ("Status: "+getStatusString(episode));
	}
	
	public String getStatusString(Episode episode){
		if (episode==null){
			episode=this.episode;
		}
		String status=null;
		
		switch (episode.getStatus()){
			case Episode.NOT_QUEUED:
				status = "Not Downloaded";
				break;
			case Episode.CURRENTLY_DOWNLOADING:
				status = "Downloading Currently";
				break;
			case Episode.DOWNLOAD_QUEUED:
				status = "Download Queued";
				break;
			case Episode.INCOMPLETE_DOWNLOAD:
				status = "Download Incomplete";
				break;
			case Episode.FINISHED:
				status = "Completed Download";
				break;
		}
		
		return status;
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

	public void cancelDownloadEpisode() {
		urlDownloads.cancelDownload(urlDownloads.findDownload(episode.getURL()));
		// urlDownload.removeDownload will do all of the podcast status alterations, so that it is uniform and
		// in the 1 place.
	}

	public void deleteEpisodeFromDrive() {
		// Added a new method to podcast, if we already have the episode instance
		podcast.deleteEpisodeFromDrive(episode);
	}

	public void downloadEpisode() {
		episode.setStatus(Details.CURRENTLY_DOWNLOADING);
		urlDownloads.addDownload(episode, podcast);
	}

	/** This method is used to change the episode status, via user input
	 *  
	 * @param statusInput
	 */
	public void changeStatus(Episode episode, String statusInput) {
		//TODO: code changeStatus
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
				case 4:
				    // Change episode status
					System.out.println ();
					
					//TODO list all status types.
					System.out.print ("Enter Download Destination["+getStatusString(null)+"]: ");
					String statusInput=input.getValidNumber(0,7);
					if (statusInput!=null)
						changeStatus(null, statusInput);
				case 9:
					// Exit episode menu, clearing the selected episode information
					setEpisode(null,null);
					menuList.removeSetting("selectedEpisode");
					break;
				case 98:
					printDetails();
					break;
				case 99:
					printMainMenu();
			}
			userInput=-1000;
		}
		//System.out.println("CLEpisodeMenu.process(int) calling super.process(int)");
		super.process(userInput);
	}

	public void process(String userInput){
		//System.out.println("CLEpisodeMenu.process(String)");
		super.process(userInput);
	}
}
