/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import com.mimpidev.podsalinan.CLDownloadSelectedMenu;
import com.mimpidev.podsalinan.CLEpisodeMenu;
import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.data.URLDownload;

/**
 * @author sbell
 *
 */
public class RestartCommand extends CLIOption {

	/**
	 * @param newData
	 */
	public RestartCommand(DataStorage newData) {
		super(newData);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public void execute(String command) {
		menuInput = menuInput.replaceFirst(menuInput.split(" ")[0]+" ", "");
		if (((menuInput.equalsIgnoreCase("delete"))||(menuInput.equalsIgnoreCase("episode")))&&
			(menuList.get(menuList.size()-1).name.equalsIgnoreCase("selectedEpisode"))){
			CLEpisodeMenu episodeMenu = (CLEpisodeMenu)mainMenu.findSubmenu("episode_selected");
			episodeMenu.deleteEpisodeFromDrive();
			System.out.println("Deleting file for episode: "+episodeMenu.getEpisode().getTitle());
		} else if (menuList.get(menuList.size()-1).name.equalsIgnoreCase("downloads")){
			if (menuList.get(menuList.size()-1).name.equalsIgnoreCase("selectedDownload")){
				URLDownload download = ((CLDownloadSelectedMenu)mainMenu.findSubmenu("downloadSelected_menu")).getDownload();
				data.getUrlDownloads().deleteDownload(data.getUrlDownloads().findDownload(download.getURL()));
				System.out.println("Deleting file for download: "+download.getURL().toString());
			}
		}		
	}

}
