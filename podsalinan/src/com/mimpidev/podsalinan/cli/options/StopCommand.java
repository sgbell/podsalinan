/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnCall;

/**
 * @author sbell
 *
 */
public class StopCommand extends CLIOption {

	/**
	 * @param newData
	 */
	public StopCommand(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnCall execute(String command) {
		//TODO: Flesh out StopCommand
		return null;
		
		/*
		URLDownload download=null;
		// Grab the selected download and call the method below
		menuInput = menuInput.replaceFirst(menuInput.split(" ")[0]+" ", "");
		if (menuInput.equalsIgnoreCase("stop")){
			download = ((CLDownloadSelectedMenu)mainMenu.findSubmenu("downloadSelected_menu")).getDownload();
		} else if (menuInput.toLowerCase().startsWith("download")){
			menuInput = menuInput.replaceFirst(menuInput.split(" ")[0]+" ", "");
			
			if ((menuInput.length()>0)&&(menuInput.length()<3)){
				int select = mainMenu.convertCharToNumber(menuInput);
				if ((select>=0)&&(select<data.getUrlDownloads().size()))
					download = data.getUrlDownloads().getDownloads().get(select);
			} else {
				if (menuInput.equalsIgnoreCase("download")){
					download = ((CLDownloadSelectedMenu)mainMenu.findSubmenu("downloadSelected_menu")).getDownload();
				}
			}
		} else if ((menuInput.length()>0)&&(menuInput.length()<3)){
			int select = mainMenu.convertCharToNumber(menuInput);
			if ((select>=0)&&(select<data.getUrlDownloads().size()))
				download = data.getUrlDownloads().getDownloads().get(select);
		}
		if (download!=null){
			// Stop the download
			data.getUrlDownloads().cancelDownload(download);
			((CLDownloadSelectedMenu)mainMenu.findSubmenu("downloadSelected_menu")).printDetails(download,true);
		} else {
			System.out.println("Error: Invalid user input.");
		}*/
	}

}
