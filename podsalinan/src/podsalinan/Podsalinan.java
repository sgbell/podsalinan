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
 *   podsalinan is a project written to 1, get me familiar with java again. and 2, create a
 *   program that will download any url it is given, as well as podcasts from rss feeds that a user
 *   sources.
 *   Podsalinan - loosely translated pod copy
 *   
 *   Written By: Sam Bell
 *   
 */
package podsalinan;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Podsalinan {
	
	private URLDownloadList urlDownloads;
	private Vector<Podcast> podcasts;
	private Vector<ProgSettings> progSettings;
	private CommandPass commands;
	private DownloadQueue downloaderList;
	private DataStorage dataFiles;
	
	/**
	 * Upon execution the program will create a new instance of podsalinan, which is where
	 * most of the work happens. Creating the new instance builds the main window.
	 * 
	 * @param args
	 *   Nothing to pass in just yet
	 */
	public static void main(String[] args) {
		Podsalinan mainProgram = new Podsalinan();
		mainProgram.initialize();
		mainProgram.backgroundProcess();
		mainProgram.exit();
	}

	private void exit() {
		dataFiles.saveSettings(podcasts, null, urlDownloads, progSettings);
	}

	private void backgroundProcess() {
		int updateInterval=0;
		
		// Find update Interval for podcasts in program settings.
		for (ProgSettings setting : progSettings)
			if(setting.setting.equalsIgnoreCase("updateInterval"))
				updateInterval=Integer.parseInt(setting.value);
		if (updateInterval<60){
			ProgSettings setting = new ProgSettings("updateInterval","60");
			progSettings.add(setting);
			updateInterval=60;
		}
		
		// List the podcast titles.
		for (Podcast podcast : podcasts){
			podcast.updateList(dataFiles.getSettingsDir());
			
			dataFiles.savePodcast(podcast);
		}
	}

	public Podsalinan(){
		podcasts = new Vector<Podcast>();
		// URL Downloads
		urlDownloads = new URLDownloadList();
		// Program Settings
		progSettings = new Vector<ProgSettings>();
		
		// Action Listener for the main window
		CommandPass aListener = new CommandPass(progSettings);

		// disables sqlite4java's logging
		Logger.getLogger("com.almworks.sqlite4java").setLevel(Level.OFF);
	}
	
	public void initialize(){
		dataFiles = new DataStorage();
		
		// load the program settings
		dataFiles.loadSettings (podcasts, null, urlDownloads, progSettings);
		// Load the podcast data
		for (Podcast podcast : podcasts)
			dataFiles.loadPodcast(podcast);
	}
}
