/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ObjectCall;
import com.mimpidev.podsalinan.cli.options.episode.SelectEpisode;

/**
 * @author sbell
 *
 */
public class SelectCommand extends CLIOption {

	public SelectCommand(DataStorage newData, ObjectCall returnObject) {
		super(newData,returnObject);
		options.put("episode", new SelectEpisode(newData));
		
	}

	@Override
	public ObjectCall execute(String command) {
		return null;
		/*
		menuInput = menuInput.replaceAll("(?i)select ", "");
		if (menuInput.toLowerCase().startsWith("podcast")){
			// remove podcast text at the start (and the space)
			menuInput= menuInput.replaceFirst(menuInput.split(" ")[0]+" ","");
			boolean podcastFound=false;
            int podcastCount=0;
            Podcast podcast=null;
            
            // First while loop for exact matches
            while ((podcastCount<data.getPodcasts().getList().size())&&(!podcastFound)){
            	podcast=data.getPodcasts().getList().get(podcastCount);
				if (((podcast.getName().equalsIgnoreCase(menuInput))||
					(podcast.getDatafile().equalsIgnoreCase(menuInput)))&&
					(!podcast.isRemoved())){
					selectPodcast(podcast);
					podcastFound=true;
				}
            	podcastCount++;
            }
			if (!podcastFound){
				// If the user only entered part of the name we need to give suggestions to the user
				Vector<Podcast> foundPodcasts = new Vector<Podcast>();
				for (Podcast podcastSearch : data.getPodcasts().getList())
					if ((podcastSearch.getName().toLowerCase().contains(menuInput.toLowerCase()))&&
						(!podcastSearch.isRemoved()))
						foundPodcasts.add(podcastSearch);
				if (foundPodcasts.size()==1){
					// if only 1 matching podcast found
					selectPodcast(foundPodcasts.firstElement());
					podcastFound=true;
				} else if (foundPodcasts.size()>1){
		            podcastCount=1;
					// If too many podcasts with text found
					System.out.println ("Matches Found: "+foundPodcasts.size());
					for (Podcast foundPodcast : foundPodcasts){
						System.out.println(getEncodingFromNumber(podcastCount)+". "+foundPodcast.getName());
					    podcastCount++;
					}
					System.out.print("Please select a podcast: ");
					// Ask user to select podcast
					String userInput = input.getStringInput();
					if ((userInput.length()>0)&&(userInput!=null)){
						int selection = mainMenu.convertCharToNumber(userInput);
						if ((selection>=0)&&(selection<foundPodcasts.size())){
							selectPodcast(foundPodcasts.get(selection));
							podcastFound=true;
						} else
							System.out.println("Error: Invalid user input");
					} else 
						System.out.println("Error: Invalid user input");
				} else if (foundPodcasts.size()==0){
					System.out.println("Error: No podcast found matching text("+menuInput+")");
				}
			}
			if ((podcastFound)&&
				(data.getSettings().isValidSetting("menuVisible"))&&
				(data.getSettings().findSetting("menuVisible").equalsIgnoreCase("true"))){
				// not going to go through the menuList array, because we had just set it in selectPodcast(Podcast)
				
				((CLPodcastSelectedMenu)mainMenu.findSubmenu("podcast_selected")).printMainMenu();
			}
		} else if (menuInput.toLowerCase().startsWith("episode")){
			if ((menuList.isValidSetting("mainMenu"))&&
				((menuList.findSetting("mainMenu").equalsIgnoreCase("podcast"))&&
				 (menuList.findSetting("selectedPodcast")!=null))){
				menuInput= menuInput.replaceFirst(menuInput.split(" ")[0]+" ","");
				// Working here next. handling user input to select an episode. my thoughts are,
				// user input will either be the letter assigned in list episodes, or the date
				
				Podcast selectedPodcast = ((CLPodcastSelectedMenu)mainMenu.findSubmenu("podcast_selected")).getSelectedPodcast();
				boolean episodeSelected=false;
				if ((menuInput.length()>0) &&(menuInput!=null)){
					int select = mainMenu.convertCharToNumber(menuInput);
					if ((select>=0)&&(select<selectedPodcast.getEpisodes().size())){
						menuList.add(new MenuPath("selectedEpisode", Integer.toString(select)));
						((CLEpisodeMenu)mainMenu.findSubmenu("episode_selected")).setEpisode(selectedPodcast.getEpisodes().get(select),selectedPodcast);
						episodeSelected = true;
					}
					if (!episodeSelected){
						/* Create a vector of matching dates on episodes. and then let the user select from the list
						 * similar to how selecting a podcast works.
						 */
		/*				Date menuInputDate = convertDate(menuInput);
						//System.out.println("Date: "+menuInputDate.toString());
						Vector<Episode> matchingEpisodes = selectedPodcast.getEpisodesByDate(menuInputDate);
						if (matchingEpisodes.size()==1){
							// Working below. need to find the episode number to add it to selectedEpisode
							//menuList.addSetting("selectedEpisode", Integer.toString(select));
							((CLEpisodeMenu)mainMenu.findSubmenu("episode_selected")).setEpisode(matchingEpisodes.firstElement(),selectedPodcast);
							episodeSelected = true;
						} else
							// Need to deal with more than 1 episode being found, and no episodes found.
							// 14-1-12 = 8 in hak5
							if (matchingEpisodes.size()>1){
					            int episodeCount=1;
								// If too many podcasts with text found
								System.out.println ("Matches Found: "+matchingEpisodes.size());
								for (Episode currentEpisode : matchingEpisodes){
									System.out.println(getEncodingFromNumber(episodeCount)+". "+currentEpisode.getTitle());
								    episodeCount++;
								}
								System.out.print("Please select an episode: ");
								// Ask user to select the episode
								String userInput = input.getStringInput();
								if ((userInput.length()>0)&&(userInput!=null)){
									int selection = mainMenu.convertCharToNumber(userInput);
									if ((selection>=0)&&(selection<matchingEpisodes.size())){
										menuList.add(new MenuPath("selectedEpisode", Integer.toString(selectedPodcast.getEpisodeId(matchingEpisodes.get(selection)))));
										((CLEpisodeMenu)mainMenu.findSubmenu("episode_selected")).setEpisode(matchingEpisodes.get(selection),selectedPodcast);
										episodeSelected=true;
									} else
										System.out.println("Error: Invalid user input");
								} else 
									System.out.println("Error: Invalid user input");
							} else {
								System.out.println("No match found.");
							}
					}
				}
				if ((episodeSelected)&&
						(data.getSettings().isValidSetting("menuVisible"))&&
						(data.getSettings().findSetting("menuVisible").equalsIgnoreCase("true"))){
						// not going to go through the menuList array, because we had just set it in selectPodcast(Podcast)
						CLEpisodeMenu episodeMenu = (CLEpisodeMenu)mainMenu.findSubmenu("episode_selected");
						episodeMenu.printMainMenu();
					}
			}
		} else if (menuInput.toLowerCase().startsWith("download")){
			menuInput= menuInput.replaceFirst(menuInput.split(" ")[0]+" ","");
			// Select download
			int select = mainMenu.convertCharToNumber(menuInput);
			if ((select>=0) && (select<data.getUrlDownloads().getDownloads().size())){
				menuList.clear();
				menuList.add(new MenuPath("mainMenu", "downloads"));
				menuList.add(new MenuPath("selectedDownload", data.getUrlDownloads().getDownloads().get(select).getURL().toString()));
				CLDownloadSelectedMenu dsMenu = (CLDownloadSelectedMenu)mainMenu.findSubmenu("downloadSelected_menu"); 
				dsMenu.setDownload(data.getUrlDownloads().getDownloads().get(select));
				if ((data.getSettings().isValidSetting("menuVisible"))&&
				    (data.getSettings().findSetting("menuVisible").equalsIgnoreCase("true"))){
					dsMenu.printMainMenu();
				}
			}
		} else {
			System.out.println("Error: Invalid User entry.");
		}*/
	}

}
