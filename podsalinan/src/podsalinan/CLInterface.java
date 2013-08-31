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
	private Vector<ProgSettings> progSettings;
	private Object waitObject = new Object();
	private CLInput input;

	public CLInterface(Vector<Podcast> podcasts, URLDownloadList urlDownloads, Vector<ProgSettings> progSettings){
		this.podcasts=podcasts;
		this.urlDownloads=urlDownloads;
		this.progSettings=progSettings;
		input = new CLInput();
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

	@Override
	public void run() {
		ArrayList<Integer> menuSelection = new ArrayList<Integer>();
		
		System.out.println("Welcome to podsalinan.");
		System.out.println("----------------------");
		while (!finished){
			switch (menuSelection.size()){
				case 0:
				default:
					// No menu selection made
					printMainMenu();
					break;
				case 1:
					// First menu selection made
					switch (menuSelection.get(0)){
						case 0:
							break;
						case 1:
							printPodcastMenu();
							break;
						case 2:
							printDownloadsMenu();
							break;
						case 3:
							printPreferencesMenu();
							break;
						case 4:
							finished=true;
							break;
					}
					break;
				case 2:
					// Sub menu
					switch (menuSelection.get(0)){
						case 1:
							if (menuSelection.get(1)<0){
								switch ((0-menuSelection.get(1))){
									case 9:
										menuSelection.clear();
										printMainMenu();
										break;
								}
							} else
								if ((menuSelection.get(1)<podcasts.size())&&
									(menuSelection.get(1)>=0))
									printPodcastSubmenu(menuSelection.get(1));
								else{
									printPodcastMenu();
									menuSelection.remove(1);
								}
							break;
						case 2:
							
							break;
						case 3:
							switch (menuSelection.get(1)){
								case 1:
									printPreferencesList();
									break;
								case 0:
									menuSelection.clear();
									printMainMenu();
									break;
							}
							break;
						case 4:
							
							break;
					}
					break;
				case 3:
					switch (menuSelection.get(0)){
						case 1:
							if (menuSelection.get(2)<0){
								switch ((0-menuSelection.get(2))){
									case 1:
										// List Episodes of a Podcast
										printPodcastEpisodeList(menuSelection.get(1));
										break;
									case 2:
										// Update episode List of Podcast
										Podcast podcast = podcasts.get(menuSelection.get(1));
										String tempDir="";
										// This if Block checks to see if it's windows or linux, and sets the
										// settings directory appropriately.
										if (System.getProperty("os.name").equalsIgnoreCase("linux"))
											tempDir = System.getProperty("user.home").concat("/.podsalinan");
										else if (System.getProperty("os.name").startsWith("Windows"))
											tempDir = System.getProperty("user.home").concat("\\appdata\\local\\podsalinan");
											
										podcast.updateList(tempDir);
										break;
									case 3:
										// Remove Podcast
										if ((menuSelection.get(1)<podcasts.size())&&
											(menuSelection.get(1)>=0)){
											podcasts.get(menuSelection.get(1)).setRemove(true);
											menuSelection.remove(2);
											menuSelection.remove(1);
											printPodcastMenu();
										}
										break;
									case 9:
										// Need to remove two items, as the menuSelection will contain
										// -9 for going up a menu, and the podcast number.
										menuSelection.remove(2);
										menuSelection.remove(1);
										printPodcastMenu();
										break;
								}
								if ((menuSelection.size()==3)&&
									(menuSelection.get(2)<3)){
									menuSelection.remove(2);
									printPodcastSubmenu(menuSelection.get(1));
								}
							} else 
								printEpisodeMenu(menuSelection);
							break;
						case 2:
							break;
						case 3:
							switch (menuSelection.get(2)){
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
								case 9:
									
									break;
							}
							menuSelection.remove(2);
							break;
						case 4:
							
							break;
					}
					break;
				case 4:
					switch (menuSelection.get(0)){
						case 1:
							switch (menuSelection.get(3)){
								case 1:
									setEpisodeStatus(menuSelection,Episode.CURRENTLY_DOWNLOADING);
									break;
								case 2:
									deleteEpisode(menuSelection);
									setEpisodeStatus(menuSelection,Episode.NOT_STARTED);
									break;
								case 3:
									setEpisodeStatus(menuSelection,Episode.NOT_STARTED);
									break;
								case 9:
									menuSelection.remove(3);
									menuSelection.remove(2);
									printPodcastSubmenu(menuSelection.get(1));
									break;
							}
							if ((menuSelection.size()==4)&&
								(menuSelection.get(3)<=3)){
								menuSelection.remove(3);
								printEpisodeMenu(menuSelection);
							}
							break;
					}
					break;
			}
			
			if (!finished){
				System.out.print("Debug:");
				for (Integer i : menuSelection)
					System.out.print(i+"-");
				System.out.println();
				
				System.out.print("->");
				String menuInput=input.getStringInput();
				if (menuInput.length()>0){
					try {
						int inputInt = Integer.parseInt(menuInput);
						if (((menuSelection.size()==1)&&(menuSelection.get(0)==1))||
							((menuSelection.size()==2)&&(menuSelection.get(0)==1)))
							// Because the podcasts are stored in menuSelection as integers too,
							// the menu options will be stored as negative values
							menuSelection.add((0-inputInt));
						else
							menuSelection.add(inputInt);
					} catch (NumberFormatException e){
						// If the input is not a number This area will sort out that code
						if ((menuInput.equalsIgnoreCase("quit"))||
							(menuInput.equalsIgnoreCase("exit"))){
							finished=true;
						} else if ((menuInput.startsWith("http"))||
								   (menuInput.startsWith("ftp"))){
							System.out.println("url detected");
						} else 
							switch (menuSelection.size()){
								case 1:
									if ((menuSelection.get(0)==1)&&(menuInput.length()<3)){
										int podcastNumber=convertCharToNumber(menuInput);
																		
										if ((podcastNumber>podcasts.size())&&
											(podcastNumber<0))
											System.out.println("Error: Invalid Podcast");
										else
											menuSelection.add(podcastNumber);
									}
									break;
								case 2:
									if ((menuSelection.get(0)==1)&&
										(menuInput.length()<3)){
										Podcast podcast = podcasts.get(menuSelection.get(1));
										int episodeNumber=convertCharToNumber(menuInput);
										
										if ((episodeNumber>podcast.getEpisodes().size())&&
											(episodeNumber<0))
											System.out.println("Error: Invalid Episode");
										else
											menuSelection.add(episodeNumber);
									}
									break;
								default:
									System.out.println("Error: Invalid input");
									break;
							}
					}
				}
			}
		}
		System.out.println("Please Standby for system Shutdown.");
		synchronized (waitObject){
			waitObject.notify();
		}
	}
	
	private void changeAutoQueueNewEpisodes() {
		
	}

	public void printMainMenu(){
		System.out.println(podcasts.size()+" - Podcasts. "+urlDownloads.getDownloads().size()+" - Downloads Queued");
		System.out.println();
		System.out.println("1. Podcasts Menu");
		System.out.println("2. Downloads Menu");
		System.out.println("3. Preferences");
		System.out.println("4. Quit");
	}
	
	private void printPodcastMenu() {
		int podcastCount=1;
		
		for (Podcast podcast : podcasts){
			if (!podcast.isRemoved())
				System.out.println(getEncodingFromNumber(podcastCount)+". "+podcast.getName());
			podcastCount++;
		}
		
		System.out.println();
		System.out.println("(A-Z) Enter Podcast letter to select Podcast.");
		System.out.println();
		System.out.println("9. Return to Main Menu");
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
		}
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
		System.out.println();
		System.out.println("1. List Preferences");
		System.out.println("0. Return to Main Menu");
	}
	
	private void printPreferencesList(){
		System.out.println ();
		System.out.println ("1. Change Podcast Update Rate");
		System.out.println ("2. Number of Downloaders");
		System.out.println ("3. Default Download Directory");
		System.out.println ("4. Automatically Download New Podcast Episodes");
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
				updateProgSettings("defaultDirectory",userInput);
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
			updateProgSettings("maxDownloaders",numDownloaders);
	}

	/**
	 * 
	 * @param setting
	 * @param value
	 */
	private void updateProgSettings(String setting, String value){
		boolean found=false;
		int count=0;
		while ((count<progSettings.size())&&
			   (!found)){
			if (progSettings.get(count).setting.equalsIgnoreCase(setting)){
				progSettings.get(count).value=value;
				found=true;
			}
			count++;
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
			updateProgSettings("updateInterval",updateValue);
			waitObject.notify();
		}
	}

	private void printDownloadsMenu() {
		System.out.println();
		System.out.println("1. List Downloads");
		System.out.println("0. Return to Main Menu");
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

	private int convertCharToNumber(String input) {
		int number=1;
		if (input.length()>1){
			for (int charCount=0; charCount < input.length()-1; charCount++)
				number=number*26*(int)(input.toUpperCase().charAt(charCount)-64);
			number+=(int)(input.toUpperCase().charAt(input.length()-1)-64);
		} else if (input.length()==1)
			number=(int)(input.toUpperCase().charAt(0)-64);
		number--;
		
		return number;
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
