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
package com.mimpidev.podsalinan.cli;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.options.*;
import com.mimpidev.podsalinan.cli.options.downloads.*;
import com.mimpidev.podsalinan.cli.options.episode.*;
import com.mimpidev.podsalinan.cli.options.generic.ChangeDestination;
import com.mimpidev.podsalinan.cli.options.generic.RemoveItem;
import com.mimpidev.podsalinan.cli.options.help.*;
import com.mimpidev.podsalinan.cli.options.list.*;
import com.mimpidev.podsalinan.cli.options.mainmenu.HideCommand;
import com.mimpidev.podsalinan.cli.options.mainmenu.ShowCommand;
import com.mimpidev.podsalinan.cli.options.podcast.*;
import com.mimpidev.podsalinan.cli.options.settings.*;
import com.mimpidev.podsalinan.data.PodcastList;
import com.mimpidev.podsalinan.data.ProgSettings;
import com.mimpidev.podsalinan.data.URLDownloadList;

/**
 * @author bugman
 *
 */
public class CLInterface extends CLIOption implements Runnable{
	/**
	 * 
	 */
	private CLInput input;
	
	/**
	 * cliGlobals is used to pass information between the CLIOptions.
	 */
	public static final CLIGlobals cliGlobals = new CLIGlobals();
	
	private Map<String,CLIOption> options;

	public CLInterface(DataStorage newData){
		super(newData);
		input = new CLInput();
		options=new HashMap<String,CLIOption>();
		initializeMenus();
	}
	
	public CLInterface(PodcastList podcasts, URLDownloadList urlDownloads, ProgSettings settings){
		this(new DataStorage());
		data.setPodcasts(podcasts);
		data.setUrlDownloads(urlDownloads);
		data.setSettings(settings);
	}

	private void initializeMenus() {
		
        CLIOption quit = new QuitCommand(data);
		options.put("quit", quit);
		options.put("exit", quit);
        CLIOption urlCommand =new URLCommand(data);
		options.put("<url>", urlCommand);
		options.put("download <url>", urlCommand);
		options.put("help", new Help(data));
		options.put("help list", new HelpList(data));
		options.put("help select", new HelpSelect(data));
		options.put("help set", new HelpSet(data));

		options.put("list podcasts", new ListPodcasts(data));
		options.put("list episodes", new ListEpisodes(data));
		options.put("list select", new ListSelection(data));
		options.put("list details", new ListDetails(data));
		options.put("list downloads", new ListDownloads(data));
		options.put("list preferences", new ListPreferences(data));
		
		options.put("show menu", new ShowCommand(data));  //TODO: 1.04 - Need to fix
		options.put("hide menu", new HideCommand(data));  //TODO: 1.05 - Need to fix
		options.put("remove", new RemoveItem(data));      //TODO: 1.06 - Need to fix
		options.put("remove <downloadid|podcastid>", new RemoveItem(data)); //TODO: 1.07 - Need to fix
		// New command to implement
		options.put("remove all downloads", new RemoveItem(data)); //TODO: 1.08 - Need to fix
		
		options.put("dump", new DumpCommand(data));              //TODO: 1.09 - Need to fix
		options.put("dump urldownloads", new DumpCommand(data)); //TODO: 1.10 - Need to fix

		/** 
		 *  The following group of cli options are for the podcast menu & submenu
		 */
		//SelectPodcast will be used when a user either enters the podcast name, or the menu item letter 
		CLIOption selectPodcast = new SelectPodcast(data);
        options.put("podcast <podcastName>", selectPodcast);
		options.put("podcast <a-z>",  selectPodcast);
        // Show Podcast Selected Menu will be called a number of ways
		CLIOption showSelectedPodcastMenu =new com.mimpidev.podsalinan.cli.options.podcast.ShowSelectedMenu(data);
		options.put("podcast <podcastid>", showSelectedPodcastMenu);
		options.put("podcast <podcastid> showmenu", showSelectedPodcastMenu);
		options.put("podcast <podcastid> episode <a-z> 9", showSelectedPodcastMenu);
		options.put("podcast <podcastid> 1", new ListEpisodes(data));
		options.put("podcast <podcastid> 2", new UpdatePodcast(data));
		CLIOption deletePodcast = new DeletePodcast(data);
		options.put("podcast <podcastid> 3", deletePodcast);
		options.put("remove podcast", deletePodcast);
		CLIOption changeDestination = new ChangeDestination(data);
		options.put("podcast <podcastid> 5", new com.mimpidev.podsalinan.cli.options.podcast.AutoQueueEpisodes(data)); //TODO: 1.02 - Need to fix
		options.put("podcast <podcastid> showdetails", new ShowPodcastDetails(data));
		
		CLIOption selectEpisode = new SelectEpisode(data);
		options.put("podcast <podcastid> <a-z>", selectEpisode);
		options.put("podcast <podcastid> episode <a-z>", selectEpisode);
		options.put("podcast <podcastid> episode <a-z> showmenu", new com.mimpidev.podsalinan.cli.options.episode.ShowSelectedMenu(data));
		CLIOption downloadEpisode = new DownloadEpisode(data);
		options.put("podcast <podcastid> episode <a-z> 1", downloadEpisode);
		options.put("download episode", downloadEpisode);
		options.put("podcast <podcastid> episode <a-z> 2", new DeleteEpisodeFromDrive(data));
		options.put("podcast <podcastid> episode <a-z> 3", new com.mimpidev.podsalinan.cli.options.episode.CancelDownload(data));
		options.put("podcast <podcastid> episode <a-z> 4", new com.mimpidev.podsalinan.cli.options.episode.ChangeStatus(data));
		options.put("podcast <podcastid> episode <a-z> showdetails", new ShowEpisodeDetails(data));
		
		CLIOption podcastShowmenu = new com.mimpidev.podsalinan.cli.options.podcast.ShowMenu(data);
		options.put("podcast showmenu", podcastShowmenu);
		options.put("podcast <podcastid> 9", podcastShowmenu);

		options.put("select episode <a-z>", selectEpisode);
		options.put("select podcast <podcastName>", selectPodcast);  
		options.put("select podcast <a-z>", selectPodcast);          
		options.put("select podcast <podcastid>", selectPodcast);
		/**
		 * Here ends the podcast menu commands
		 */
        // The following is a generic call - which handles podcasts & downloads
		options.put("podcast <podcastid> 4", changeDestination);     //TODO: 1.01 - Need to fix for all 3 calls
		options.put("downloads <downloadid> 7", changeDestination); 
		options.put("set destination <path>", changeDestination);
		
		/**
		 *  Here is the download menu command list
		 */
		CLIOption downloadsShowmenu = new com.mimpidev.podsalinan.cli.options.downloads.ShowMenu(data);
		options.put("downloads showmenu", downloadsShowmenu);
		options.put("downloads <downloadid> 9", downloadsShowmenu);
        CLIOption selectDownload = new SelectDownload(data);
		options.put("downloads <a-z>", selectDownload);
		options.put("select download <a-z>", selectDownload);
		options.put("select download <downloadid>", selectDownload);  //TODO: 1.11.01 - Need to fix
		CLIOption showSelectedDownloadMenu = new com.mimpidev.podsalinan.cli.options.downloads.ShowSelectedMenu(data);
		options.put("downloads <downloadid>", showSelectedDownloadMenu);
		options.put("downloads <downloadid> showmenu", showSelectedDownloadMenu);
        CLIOption deleteDownload = new DeleteDownload(data);
		options.put("downloads <downloadid> 1", deleteDownload);
		options.put("remove download", deleteDownload);				//TODO: 1.11.02 - Need to fix
		CLIOption restartDownload = new RestartDownload(data);
		options.put("downloads <downloadid> 2", restartDownload);		
		options.put("restart downloads", restartDownload);		    //TODO: 1.11.03 - Need to fix
		CLIOption stopDownload =new StopDownload(data);
		options.put("downloads <downloadid> 3", stopDownload);		
		options.put("stop download", stopDownload);					//TODO: 1.11.04 - Need to fix
		options.put("stop <downloadid>", stopDownload);				//TODO: 1.11.05 - Need to fix
		options.put("stop", stopDownload);							//TODO: 1.11.06 - Need to fix
		options.put("downloads <downloadid> 4", new StartDownload(data));	
        CLIOption increasePriority = new IncreasePriority(data);
		options.put("downloads <downloadid> 5", increasePriority);		
		options.put("increase", increasePriority);					     //TODO: 1.11.07 Need to fix
		options.put("increase download <downloadid>", increasePriority); //TODO: 1.11.08 Need to fix
		CLIOption decreasePriority = new DecreasePriority(data);
		options.put("downloads <downloadid> 6", decreasePriority);		
		options.put("decrease", decreasePriority);                       //TODO: 1.11.09 Need to fix
		options.put("decrease download <downloadid>", decreasePriority); //TODO: 1.11.10 Need to fix
		options.put("downloads <downloadid> showdetails", new ShowDownloadDetails(data));

		/**
		 *  Here ends the download menu command list
		 */
		
		/**
		 *   Begin Settings Options
		 */
		CLIOption showSettingsMenu = new com.mimpidev.podsalinan.cli.options.settings.ShowMenu(data);
		options.put("settings", showSettingsMenu);
		options.put("settings showmenu", showSettingsMenu);
		options.put("settings <0-9> 9", showSettingsMenu);
		CLIOption podcastUpdateRate =new PodcastUpdateRate(data);
		options.put("settings 1", podcastUpdateRate);         
		options.put("set updateinterval <0-9>", podcastUpdateRate); //TODO: 1.12.1 - Fix it
		CLIOption maxDownloads = new MaxDownloaders(data);
		options.put("settings 2", maxDownloads);				 //TODO:1.12.2 - Fix User Input
		options.put("set maxdownloaders <0-9>", maxDownloads);   //TODO:1.12.2.2 - Fix pass in value
		CLIOption downloadDirectory =new DownloadDirectory(data); 
		options.put("settings 3", downloadDirectory);
		options.put("set defaultdirectory <path>", downloadDirectory);   //TODO: 1.12.3 - Fix input scan to match path to file system
		CLIOption autoqueueEpisodes =new com.mimpidev.podsalinan.cli.options.settings.AutoQueueEpisodes(data); 
		options.put("settings 4", autoqueueEpisodes);
		options.put("set autoqueue <0|1|true|false>", autoqueueEpisodes);           //TODO: 1.12.4 - Fix input scan to match 0|1|true|false
		CLIOption downloadSpeedLimit = new DownloadSpeedLimit(data); 
		options.put("settings 5", downloadSpeedLimit);
		options.put("set downloadlimit <00M>", downloadSpeedLimit);                //TODO: 1.12.5 - Fix input scan to match <00M> to speed
		options.put("set menuvisible <0|1|true|false>", new MenuVisibility(data)); //TODO: 1.12.6 - Need to fix
		/**
		 *  End settings options
		 */
		
		/**
		 *  Begin main menu calls
		 */
		CLIOption mainMenuCommands = new MainMenuCommand(data);
		CLIOption mainMenuShow = new com.mimpidev.podsalinan.cli.options.mainmenu.ShowMenu(data); 
		options.put("mainmenu showmenu", mainMenuShow);
		options.put("podcast 9", mainMenuShow);
		options.put("downloads 9", mainMenuShow);
		options.put("setting 9", mainMenuShow);
		options.put("mainmenu <0-9>", mainMenuCommands);
		/**
		 *  End Main Menu calls
		 */
	}

	//TODO: 1. Moving the command line menu around again. Move all of the child options to here
	//TODO: 2. remove all debug=true
	//TODO: 3. Change input to character input
	//TODO: 4. Add the ability for multiple child download threads to facilitate faster downloading.
	
	@Override
	public void run() {
		System.out.println("Welcome to podsalinan.");
		System.out.println("----------------------");
		returnObject.methodCall="mainmenu showMenu";
		returnObject.execute=true;
		while (!data.getSettings().isFinished()){
			returnObject=execute(returnObject.parameterMap);

			// User Input
			if (!data.getSettings().isFinished()){
				System.out.print("->");
				String menuInput=input.getStringInput();
				if ((menuInput.length()>0)&&(menuInput!=null)){
					returnObject = getMenuCommand(menuInput);
					if (!options.containsKey(returnObject.methodCall)){
						System.out.println("Error: Invalid command");
						System.out.println("Error: "+returnObject.methodCall);
					}
					returnObject = options.get(returnObject.methodCall).execute(returnObject.parameterMap);
				}
			}
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
	public Date convertDate(String menuInput) {
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

	public ReturnObject getMenuCommand(String input){
		ReturnObject menuCommand = new ReturnObject();
		boolean failedMatch=false;
		
		while (!menuCommand.execute && !failedMatch){
			if (!options.containsKey(input)){
				int score=0;
				boolean match=false;
	        	if (debug) Podsalinan.debugLog.logInfo(this, 312, "user input: '"+input+"'");
				String[] methodCallSplit = input.split(" ");
				if (debug) Podsalinan.debugLog.logInfo(this, 314, "length:"+methodCallSplit.length);
	        	for (String key : options.keySet()){
					score=0;
	        		String[] splitValue = key.split(" ");
					if ((debug)&&(key.equalsIgnoreCase("podcast <podcastid> <aa>"))){
						Podsalinan.debugLog.logInfo(this, 319, key);
						Podsalinan.debugLog.logInfo(this, 320, "length:"+splitValue.length);
					}
	        		if (splitValue.length==methodCallSplit.length){
	        			int svc=0;
	        			while (svc<splitValue.length && score<splitValue.length && !match){
	        				if (splitValue[svc].startsWith("<")&&
	        					splitValue[svc].endsWith(">")){
	        					
	        					
	        					if ((splitValue[svc].matches("^<url>") &&
	        						 methodCallSplit[svc].matches("\\b(https?|ftp):.*"))||
	        						(splitValue[svc].matches("^\\<((download|podcast)(I|i)d|download(I|i)d\\|podcast(I|i)d)\\>") &&
	    	        				 methodCallSplit[svc].matches("^[a-fA-F0-9]{8}") &&
	    	        				 !methodCallSplit[svc].equalsIgnoreCase("showmenu"))){
		        					if (splitValue[svc].matches("^<url>")){
		        						menuCommand.parameterMap.put("url", methodCallSplit[svc]);
		        					} else if (splitValue[svc].matches("^\\<((download|podcast)(I|i)d|download(I|i)d\\|podcast(I|i)d)\\>")){
		        						menuCommand.parameterMap.put("uid", methodCallSplit[svc]);
		        					}
	        						score++;
	        					} else if (splitValue[svc].matches("<podcastName>")&&
	        						!methodCallSplit[svc].matches("^[a-fA-F0-9]{8}")){
	        						menuCommand.parameterMap.put("userInput", methodCallSplit[svc]);
	        						score++;
	        					} else if (splitValue[svc].matches("<a-z>") &&
	        						methodCallSplit[svc].matches("[a-zA-Z]{1,2}")){
	        						menuCommand.parameterMap.put("userInput", methodCallSplit[svc]);
	        						score++;
	        					}
	        				} else {
	        					if (splitValue[svc].equalsIgnoreCase(methodCallSplit[svc])){
	        						score++;
	        					}
	        				}
	        				svc++;
	        			}
	        			if (score==splitValue.length){
	        				match=true;
	        				menuCommand.methodCall=key;
	        				menuCommand.execute=true;
	        				if (debug) Podsalinan.debugLog.logInfo(this, 356, "matched");
	        				break;
	        			}
	        		}
	        	}
	            if (!match){
	            	menuCommand.execute=false;
					if (debug) Podsalinan.debugLog.logInfo(this, 363, "not matched");
	            }
	        } else {
        	    menuCommand.methodCall=input;
        	    if (debug) Podsalinan.debugLog.logInfo(this, 375, menuCommand.methodCall);
        	    menuCommand.debug(debug);
        	    menuCommand.execute=true;
			}
			
			// This is going to traverse the main menu
			if (!menuCommand.execute){
				if (returnObject.methodCall.length()==0 && input.matches("[0-9]{1}")){
					menuCommand.parameterMap.put("menuItem", input);
					menuCommand.methodCall="mainmenu <0-9>";
					menuCommand.execute=true;
				} else if (returnObject.methodCall.length()>0 && 
						   !input.contains(returnObject.methodCall) &&
						   input.length()>0){
					input=returnObject.methodCall+" "+input;
				} else if (returnObject.methodCall.length()>0){
					System.out.println("Error: Invalid input - "+input);
					input=returnObject.methodCall;
					if (debug) Podsalinan.debugLog.logInfo(this, 397, "Error - '"+input+"'");
				} else if (cliGlobals.getGlobalSelection().size()>0 && input.length()>0){
					input=cliGlobals.globalSelectionToString()+" "+input;
					if (debug) Podsalinan.debugLog.logMap(this, cliGlobals.getGlobalSelection());
					if (debug) Podsalinan.debugLog.logInfo(this, 393, "'"+menuCommand.methodCall+"'");
				} else {
					failedMatch=true;
				}
			}
		}
		if(failedMatch){
			menuCommand.methodCall=cliGlobals.globalSelectionToString();
			menuCommand.execute=true;
		}
		
		menuCommand.debug(debug);
		
		return menuCommand;
	}
	
	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
    	
		while (returnObject.execute){

			if (debug) Podsalinan.debugLog.logInfo(this, "Calling requested function: "+returnObject.methodCall);
			if (returnObject.parameterMap.size()==0){
				cliGlobals.createReturnParameters(returnObject.parameterMap);				
			}
			returnObject.debug(true);
			returnObject=getMenuCommand(returnObject.methodCall);
			if (!options.containsKey(returnObject.methodCall)){
				System.out.println("Error: command does not exist.");
				System.out.println("Error: "+returnObject.methodCall);
				returnObject.execute=false;
			} else {
				returnObject=options.get(returnObject.methodCall.toLowerCase()).execute(returnObject.parameterMap);
			}
		}
		return returnObject; //ece<3sam
	}
}
