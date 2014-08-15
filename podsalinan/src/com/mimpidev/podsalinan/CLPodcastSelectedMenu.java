/**
 * 
 */
package com.mimpidev.podsalinan;

import java.io.File;

import com.mimpidev.podsalinan.data.Episode;
import com.mimpidev.podsalinan.data.Podcast;
import com.mimpidev.podsalinan.data.ProgSettings;
import com.mimpidev.podsalinan.data.URLDownloadList;


/**
 * @author sbell
 *
 */
public class CLPodcastSelectedMenu extends CLMenu {
    private Podcast selectedPodcast;
    private CLInput input;
	
	/**
	 * @param newMenuList
	 * @param progSettings 
	 * @param urlDownloads 
	 * @param "podcast"
	 */
	public CLPodcastSelectedMenu(ProgSettings newMenuList, URLDownloadList urlDownloads) {
		super(newMenuList, "podcast_selected");
		String[] mainMenuList = {"1. List Episodes",
								 "2. Update List",
								 "3. Delete Podcast",
								 "4. Change Download Directory",
								 "5. Autoqueue Episodes",
								 "<AA>. Select Episode",
								 "",
								 "9. Return to List of Podcasts"};
		setMainMenuList(mainMenuList);
		input = new CLInput();
		addSubmenu(new CLEpisodeMenu(menuList,urlDownloads));
	}

	public void printMainMenu(){
		if ((selectedPodcast!=null)&&(selectedPodcast.getDatafile().equalsIgnoreCase(menuList.findSetting("selectedPodcast")))){
			System.out.println("Podcast: "+selectedPodcast.getName()+ " - Selected");
			super.printMainMenu();
		}
	}
	
	public void printDetails(Podcast podcast){
		if (podcast==null)
			podcast = selectedPodcast;
		
		System.out.println("Podcast: "+podcast.getName());
		System.out.println("URL: "+podcast.getURL());
		System.out.println("Local Directory: "+podcast.getDirectory());
	}

	public Podcast getSelectedPodcast() {
		return selectedPodcast;
	}

	public void setSelectedPodcast(Podcast selectedPodcast) {
		this.selectedPodcast = selectedPodcast;
	}
	
	public void printEpisodeList() {
		System.out.println ();
		int epCount=1;
		
		if (selectedPodcast!=null){
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
	}

	public boolean changeDirectory(Podcast podcast, String userInput){
		File newPath;
		if ((userInput.length()>0)&&(userInput!=null)){
			newPath=new File(userInput);
			if ((newPath.exists())&&(newPath.isDirectory())){
				podcast.setDirectory(userInput);
				System.out.println("Podcast Download Directory: "+podcast.getDirectory());
				return true;
			} else if ((newPath.getParentFile().exists())&&
					   (newPath.getParentFile().isDirectory())){
				System.out.println("Error: Directory does no exist.");
				if (input.confirmCreation()){
					newPath.mkdir();
					System.out.println("Podcast Download Directory Created: "+newPath);
					podcast.setDirectory(userInput);
				}
			} else {
				System.out.println ("Error: Invalid path");
			}
		}
		return false;
	}
	
	public void process(int userInputInt){
		//System.out.println("CLPodcastSelectedMenu.process(int)");
		//System.out.println("menuList.size(): "+menuList.size());
		if (menuList.size()==2){
			switch (userInputInt){
			    case 1:
			    	printEpisodeList();
			    	printMainMenu();
			    	break;
			    case 2:
			    	if (selectedPodcast!=null){
			    		String tempDir=null;
						if (System.getProperty("os.name").startsWith("Windows"))
							tempDir = System.getProperty("user.home").concat("\\appdata\\local\\podsalinan");
						else
							tempDir = System.getProperty("user.home").concat("/.podsalinan");

						if (tempDir!=null)
			    			selectedPodcast.updateList(tempDir,true);
			    	}
			    	break;
			    case 3:
			    	if(input.confirmRemoval()){
			    		selectedPodcast.setRemove(true);
			    		setSelectedPodcast(null);
			    		menuList.removeSetting("selectedPodcast");
			    	}
			    	break;
			    case 4:
					System.out.println ();
					System.out.print ("Enter Podcast Download Directory["+selectedPodcast.getDirectory()+"]: ");
					String userInput=input.getStringInput();
			    	changeDirectory(selectedPodcast,userInput);
			    	break;
			    case 5:
			    	System.out.println ();
			    	if (selectedPodcast.isAutomaticQueue()){
			    		selectedPodcast.setAutomaticQueue(false);
			    		System.out.println(selectedPodcast.getName()+" podcast autoqueue disabled.");
			    	}else{
			    		selectedPodcast.setAutomaticQueue(true);
			    		System.out.println(selectedPodcast.getName()+"podcast autoqeue enabled.");
			    	}
			    	break;
				case 9:
					setSelectedPodcast(null);
					menuList.removeSetting("selectedPodcast");
					break;
				case 98:
					printDetails(null);
					break;
				case 99:
					printMainMenu();
			}
		}
		if (menuList.size()>2){
			if (menuList.findSetting("selectedEpisode")!=null){
				((CLEpisodeMenu)findSubmenu("episode_selected")).process(userInputInt);
			}
		}
		if (menuList.size()==2){
			userInputInt=-1000;
			//System.out.println("CLPodcastSelectedMenu.process(int) calling super.process(int)");
			super.process(userInputInt);
		}
	}

	public void process(String userInput){
		//System.out.println("CLPodcastSelectedMenu.process(String)");
		// If user enters code for an episode
		if ((menuList.size()==2)&&(userInput!=null)){
			if (userInput.length()<3){
				int episodeNumber=convertCharToNumber(userInput);
					
				if ((episodeNumber>selectedPodcast.getEpisodes().size())&&
					(episodeNumber<0))
					System.out.println("Error: Invalid Episode");
				else {
					menuList.addSetting("selectedEpisode", Integer.toString(episodeNumber));
					((CLEpisodeMenu)findSubmenu("episode_selected")).setEpisode(selectedPodcast.getEpisodes().get(episodeNumber),selectedPodcast);
				}
				userInput=null;
			}
		}
		if (menuList.size()>2){
			if (menuList.findSetting("selectedEpisode")!=null){
				((CLEpisodeMenu)findSubmenu("episode_selected")).process(userInput);
			}
		}
		if (menuList.size()==2){
			userInput=null;
			super.process(userInput);
		}
	}
}
