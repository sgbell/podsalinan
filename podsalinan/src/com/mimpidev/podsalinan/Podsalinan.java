/*******************************************************************************
 * Copyright (c) 2011 Sam Bell.
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
 *   podsalinan is a project written to 
 *   1, get me familiar with java again. 
 *   2, create a program that will download any url it is given, as well as podcasts from
 *   rss feeds that a user sources.
 *   Podsalinan - loosely translated pod copy
 *   
 *   Written By: Sam Bell
 *   
 */
package com.mimpidev.podsalinan;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.mimpidev.dev.debug.Log;
import com.mimpidev.podsalinan.cli.CLInterface;
import com.mimpidev.podsalinan.data.Podcast;

public class Podsalinan {
	//private CommandPass commands;
	private DownloadQueue downloaderList;
	private DataStorage data;
	private CLInterface cli;
	//private MainWindow gui;
	private String[] cmdLineArgs;
    /**
     *  debugLog is going to be handed through all of the system so we can write to the debug log
     */
	public static final Log debugLog = new Log();
	
	/**
	 * Upon execution the program will create a new instance of podsalinan, which is where
	 * most of the work happens. Creating the new instance builds the main window.
	 * 
	 * @param args
	 *   Nothing to pass in just yet
	 */
	public static void main(String[] args) {
		debugLog.initialise();
		Podsalinan mainProgram = new Podsalinan();
		mainProgram.setCmdLineArgs(args);
		mainProgram.initialize();
		mainProgram.backgroundProcess();
	}

	private void backgroundProcess() {
		int updateInterval=0;
		
		// Find update Interval for podcasts in program settings.
		try {
			updateInterval=Integer.parseInt(data.getSettings().getSettingValue("updateInterval"));
		} catch (NumberFormatException e){
			updateInterval=0;
		} catch (NullPointerException e){
			updateInterval=0;
		}
		if (updateInterval<60){
			data.getSettings().updateSetting("updateInterval", "60");
			updateInterval=60;
		}
		
		while(!data.getSettings().isFinished()){
			// List the podcast titles.
			for (Podcast podcast : data.getPodcasts().getList()){
				/*if ((!data.getSettings().isFinished())&&(!podcast.isRemoved())){
					podcast.updateList(data.getSettingsDir());
					podcast.updateDatabase();
				}*/
				
				// The following will scan the directory for already downloaded episodes of the podcast and mark them as downloaded
				podcast.scanDirectory(data);

				/* If autoQueue is set in the program settings to true, or autoQueue is set in the podcast,
				 * scan the podcast lists for episodes not yet downloaded, and queue them to download. 
				 */
				/*if (((data.getSettings().findSetting("autoQueue")!=null)&&
						 (data.getSettings().findSetting("autoQueue").equalsIgnoreCase("true")))||
						 (podcast.isAutomaticQueue())){
						Vector<Episode> podcastEpisodes = podcast.getEpisodesByStatus(Details.NOT_QUEUED);
						if (podcastEpisodes.size()>0)
							for (Episode episode : podcastEpisodes){
								episode.setStatus(Details.CURRENTLY_DOWNLOADING);
								data.getUrlDownloads().addDownload(episode, podcast);
							}
					}*/
			}
				
			// Put this thread to sleep till it is next woken up to check for updates in the podcasts
			try {
				synchronized (data.getSettings().getWaitObject()){
					if (!data.getSettings().isFinished())
						// updateInterval will be a multiple of 1 minute
						data.getSettings().getWaitObject().wait(updateInterval*60000);
				}
			} catch (InterruptedException e) {
			}
		}
		while (!downloaderList.isFinished()){
			synchronized (data.getFinishWait()){
				try {
					data.getFinishWait().wait();
				} catch (InterruptedException e) {
				}
			}
		}
			
		data.saveSettings();
		if (Podsalinan.debugLog!=null)
		   Podsalinan.debugLog.close();
		System.out.println("Goodbye.");
		// added line below so program exits, as having gui changes things
		System.exit(0);
	}

	public Podsalinan(){
		// Moved the 3 lines below to DataStorage.
		// podcasts = new Vector<Podcast>();
		// URL Downloads
		// urlDownloads = new URLDownloadList(podcasts);
		// Program Settings
		// settings = new ProgSettings();
		
		// Action Listener for the main window
		//CommandPass aListener = new CommandPass(settings);

		// disables sqlite4java's logging
		Logger.getLogger("com.almworks.sqlite4java").setLevel(Level.OFF);
	}
	
	public void initialize(){
		boolean showCli=false,
		        showGui=false;
		
		data = new DataStorage();
		
		// load the program settings and data
		data.loadSettings ();

		// Downloader List
		downloaderList = new DownloadQueue(data);
		Thread downloadListThread = new Thread(downloaderList);
		downloadListThread.start();

		if (!data.getSettings().isValidSetting("interface"))
			showCli=true;
		else{
			if ((data.getSettings().findSetting("interface").equalsIgnoreCase("cli")))
				showCli=true;
			if (data.getSettings().findSetting("interface").equalsIgnoreCase("gui"))
				showGui=true;
		}
		for (String arg : cmdLineArgs){
			if (arg.contentEquals("--show-gui"))
				showGui=true;
			if (arg.contentEquals("--show-cli"))
				showCli=true;
		}
		
		if (showCli){
			cli = new CLInterface(data);
			Thread cliThread = new Thread(cli);
			cliThread.start();
		}
		
		if (showGui){
			//gui = new MainWindow(data);
			
		}
	}

	/**
	 * @return the cmdLineArgs
	 */
	public String[] getCmdLineArgs() {
		return cmdLineArgs;
	}

	/**
	 * @param cmdLineArgs the cmdLineArgs to set
	 */
	public void setCmdLineArgs(String[] cmdLineArgs) {
		this.cmdLineArgs = cmdLineArgs;
	}
}
