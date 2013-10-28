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
		mainMenu.addSubmenu(new CLPodcastMenu(menuList,podcasts,settings,urlDownloads));
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
