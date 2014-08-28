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
package com.mimpidev.podsalinan;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.mimpidev.dev.debug.Log;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.options.*;
import com.mimpidev.podsalinan.data.Episode;
import com.mimpidev.podsalinan.data.Podcast;
import com.mimpidev.podsalinan.data.PodcastList;
import com.mimpidev.podsalinan.data.ProgSettings;
import com.mimpidev.podsalinan.data.URLDownload;
import com.mimpidev.podsalinan.data.URLDownloadList;

/**
 * @author bugman
 *
 */
public class CLInterface implements Runnable{
	private CLInput input;
	private ArrayList<MenuPath> menuList;
	private CLMainMenu mainMenu;
	private DataStorage data=null;
	private Map<String, CLIOption> menuOptions=null;

	public CLInterface(DataStorage newData){
		setData(newData);
		menuList = new ArrayList<MenuPath>();
		input = new CLInput();
		initializeMenus();
	}
	
	public CLInterface(PodcastList podcasts, URLDownloadList urlDownloads, ProgSettings settings){
		data = new DataStorage();
		data.setPodcasts(podcasts);
		data.setUrlDownloads(urlDownloads);
		data.setSettings(settings);
		menuList = new ArrayList<MenuPath>();
		input = new CLInput();
		initializeMenus();
	}

	private void initializeMenus() {
		// Main menu requires podcasts and urlDownloads so it can display the number of podcasts and downloads queued.
		mainMenu = new CLMainMenu(menuList,data.getPodcasts(),data.getUrlDownloads());
		// When creating the Podcast Menus, we need settings to grab the default directory to do a manual update,
		// and urlDownloads so we can queue episodes up for downloading manually.
		mainMenu.addSubmenu(new CLPodcastMenu(menuList,data.getPodcasts(),data.getUrlDownloads()));
		mainMenu.addSubmenu(new CLPreferencesMenu(menuList,data.getSettings()));
		mainMenu.addSubmenu(new CLDownloadMenu(menuList,data.getUrlDownloads()));
		
		menuOptions = new HashMap<String, CLIOption>();
		menuOptions.put("quit", new QuitCommand(data));
		menuOptions.put("exit", new QuitCommand(data));
		menuOptions.put("http", new URLCommand(data));
		menuOptions.put("ftp", new URLCommand(data));
		menuOptions.put("help", new HelpCommand(data));
		menuOptions.put("select", new SelectCommand(data));
		menuOptions.put("set", new SetCommand(data));
		menuOptions.put("list", new ListCommand(data));
		menuOptions.put("show", new ShowCommand(data));
		menuOptions.put("hide", new HideCommand(data));
		menuOptions.put("download", new DownloadCommand(data));
		menuOptions.put("restart", new RestartCommand(data));
		menuOptions.put("stop", new StopCommand(data));
		menuOptions.put("remove", new RemoveCommand(data));
		menuOptions.put("clear", new ClearCommand(data));
		menuOptions.put("increase", new IncreaseCommand(data));
		menuOptions.put("decrease", new DecreaseCommand(data));
		menuOptions.put("dump", new DumpCommand(data));
	}

	/* TODO: Rewrite user input to allow command line completion. Current thoughts on how to
	 * implement this:
	 *       Pass the the command string, and the method into a method of CLInput.
	 *       ie: userInput.addCommand("select podcast <userInput>",selectPodcastInput());
	 *       
	 * Then when user input is entered, and the user presses tab, it will either complete,
	 *   or list options.
	 */

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
		if ((menuInput.length()>0)&&(menuInput!=null)){
			try {
				int inputInt = Integer.parseInt(menuInput);
				// process number input
				if ((menuList.size()==0)&&(inputInt==4))
					data.getSettings().setFinished(true);
				else if ((data.getSettings().findSetting("menuVisible")==null)||
						 (data.getSettings().findSetting("menuVisible").equalsIgnoreCase("true")))
					mainMenu.process(inputInt);
			} catch (NumberFormatException e){
				// Replacing the If statements below.
				String methodCall=menuInput.split(" ",2)[0];
				menuOptions.get(methodCall).execute(menuInput);
				// If the input is not a number This area will sort out that code
				if ((data.getSettings().findSetting("menuVisible")==null)||
				    (data.getSettings().findSetting("menuVisible").equalsIgnoreCase("true")))
					mainMenu.process(menuInput);
			}
		}
	}

	@Override
	public void run() {
		System.out.println("Welcome to podsalinan.");
		System.out.println("----------------------");
		while (!data.getSettings().isFinished()){
			if ((menuList.size()==0)&&
				((data.getSettings().findSetting("menuVisible")==null)||
				 (data.getSettings().findSetting("menuVisible").equalsIgnoreCase("true"))))
				mainMenu.printMainMenu();
			if (!data.getSettings().isFinished())
				userInput();
		}
		System.out.println("Please Standby for system Shutdown.");
		synchronized (data.getSettings().getWaitObject()){
			data.getSettings().getWaitObject().notify();
		}
	}
	
    /** This method is used to try to find a date from the user entry
     * @param menuInput
     * @return
     */
	private Date convertDate(String menuInput) {
		Date date=null;
		String[] dateFormat = {"dd-MMM-yy",
				               "dd-MM-yy",
				               "dd-MMM-yyy",
				               "dd-MM-yyy"};
		int dateCounter=0;
		
		while ((date==null)&&(dateCounter<dateFormat.length)){
			try {
				date = new SimpleDateFormat(dateFormat[dateCounter]).parse(menuInput);
			} catch (ParseException e) {
				// date not found
			}
			dateCounter++;
		}
		
		return date;
	}

	private void selectPodcast(Podcast podcast){
		// Add information to menuList
		menuList.clear();
		menuList.add(new MenuPath("mainMenu", "podcast"));
		menuList.add(new MenuPath("selectedPodcast", podcast.getDatafile()));
			
		// Set selected podcast
		((CLPodcastSelectedMenu)(mainMenu.findSubmenu("podcast_selected"))).setSelectedPodcast(podcast);

		if ((data.getSettings().isValidSetting("menuVisible"))&&
			(data.getSettings().findSetting("menuVisible").equalsIgnoreCase("false")))
  		    System.out.println("Selected Podcast: "+podcast.getName());
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
	 * @return the data
	 */
	public DataStorage getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(DataStorage data) {
		this.data = data;
	}
	
	public static class MenuPath {
		public String name;
		public String value;

		public MenuPath(String newName, String newValue) {
			name = newName;
			newValue = value;
		}
	}
}
