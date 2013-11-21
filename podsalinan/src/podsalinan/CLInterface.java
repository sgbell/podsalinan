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
		// Main menu requires podcasts and urlDownloads so it can display the number of podcasts and downloads queued.
		mainMenu = new CLMainMenu(menuList,podcasts,urlDownloads);
		// When creating the Podcast Menus, we need settings to grab the default directory to do a manual update,
		// and urlDownloads so we can queue episodes up for downloading manually.
		mainMenu.addSubmenu(new CLPodcastMenu(menuList,podcasts,urlDownloads));
		mainMenu.addSubmenu(new CLPreferencesMenu(menuList,settings,waitObject));
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
				} else if ((menuInput.toUpperCase().startsWith("HTTP"))||
						   (menuInput.toUpperCase().startsWith("FTP"))){
					// User has entered a url to download.
					urlDownloads.addDownload(menuInput,settings.getSettingValue("defaultDirectory"),"-1",false);
				} else if (menuInput.toUpperCase().startsWith("HELP")){
					helpList(menuInput);
				} else if (menuInput.toUpperCase().startsWith("SELECT")){
					cliSelection(menuInput);
				} else if (menuInput.toUpperCase().startsWith("SET")){
					setCommand(menuInput);
				} else if (menuInput.toUpperCase().startsWith("LIST")){
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
		if (menuInput.equalsIgnoreCase("help")){
			// Main help text
			System.out.println("");
			System.out.println("A url is accepted at almost any time to add it to the download queue.");
			System.out.println("It will have to start with (http/https/ftp):// for the system to download it.");
			System.out.println("");
			System.out.println("4 main root commands");
			System.out.println("");
			System.out.println("   help <command>        will show this screen");
			System.out.println("   select                used to select podcast/episode/queued download");
			System.out.println("   set                   used for changing system preferences");
			System.out.println("   list                  used for listing podcasts/episodes/queued downloads/preferences");
			System.out.println("");
			System.out.println("");
			System.out.println("Commands to exit the program");
			System.out.println("   quit");
			System.out.println("   exit");
			System.out.println("");
			System.out.println("");
		} else if (menuInput.toLowerCase().contains("select")){
			// If user enters "help select"
		} else if (menuInput.toLowerCase().contains("list")){
			// If user enters "help list"
		    // Show sub commands for list command
			System.out.println("");
			System.out.println("list can have the following parameters");
			System.out.println("");
			System.out.println("    list podcast           show list of podcasts to the screen");
			System.out.println("    list episodes          show list of episodes to the screen, if a podcast has been selected");
			System.out.println("    list downloads         show list of queued downloads to the screen");
			System.out.println("    list preferences       show list of preferences to the screen");
			System.out.println("");
			System.out.println("");
		} else if (menuInput.toLowerCase().contains("set")){
			// If the user enters "help set"
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
