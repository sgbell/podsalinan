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
import com.mimpidev.podsalinan.cli.options.episode.SelectEpisode;
import com.mimpidev.podsalinan.cli.options.generic.ChangeDestination;
import com.mimpidev.podsalinan.cli.options.podcast.AutoQueueEpisodes;
import com.mimpidev.podsalinan.cli.options.podcast.DeletePodcast;
import com.mimpidev.podsalinan.cli.options.podcast.ListEpisodes;
import com.mimpidev.podsalinan.cli.options.podcast.SelectPodcast;
import com.mimpidev.podsalinan.cli.options.podcast.ShowSelectedMenu;
import com.mimpidev.podsalinan.cli.options.podcast.UpdatePodcast;
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

	public CLInterface(DataStorage newData){
		super(newData);
		debug=true;
		input = new CLInput();
		initializeMenus();
	}
	
	public CLInterface(PodcastList podcasts, URLDownloadList urlDownloads, ProgSettings settings){
		this(new DataStorage());
		data.setPodcasts(podcasts);
		data.setUrlDownloads(urlDownloads);
		data.setSettings(settings);
	}

	private void initializeMenus() {
		
        URLCommand urlCommand =new URLCommand(data);
        QuitCommand quit = new QuitCommand(data);
		options.put("quit", quit);
		options.put("exit", quit);
		options.put("http", urlCommand);
		options.put("ftp", urlCommand);
		options.put("help", new HelpCommand(data));
		options.put("help list", null);
		options.put("help select", null);
		options.put("help set", null);
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
		options.put("stop <downloadId>", new StopCommand(data));
		options.put("remove", new RemoveCommand(data));
		options.put("remove download", new RemoveCommand(data));
		options.put("remove podcast", new RemoveCommand(data));
		options.put("remove <downloadId|podcastId>", new RemoveCommand(data));
		// New command to implement
		options.put("remove all downloads", null);
		options.put("clear", new ClearCommand(data));
		options.put("increase", new IncreaseCommand(data));
		options.put("increase download <downloadId>", new IncreaseCommand(data));
		options.put("decrease", new DecreaseCommand(data));
		options.put("decrease download <downloadId>", new DecreaseCommand(data));
		options.put("dump", new DumpCommand(data));
		options.put("dump urldownloads", new DumpCommand(data));
        SelectPodcast selectPodcast = new SelectPodcast(data);
		options.put("podcast <podcastId>", selectPodcast);
		options.put("podcast <a-z>",  selectPodcast);
        // Exit podcast menu, and return to main menu
		PodcastCommand podcastCommand = new PodcastCommand(data);
		options.put("podcast 9", podcastCommand);
		options.put("podcast <aaaaaaaa> 1", new ListEpisodes(data));
		options.put("podcast <aaaaaaaa> 2", new UpdatePodcast(data));
		options.put("podcast <aaaaaaaa> 3", new DeletePodcast(data));
		options.put("podcast <aaaaaaaa> 4", new ChangeDestination(data));
		options.put("podcast <aaaaaaaa> 5", new AutoQueueEpisodes(data));
		options.put("podcast <aaaaaaaa> episode <aa>", new SelectEpisode(data));		
		options.put("podcast showmenu", new com.mimpidev.podsalinan.cli.options.podcast.ShowMenu(data));
		options.put("podcast <a-z>", podcastCommand);
		ShowSelectedMenu showSelectedPodcastMenu =new ShowSelectedMenu(data); 
		options.put("podcast <aaaaaaaa>", showSelectedPodcastMenu);
		options.put("podcast <aaaaaaaa> showmenu", showSelectedPodcastMenu);
		options.put("downloads <downloadId>", new DownloadsCommand(data));
		options.put("downloads showmenu", new com.mimpidev.podsalinan.cli.options.downloads.ShowMenu(data));
		options.put("settings", new SettingsCommand(data));
		MainMenuCommand mainMenuCommands = new MainMenuCommand(data);
		options.put("mainmenu showmenu", new com.mimpidev.podsalinan.cli.options.mainmenu.ShowMenu(data));
		options.put("mainmenu <0-9>", mainMenuCommands);
	}

	//TODO: 1. Moving the command line menu around again. Move all of the child options to here
	//TODO: 2. Rewrite menu traversal
	//TODO: 3. remove all debug=true
	/* TODO: 4. Rewrite user input to allow command line completion. Current thoughts on how to
	 * implement this:
	 *       Pass the the command string, and the method into a method of CLInput.
	 *       ie: userInput.addCommand("select podcast <userInput>",selectPodcastInput());
	 *       
	 * Then when user input is entered, and the user presses tab, it will either complete,
	 *   or list options.
	 */
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
				String menuInput=(returnObject.methodCall.length()>0?returnObject.methodCall+" ":"")+input.getStringInput();
				if ((menuInput.length()>0)&&(menuInput!=null)){
					returnObject.methodCall=menuInput;
					returnObject.execute=true;
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
	
	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
    	
		while (returnObject.execute){
			if (!options.containsKey(returnObject.methodCall)){
				int score=0;
				boolean match=false;
	        	returnObject.debug(debug);
				String[] methodCallSplit = returnObject.methodCall.split(" ");
	        	for (String key : options.keySet()){
					score=0;
					returnObject.parameterMap.clear();
	        		String[] splitValue = key.split(" ");
	        		if (splitValue.length==methodCallSplit.length){
	        			int svc=0;
	        			while (svc<splitValue.length && score<splitValue.length && !match){
	        				if (splitValue[svc].startsWith("<")&&
	        					splitValue[svc].endsWith(">")){
	        					if ((splitValue[svc].matches("^<url>") &&
	        						 methodCallSplit[svc].matches("\\b(https?|ftp):.*"))||
	        						(splitValue[svc].matches("^\\<((download|podcast)Id|downloadId\\|podcastId)\\>") &&
	    	        				 methodCallSplit[svc].matches("^[a-fA-F0-9]{8}") &&
	    	        				 !methodCallSplit[svc].equalsIgnoreCase("showmenu"))){
		        					if (splitValue[svc].matches("^<url>")){
		        						returnObject.parameterMap.put("url", methodCallSplit[svc]);
		        					} else if (splitValue[svc].matches("^\\<((download|podcast)Id|downloadId\\|podcastId)\\>")){
		        						returnObject.parameterMap.put("uid", methodCallSplit[svc]);
		        					}
	        						score++;
	        					}
	        					//TODO:NEXT - podcast a is not matching here when it should
	        					if (splitValue[svc].matches("<a-z>") &&
	        						methodCallSplit[svc].matches("[a-zA-Z]")){
	        						returnObject.parameterMap.put("userInput", methodCallSplit[svc]);
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
	        				returnObject.methodCall=key;
	        				returnObject.execute=true;
	        				if (debug) Podsalinan.debugLog.logInfo(this, 254, "matched");
	        				break;
	        			}
	        		}
	        	}
	            if (!match){
	            	returnObject.execute=false;
    				if (debug) Podsalinan.debugLog.logInfo(this, 252, "not matched");
	            }
	        }
			// This is going to traverse the main menu
			if (!options.containsKey(returnObject.methodCall)){
				if (returnObject.methodCall.matches("[0-9]{1}")){
					returnObject.parameterMap.put("menuItem", returnObject.methodCall);
					returnObject.methodCall="mainmenu <0-9>";
					returnObject.execute=true;
				}
			}
			if (debug) Podsalinan.debugLog.logInfo(this, "Calling requested function: "+returnObject.methodCall);
			if (returnObject.parameterMap.size()==0){
				returnObject.parameterMap=cliGlobals.getGlobalSelection();
			}
			returnObject.debug(true);
			returnObject=options.get(returnObject.methodCall.toLowerCase()).execute(returnObject.parameterMap);
		}
		return returnObject;
	}
}
