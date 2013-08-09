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

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.Timer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

@SuppressWarnings({ "serial", "unused" })
public class Podsalinan {
	
	private URLDownloadList urlDownloads;
	private Vector<Podcast> podcasts;
	private Vector<ProgSettings> progSettings;
	private CommandPass commands;
	private DownloadQueue downloaderList;
	
	/**
	 * Upon execution the program will create a new instance of bgdownloader, which is where
	 * most of the work happens. Creating the new instance builds the main window.
	 * 
	 * @param args
	 *   Nothing to pass in just yet
	 */
	public static void main(String[] args) {
		Podsalinan mainProgram = new Podsalinan();
		mainProgram.initialize();
		mainProgram.backgroundProcess();
	}

	private void backgroundProcess() {
		
		// List the podcast titles.
		for (Podcast podcast : podcasts)
			System.out.println("Podcast Name: "+podcast.getName());
		
		for (ProgSettings setting : progSettings)
			System.out.println(setting.setting+" - "+setting.value);
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
		DataStorage dataFiles = new DataStorage();
		
		// load the program settings
		dataFiles.loadSettings (podcasts, null, urlDownloads, progSettings);
		// Load the podcast data
		for (Podcast podcast : podcasts)
			dataFiles.loadPodcast(podcast);
	}
}
