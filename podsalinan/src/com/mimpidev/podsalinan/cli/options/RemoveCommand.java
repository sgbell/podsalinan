/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLDownloadSelectedMenu;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnCall;
import com.mimpidev.podsalinan.data.Podcast;
import com.mimpidev.podsalinan.data.URLDownload;

/**
 * @author sbell
 *
 */
public class RemoveCommand extends CLIOption {

	/**
	 * @param newData
	 */
	public RemoveCommand(DataStorage newData) {
		super(newData);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ReturnCall execute(String command) {
		String menuInput = command.replaceFirst(command.split(" ")[0]+" ", "");
		return null;
		
		/*
		if (menuInput.equalsIgnoreCase("remove")){
			if ((menuList.size()>0)&&
			    (menuList.get(menuList.size()-1).name.equalsIgnoreCase("selectedDownload"))){
				// remove the download
				CLDownloadSelectedMenu cldsmenu = (CLDownloadSelectedMenu)mainMenu.findSubmenu("downloadSelected_menu");
				cldsmenu.printDetails(null,true);

				if (input.confirmRemoval()){
					data.getUrlDownloads().deleteDownload(cldsmenu.getDownload());
					System.out.println("Download deleted.");
				}
			} else if (menuList.get(menuList.size()-1).name.equalsIgnoreCase("selectedPodcast")){
				// remove the podcast from the system
				CLPodcastSelectedMenu clpsmenu = (CLPodcastSelectedMenu)mainMenu.findSubmenu("podcast_selected");
				clpsmenu.printDetails(null);
				
				if (input.confirmRemoval()){
					clpsmenu.getSelectedPodcast().setRemove(true);
					for (URLDownload download : data.getUrlDownloads().getDownloads()){
						if (download.getPodcastId().equalsIgnoreCase(clpsmenu.getSelectedPodcast().getDatafile())){
							download.setPodcastId("");
						}
					}
					System.out.println("Podcast deleted.");
				}
			} else
				System.out.println("Error: No Download or Podcast selected");
		} else if (menuInput.startsWith("download")){
			menuInput = menuInput.replaceFirst(menuInput.split(" ")[0]+" ", "");
			
			if (menuInput.equalsIgnoreCase("download")){
				if ((menuList.size()>0)&&
					(menuList.get(menuList.size()-1).name.equalsIgnoreCase("selectedDownload"))){
					CLDownloadSelectedMenu cldsmenu = (CLDownloadSelectedMenu)mainMenu.findSubmenu("downloadSelected_menu");
					cldsmenu.printDetails(null,true);
					if (input.confirmRemoval()){
						data.getUrlDownloads().deleteDownload(cldsmenu.getDownload());
						System.out.println("Download deleted.");
					}
				} else 
					System.out.println("Error: Invalid user input");
			} else {
				if ((menuInput.length()>0)&&(menuInput.length()<3)){
					int select = mainMenu.convertCharToNumber(menuInput);
					CLDownloadSelectedMenu cldsmenu = (CLDownloadSelectedMenu)mainMenu.findSubmenu("downloadSelected_menu");
					cldsmenu.printDetails(data.getUrlDownloads().getDownloads().get(select), true);
					if (input.confirmRemoval()){
						data.getUrlDownloads().deleteDownload(select);
						System.out.println("Download deleted.");
					}
				} else 
					System.out.println("Error: Invalid user input");
			}
		} else if (menuInput.startsWith("podcast")){
			menuInput = menuInput.replaceFirst(menuInput.split(" ")[0]+" ", "");
			
			if (menuInput.equalsIgnoreCase("podcast")){
				if ((menuList.size()>0)&&
				    (menuList.get(menuList.size()-1).name.equalsIgnoreCase("selectedPodcast"))){
					// remove the podcast from the system
					CLPodcastSelectedMenu clpsmenu = (CLPodcastSelectedMenu)mainMenu.findSubmenu("podcast_selected");
					clpsmenu.printDetails(null);
					if (input.confirmRemoval()){
						clpsmenu.getSelectedPodcast().setRemove(true);
						for (URLDownload download : data.getUrlDownloads().getDownloads()){
							if (download.getPodcastId().equalsIgnoreCase(clpsmenu.getSelectedPodcast().getDatafile())){
								download.setPodcastId("");
							}
						}
						System.out.println("Podcast deleted.");
					}
				} else {
					int podcastCount=0;
					boolean podcastFound=false;
					Podcast podcast=null;
					
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
		            
		            if (podcastFound){
						CLPodcastSelectedMenu clpsmenu = (CLPodcastSelectedMenu)mainMenu.findSubmenu("podcast_selected");
						clpsmenu.printDetails(podcast);
						if (input.confirmRemoval()){
							podcast.setRemove(true);
							for (URLDownload download : data.getUrlDownloads().getDownloads()){
								if (download.getPodcastId().equalsIgnoreCase(podcast.getDatafile())){
									download.setPodcastId("");
								}
							}
							System.out.println("Podcast deleted.");
		            	}
		            } else {
						System.out.println("Error: Invalid user input");
		            }
				}
			} else 
				System.out.println("Error: Invalid user input");
		} else if ((menuInput.length()>0)&&(menuInput.length()<3)){
			URLDownload download=null;
			int select = mainMenu.convertCharToNumber(menuInput);
			if ((select>=0)&&(select<data.getUrlDownloads().size())){
				download = data.getUrlDownloads().getDownloads().get(select);

				CLDownloadSelectedMenu cldsmenu = (CLDownloadSelectedMenu)mainMenu.findSubmenu("downloadSelected_menu");
				cldsmenu.printDetails(download, true);
				if (input.confirmRemoval()){
					data.getUrlDownloads().deleteDownload(select);
					System.out.println("Download deleted.");
				}
			} else
				System.out.println("Error: Invalid user input");
		} else
			System.out.println("Error: Invalid user input");
			*/
	}

}
