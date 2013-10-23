/*******************************************************************************
 * Copyright (c) 2013 Sam Bell.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or  any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See
 * the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Sam Bell - initial API and implementation
 ******************************************************************************/
/**
 * 
 */
package podsalinan;

import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

/**
 * @author bugman
 *
 */
public class CLInterface implements Runnable{
	private boolean finished=false;
	private Vector<Podcast> podcasts;
	private URLDownloadList urlDownloads;
	private ProgSettings settings;
	private Object waitObject = new Object();
	private CLInput input;
	private ProgSettings menuList;
	private CLMainMenu mainMenu;

	public CLInterface(Vector<Podcast> podcasts, URLDownloadList urlDownloads, ProgSettings settings){
		this.podcasts=podcasts;
		this.urlDownloads=urlDownloads;
		this.settings=settings;
		menuList = new ProgSettings();
		input = new CLInput();
		initializeMenus();
	}

	private void initializeMenus() {
		mainMenu = new CLMainMenu(menuList,podcasts,urlDownloads);
		mainMenu.addSubmenu(new CLPodcastMenu(menuList,podcasts));
		mainMenu.addSubmenu(new CLPreferencesMenu(menuList,settings));
		mainMenu.addSubmenu(new CLDownloadMenu(menuList,urlDownloads));
	}


	/** Brain Storming how to deal with user input, as it works on 2 different levels.
	 *  
	 *  Process
	 *  ===================
	 *  accept user_input
	 *  if user_input is a number then
	 *  	if ((menuList.size = 0) && (user_input==4))
	 * 			quit
	 * 		else
	 * 			mainMenu.process(user_input);
	 *  else
	 *  	process user command
	 */
	
	public void userInput(){
		System.out.print("->");
		String menuInput=input.getStringInput();
		if (menuInput.length()>0){
			try {
				int inputInt = Integer.parseInt(menuInput);
				// process number input
				if ((menuList.size()==0)&&(inputInt==4))
					finished=true;
				else
					mainMenu.process(inputInt);
			} catch (NumberFormatException e){
				// If the input is not a number This area will sort out that code
				if ((menuInput.equalsIgnoreCase("quit"))||
					(menuInput.equalsIgnoreCase("exit"))){
					finished=true;
				} else if ((menuInput.startsWith("http"))||
						   (menuInput.startsWith("ftp"))){
					// User has entered a url to download.
					urlDownloads.addDownload(menuInput,settings.getSettingValue("defaultDirectory"),"-1",false);
				} else if (menuInput.startsWith("help")){
					helpList(menuInput);
				} else if (menuInput.startsWith("select")){
					cliSelection(menuInput);
				} else if (menuInput.startsWith("set")){
					setCommand(menuInput);
				} else if (menuInput.startsWith("list")){
					listCommand(menuInput);
				} else
					mainMenu.process(menuInput);
			}
		}
	}
	
	@Override
	public void run() {
		System.out.println("Welcome to podsalinan.");
		System.out.println("----------------------");
		while (!finished){
			if (menuList.size()==0)
				mainMenu.printMainMenu();
			if (!finished)
				userInput();
		}
		System.out.println("Please Standby for system Shutdown.");
		synchronized (waitObject){
			waitObject.notify();
		}
	}
	
	private void listCommand(String menuInput) {
		// TODO Auto-generated method stub
		
	}

	private void setCommand(String menuInput) {
		// TODO Auto-generated method stub
		
	}

	private void cliSelection(String menuInput) {
		// TODO Auto-generated method stub
		
	}

	private void helpList(String menuInput) {
		// TODO Auto-generated method stub
		
	}

	private void preferencesMenuSelection(ArrayList<Integer> menuSelection) {
		switch (menuSelection.get(1)){
			case 1:
				changePodcastRate();
				break;
			case 2:
				changeNumDownloaders();
				break;
			case 3:
				changeDefaultDirectory();
				break;
			case 4:
				changeAutoQueueNewEpisodes();
				break;
			case 5:
				setDownloadSpeed();
				break;
		}
		if (menuSelection.get(1)>0){
			menuSelection.remove(1);
			printPreferencesMenu();
		} else {
			menuSelection.clear();
		}
	}

	private void downloadsMainMenuSelection(ArrayList<Integer> menuSelection) {
		if (menuSelection.get(1)<0){
			switch ((0-menuSelection.get(1))){
				case 1:
					
					break;
				case 9:
					menuSelection.clear();
					break;
			}
		} else if (urlDownloads!=null)
			if ((menuSelection.get(1)<urlDownloads.getDownloads().size())&&
				(menuSelection.get(1)>=0))
				printDownloadSubmenu(menuSelection.get(1));
	}

	private void changeDownloadDirectory(Integer integer) {
		// TODO Auto-generated method stub
		
	}

	private void printPodcastSubmenu(int selectedPodcast) {
		System.out.println ("Podcast: "+podcasts.get(selectedPodcast).getName()+ " - Selected");
		System.out.println ();
		System.out.println ("1. List Episodes");
		System.out.println ("2. Update List");
		System.out.println ("3. Remove Podcast");
		System.out.println ("<AA>. Select Episode");
		System.out.println ();
		System.out.println ("9. Return to List of Podcasts");
	}

	private void printPodcastEpisodeList(int selectedPodcast) {
		System.out.println ();
		int epCount=1;
		Podcast podcast = podcasts.get(selectedPodcast);
		
		synchronized (podcast.getEpisodes()){
			for (Episode episode : podcast.getEpisodes()){
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

	private void printEpisodeMenu(ArrayList<Integer> menuSelection) {
		Podcast podcast = podcasts.get(menuSelection.get(1));
		Episode episode = null;
		synchronized (podcast.getEpisodes()){
			episode = podcast.getEpisodes().get(menuSelection.get(2));
		}
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
					System.out.println ("Status: Queued to Download");
					break;
				case Episode.FINISHED:
					System.out.println ("Status: Completed Download");
					break;
			}
			System.out.println ();
			System.out.println ("1. Download episode");
			System.out.println ("2. Delete episode from Drive");
			System.out.println ("3. Cancel download of episode");
			System.out.println ();
			System.out.println ("9. Return to Podcast menu");
		} else
			System.out.println ("Error: Invalid Episode");
	}

	private void setEpisodeStatus(ArrayList<Integer> menuSelection,
			int newStatus) {
		Podcast podcast = podcasts.get(menuSelection.get(1));
		synchronized (podcast.getEpisodes()){
			Episode episode = podcast.getEpisodes().get(menuSelection.get(2));
			episode.setStatus(newStatus);
		}
	}

	private void deleteEpisode(ArrayList<Integer> menuSelection) {
		Podcast podcast = podcasts.get(menuSelection.get(1));
		podcast.deleteEpisodeFromDrive(menuSelection.get(2));
	}

	private void printPreferencesMenu() {
		System.out.println ();
		System.out.println ("1. Change Podcast Update Rate");
		System.out.println ("2. Number of Downloaders");
		System.out.println ("3. Default Download Directory");
		System.out.println ("4. Automatically Download New Podcast Episodes");
		System.out.println ("5. Set Download Speed Limit");
		System.out.println ();
		System.out.println ("0. Return to Preferences Menu");
	}

	private void changeDefaultDirectory() {
		File newPath;
		System.out.println ();
		System.out.print ("Enter Default Directory: ");
		/* Take user input.
		 * Create File object to test if directory exists.
		 * If directory exists set defaultDirectory to file Input
		 * If not show and error and leave defaultDirectory as is
		 */
		String userInput=input.getStringInput();
		if ((userInput.length()>0)&&(userInput!=null)){
			newPath=new File(userInput);
			if ((newPath.exists())&&(newPath.isDirectory())){
				settings.updateSetting("defaultDirectory",userInput);
			} else {
				System.out.println ("Error: User Input invalid");
			}
		}
	}

	private void changeNumDownloaders() {
		System.out.println ();
		System.out.print ("Enter Number of Simultaneous Downloads: ");
		/* Take user input.
		 * Make sure it is between 1 and 30
		 * If not, get the user to enter it again.
		 */
		String numDownloaders = input.getValidNumber(1,30);
		if (numDownloaders!=null)
			settings.updateSetting("maxDownloaders",numDownloaders);
	}

	private void changeAutoQueueNewEpisodes() {
		System.out.println ();
		System.out.print ("Do you want new episodes Automatically Queued to Download? (Y/N): ");
		String autoDownloadResponse = input.getStringInput();
		if (autoDownloadResponse.length()==1){
			switch (autoDownloadResponse.charAt(0)){
				case 'Y':
				case 'y':
					settings.updateSetting("autoQueue","true");
					break;
				case 'N':
				case 'n':
					settings.updateSetting("autoQueue","false");
					break;
				default:
					System.err.println ("Error: User entered Value is invalid. No change made");
					break;
			}
		} else if (autoDownloadResponse.length()>1) {
			if (autoDownloadResponse.equalsIgnoreCase("yes"))
				settings.updateSetting("autoQueue","true");
			else if (autoDownloadResponse.equalsIgnoreCase("no"))
				settings.updateSetting("autoQueue","false");
			else if (autoDownloadResponse.equalsIgnoreCase("no"))
				System.err.println ("Error: User entered Value is invalid. No change made");
		}
	}

	private void changePodcastRate() {
		System.out.println ();
		System.out.println ("How often to update the podcast feeds?");
		System.out.println ("1. Hourly");
		System.out.println ("2. Every 2 Hours");
		System.out.println ("3. Every 3 Hours");
		System.out.println ("4. Every 6 Hours");
		System.out.println ("5. Every 12 Hours");
		System.out.println ("6. Daily");
		System.out.print ("Choice: ");
		/* Take user input.
		 * Make sure it is between 1 & 6
		 * If not leave PodcastRate as it's current value.
		 */
		
		String updateValue = input.getValidNumber(1,6);
		if (updateValue!=null){
			switch (Integer.parseInt(updateValue)){
				case 1:
					// 1 Hour
					updateValue="60";
					break;
				case 2:
					// 2 Hours
					updateValue="120";
					break;
				case 3:
					// 3 Hours
					updateValue="180";
					break;
				case 4:
					// 6 Hours
					updateValue="360";
					break;
				case 5:
					// 12 Hours
					updateValue="720";
					break;
				case 6:
					// 24 Hours
					updateValue="1440";
					break;
			}
			settings.updateSetting("updateInterval",updateValue);
			synchronized (waitObject){
				waitObject.notify();
			}
		}
	}

	private void setDownloadSpeed() {
		System.out.println();
		System.out.println("Valid values: 0 (Means no limit); 25 (Means 25Kbps); 1M (Means 1 Mbps)");
		System.out.println("The value you set here is the total limit, which is shared evenly across downloaders");
		System.out.print ("Please enter the Download Speed Limit:");
		String userInput = input.getStringInput();
		int speed=-1;
		if (userInput.length()>0){
			try {
				speed = Integer.parseInt(userInput);
			} catch (NumberFormatException e){
				speed= -1;
			}
			if (speed<0){
				if ((userInput.toUpperCase().endsWith("M"))||
					(userInput.toUpperCase().endsWith("MBPS"))||
					(userInput.toUpperCase().endsWith("MB"))){
					String speedString=userInput.split("M")[0];
					try{
						speed = (Integer.parseInt(speedString)*1024);
					} catch (NumberFormatException e){
						speed = -1;
					}
				}
			}
			if (speed<0){
				System.err.println("Invalid Speed. Download Speed Limit unchanged");
			} else {
				settings.addSetting("downloadLimit",Integer.toString(speed));
			}
		}
	}

	private void printDownloadSubmenu(int selectedDownload) {
		URLDownload download = urlDownloads.getDownloads().get(selectedDownload);
		if ((download!=null)&&(!download.isRemoved())){
			System.out.println("Download Url: "+download.getURL().toString());
			System.out.println("Destination: "+download.getDestination());
			System.out.println("Priority: "+(selectedDownload+1));
			switch (download.getStatus()){
				case Details.NOT_STARTED:
					System.out.println ("Status: Not Downloaded");
					break;
				case Details.CURRENTLY_DOWNLOADING:
				case Details.PREVIOUSLY_STARTED:
					System.out.println ("Status: Downloading");
					break;
				case Details.FINISHED:
					System.out.println ("Status: Completed Download");
					break;
				case Details.DO_NOT_DOWNLOAD:
					System.out.println ("Status: Cancelled Download");
					break;
		    }
			System.out.println();
			System.out.println("1. Increase Priority");
			System.out.println("2. Decrease Priority");
			System.out.println("3. Cancel Download");
			System.out.println("4. Restart Download");
			System.out.println("5. Delete Download");
			System.out.println("6. Change Destination");
			System.out.println("9. Return to Download menu");
		}
	}

	public boolean isFinished(){
		return finished;
	}
	
	public void setFinished(boolean isFinished){
		finished = isFinished;
	}
	
	public String getCharForNumber(int i){
		return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
	}
	
	public String getEncodingFromNumber(int number){
		String charOutput="";
		if (number<27)
			charOutput = getCharForNumber(number);
		else {
			if (number%26!=0){
				charOutput+=getCharForNumber(number/26);
				charOutput+=getCharForNumber(number%26);
			} else {
				charOutput=getCharForNumber((number/26)-1)+"Z";
			}
		}
		
		return charOutput;
	}

	/**
	 * @return the waitObject
	 */
	public Object getWaitObject() {
		return waitObject;
	}

	/**
	 * @param waitObject the waitObject to set
	 */
	public void setWaitObject(Object waitObject) {
		this.waitObject = waitObject;
	}
}
