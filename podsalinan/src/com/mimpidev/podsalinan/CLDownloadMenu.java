/**
 * 
 */
package com.mimpidev.podsalinan;

/**
 * @author bugman
 *
 */
public class CLDownloadMenu extends CLMenu{
	private URLDownloadList urlDownloads;

	public CLDownloadMenu(ProgSettings newMenuList, URLDownloadList downloadList) {
		super(newMenuList,"downloads");
		String[] mainMenuList = {"(A-ZZ) Enter Download letter to select Download.",
								 "To add a new download to the queue just enter the the url to be downloaded.",
								 "",
								 "9. Return to Main Menu"

		};
		setMainMenuList(mainMenuList);
		setUrlDownloads(downloadList);
		addSubmenu(new CLDownloadSelectedMenu(menuList, downloadList));
	}

	public void printMainMenu() {
		listDownloads();
		super.printMainMenu();
	}

	public void listDownloads(){
		int downloadCount=1;
		
		for (URLDownload download : urlDownloads.getDownloads()){
			if (!download.isRemoved()){
				System.out.println(getEncodingFromNumber(downloadCount)+". ("+download.getCharStatus()+") "+download.getURL().toString());
				downloadCount++;
			}
		}
	}
	
	public void process(int userInputInt) {
		//System.out.println("CLDownloadMenu.process(int);");
		if (menuList.size()==1){
			switch (userInputInt){
			    case 9:
				    menuList.clear();
				    break;
				case 99:
					printMainMenu();
		    }
		}
		//System.out.println("CLDownloadMenu.process(int) - menuList.size()="+menuList.size());
		if (menuList.size()>1){
			if (menuList.findSetting("selectedDownload")!=null)
				((CLDownloadSelectedMenu)findSubmenu("downloadSelected_menu")).process(userInputInt);
		}
		if (menuList.size()==1)
			userInputInt=-1000;
		    super.process(userInputInt);
		//System.out.println("CLDownloadMenu.process(int) - menuList.size()="+menuList.size());

	}
	
	public void process(String userInput){
		//System.out.println("CLDownloadMenu.process(String);");
		if (menuList.size()==1){
			if (userInput.length()<3){
				int downloadNumber = convertCharToNumber(userInput);
				
				if ((downloadNumber>urlDownloads.size())&&
					(downloadNumber<0))
					System.out.println("Error: Invalid Download Selected");
				else {
					menuList.addSetting("selectedDownload", urlDownloads.getDownloads().get(downloadNumber).getURL().toString());
					((CLDownloadSelectedMenu)findSubmenu("downloadSelected_menu")).setDownload(urlDownloads.getDownloads().get(downloadNumber));
				}
				userInput=null;
			}
		}
		//System.out.println("CLDownloadMenu.process(int) -menuList.size()="+menuList.size());
		if (menuList.size()>1){
			if (menuList.findSetting("selectedDownload")!=null)
				((CLDownloadSelectedMenu)findSubmenu("downloadSelected_menu")).process(userInput);
		}
		if (menuList.size()==1)
			super.process(userInput);
		//System.out.println("CLDownloadMenu.process(int) -menuList.size()="+menuList.size());
	}

	public URLDownloadList getUrlDownloads() {
		return urlDownloads;
	}

	public void setUrlDownloads(URLDownloadList urlDownloads) {
		this.urlDownloads = urlDownloads;
	}
}
