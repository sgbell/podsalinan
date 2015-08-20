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
import com.mimpidev.podsalinan.cli.options.help.*;
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
		
        QuitCommand quit = new QuitCommand(data);
		options.put("quit", quit);
		options.put("exit", quit);
        URLCommand urlCommand =new URLCommand(data);
		options.put("http", urlCommand);
		options.put("ftp", urlCommand);
		options.put("help", new Help(data));
		options.put("help list", new HelpList(data));
		options.put("help select", new HelpSelect(data));
		options.put("help set", new HelpSet(data));
		options.put("select podcast", new SelectCommand(data));
		options.put("select episode", new SelectCommand(data));
		options.put("select download", new SelectCommand(data));
		options.put("set updateinterval", new SetCommand(data));
		options.put("set downloadlimit", new SetCommand(data));
		options.put("set maxdownloaders", new SetCommand(data));
		options.put("set autoqueue", new SetCommand(data));
		options.put("set menuvisible", new SetCommand(data));
		options.put("set defaultdirectory", new SetCommand(data));
		options.put("set destination", new SetCommand(data));
		options.put("set podcast directory", new SetCommand(data));
		options.put("set directory", new SetCommand(data));
		options.put("list podcasts", new ListCommand(data));
		options.put("list episode", new ListCommand(data));
		options.put("list select", new ListCommand(data));
		options.put("list details", new ListCommand(data));
		options.put("list downloads", new ListCommand(data));
		options.put("list preferences", new ListCommand(data));
		options.put("show menu", new ShowCommand(data));
		options.put("hide menu", new HideCommand(data));
		options.put("download episode", new DownloadCommand(data));
		options.put("download <url>", new DownloadCommand(data));
		options.put("restart episode", new RestartCommand(data));
		options.put("restart downloads", new RestartCommand(data));
		options.put("stop", new StopCommand(data));
		options.put("stop download", new StopCommand(data));
		options.put("stop <downloadid>", new StopCommand(data));
		options.put("remove", new RemoveCommand(data));
		options.put("remove download", new RemoveCommand(data));
		options.put("remove podcast", new RemoveCommand(data));
		options.put("remove <downloadid|podcastid>", new RemoveCommand(data));
		// New command to implement
		options.put("remove all downloads", null);
		options.put("clear", new ClearCommand(data));
		options.put("increase", new IncreaseCommand(data));
		options.put("increase download <downloadid>", new IncreaseCommand(data));
		options.put("decrease", new DecreaseCommand(data));
		options.put("decrease download <downloadid>", new DecreaseCommand(data));
		options.put("dump", new DumpCommand(data));
		options.put("dump urldownloads", new DumpCommand(data));

		/** 
		 *  The following group of cli options are for the podcast menu & submenu
		 */
		//SelectPodcast will be used when a user either enters the podcast name, or the menu item letter 
		SelectPodcast selectPodcast = new SelectPodcast(data);
        options.put("podcast <podcastName>", selectPodcast);
		options.put("podcast <a-z>",  selectPodcast);
        // Show Podcast Selected Menu will be called a number of ways
		CLIOption showSelectedPodcastMenu =new com.mimpidev.podsalinan.cli.options.podcast.ShowSelectedMenu(data);
		options.put("podcast <podcastid>", showSelectedPodcastMenu);
		options.put("podcast <podcastid> showmenu", showSelectedPodcastMenu);
		options.put("podcast <podcastid> episode <aa> 9", showSelectedPodcastMenu);
		options.put("podcast <podcastid> 1", new ListEpisodes(data));
		options.put("podcast <podcastid> 2", new UpdatePodcast(data));
		options.put("podcast <podcastid> 3", new DeletePodcast(data));
		options.put("podcast <podcastid> 4", new ChangeDestination(data));
		options.put("podcast <podcastid> 5", new com.mimpidev.podsalinan.cli.options.podcast.AutoQueueEpisodes(data));
		
		CLIOption selectEpisode = new SelectEpisode(data);
		options.put("podcast <podcastid> <aa>", selectEpisode);
		options.put("podcast <podcastid> episode <aa>", selectEpisode);
		options.put("podcast <podcastid> episode <aa> showmenu", new com.mimpidev.podsalinan.cli.options.episode.ShowSelectedMenu(data));
		options.put("podcast <podcastid> episode <aa> 1", new DownloadEpisode(data));
		options.put("podcast <podcastid> episode <aa> 2", new DeleteEpisodeFromDrive(data));
		options.put("podcast <podcastid> episode <aa> 3", new com.mimpidev.podsalinan.cli.options.episode.CancelDownload(data));
		options.put("podcast <podcastid> episode <aa> 4", new com.mimpidev.podsalinan.cli.options.episode.ChangeStatus(data));
		
		CLIOption podcastShowmenu = new com.mimpidev.podsalinan.cli.options.podcast.ShowMenu(data);
		options.put("podcast showmenu", podcastShowmenu);
		options.put("podcast <podcastid> 9", podcastShowmenu);
		/**
		 * Here ends the podcast menu commands
		 */
		
		/**
		 *  Here is the download menu command list
		 */
		CLIOption downloadsShowmenu = new com.mimpidev.podsalinan.cli.options.downloads.ShowMenu(data);
		options.put("downloads showmenu", downloadsShowmenu);
		options.put("downloads <downloadid> 9", downloadsShowmenu);
		options.put("downloads <a-z>", new SelectDownload(data));
		CLIOption showSelectedDownloadMenu = new com.mimpidev.podsalinan.cli.options.downloads.ShowSelectedMenu(data);
		options.put("downloads <downloadid>", showSelectedDownloadMenu);
		options.put("downloads <downloadid> showmenu", showSelectedDownloadMenu);
		options.put("downloads <downloadid> 1", new DeleteDownload(data));		
		options.put("downloads <downloadid> 2", new RestartDownload(data));		
		options.put("downloads <downloadid> 3", new StopDownload(data));		
		options.put("downloads <downloadid> 4", new StartDownload(data));		
		options.put("downloads <downloadid> 5", new IncreasePriority(data));		
		options.put("downloads <downloadid> 6", new DecreasePriority(data));		
		options.put("downloads <downloadid> 7", new ChangeDestination(data));
		/**
		 *  Here ends the download menu command list
		 */
		
		/**
		 *   Begin Settings Options
		 */
		CLIOption showSettingsMenu = new com.mimpidev.podsalinan.cli.options.settings.ShowMenu(data);
		options.put("settings showmenu", showSettingsMenu);
		options.put("settings <0-9> 9", showSettingsMenu);
		options.put("settings 1", new PodcastUpdateRate(data));
		options.put("settings 2", new MaxDownloaders(data));
		options.put("settings 3", new DownloadDirectory(data));
		options.put("settings 4", new com.mimpidev.podsalinan.cli.options.settings.AutoQueueEpisodes(data));
		options.put("settings 5", new DownloadSpeedLimit(data));
		/**
		 *  End settings options
		 */
		
		/**
		 *  Begin main menu calls
		 */
		MainMenuCommand mainMenuCommands = new MainMenuCommand(data);
		com.mimpidev.podsalinan.cli.options.mainmenu.ShowMenu mainMenuShow = new com.mimpidev.podsalinan.cli.options.mainmenu.ShowMenu(data); 
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
	/*TODO: 4. Optimize loading of Data. IE load podcast information, and downloads.
	 *         Then as the user loads a podcast, or the system does an update. load the podcast data.
	 */
	/*TODO: 5. Noticed 100s of download threads during debug while limited to 3 downloaders. Need to change the
	 *         functionality so that the system will have (max downloaders) number of downloaders running at all
	 *         times, and have them sleep when they have nothing to do. Wkae them up when they have a file to download
	 */
	//TODO: 6. Add the ability for multiple child download threads to facilitate faster downloading.
	
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
		
		while (!menuCommand.execute){
			if (!options.containsKey(input)){
				int score=0;
				boolean match=false;
	        	if (debug) Podsalinan.debugLog.logInfo(this, "user input: '"+input+"'");
				String[] methodCallSplit = input.split(" ");
	        	for (String key : options.keySet()){
					score=0;
	        		String[] splitValue = key.split(" ");
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
	        						methodCallSplit[svc].matches("[a-zA-Z]")){
	        						menuCommand.parameterMap.put("userInput", methodCallSplit[svc]);
	        						score++;
	        					} else if (splitValue[svc].matches("<aa>") &&
	        							methodCallSplit[svc].matches("[a-zA-Z]{1,2}")){
	        						menuCommand.parameterMap.put("episode", methodCallSplit[svc]);
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
	        				if (debug) Podsalinan.debugLog.logInfo(this, 262, "matched");
	        				break;
	        			}
	        		}
	        	}
	            if (!match){
	            	menuCommand.execute=false;
					if (debug) Podsalinan.debugLog.logInfo(this, 272, "not matched");
	            }
	        } else {
        	    menuCommand.methodCall=input;
        	    if (debug) Podsalinan.debugLog.logInfo(this, menuCommand.methodCall);
        	    returnObject.debug(debug);
        	    menuCommand.execute=true;
			}
			
			// This is going to traverse the main menu
			if (!menuCommand.execute){
				if (returnObject.methodCall.length()==0 && input.matches("[0-9]{1}")){
					menuCommand.parameterMap.put("menuItem", input);
					menuCommand.methodCall="mainmenu <0-9>";
					menuCommand.execute=true;
				} else if (returnObject.methodCall.length()>0 && !input.contains(returnObject.methodCall)){
					input=returnObject.methodCall+" "+input;
				} else {
					System.out.println("Error: Invalid input - "+input);
					input=returnObject.methodCall;
					if (debug) Podsalinan.debugLog.logInfo(this, "Error - '"+input+"'");
				}
			}
		}
		menuCommand.debug(debug);
		
		return menuCommand;
	}
	
	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
    	
		while (returnObject.execute){

			if (debug) Podsalinan.debugLog.logInfo(this, "Calling requested function: "+returnObject.methodCall);
			if (returnObject.parameterMap.size()==0){
				returnObject.parameterMap=cliGlobals.getGlobalSelection();
			}
			returnObject.debug(true);
			returnObject=getMenuCommand(returnObject.methodCall);
			returnObject=options.get(returnObject.methodCall.toLowerCase()).execute(returnObject.parameterMap);
		}
		return returnObject; //ece<3sam
	}
}
