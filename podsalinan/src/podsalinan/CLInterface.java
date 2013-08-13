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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

	public CLInterface(Vector<Podcast> podcasts, URLDownloadList urlDownloads, Vector<ProgSettings> progSettings){
		this.podcasts=podcasts;
		this.urlDownloads=urlDownloads;
		this.progSettings=progSettings;
	}

	@Override
	public void run() {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		String input="";
		int menuItem=0,
		   	subMenu=0,
		   	selectedPodcast=-1;
		
		System.out.println("Welcome to podsalinan.");
		System.out.println("----------------------");
		while (!finished){
			switch (menuItem){
				case 0:
				default:
					printMainMenu();
					subMenu=0;
					break;
				case 1:
					switch (subMenu){
						case 0:
						default:
							printPodcastMenu();
							subMenu=1;
							break;
						case 1:
							break;
					}
					break;
				case 2:
					switch (subMenu){
						case 0:
						default:
							printDownloadsMenu();
							subMenu=2;
							break;
						case 1:
							break;
					}
					break;
				case 3:
					switch (subMenu){
						case 0:
						default:
							printPreferencesMenu();
							subMenu=3;
							break;
						case 1:
							break;
					}
					break;
				case 4:
					switch (subMenu){
						case 0:
							finished=true;
							break;
						case 1:
							break;
					}
					break;
			}
			if (!finished){
				System.out.print("->");
				try {
					input = bufferedReader.readLine(); 
				} catch (IOException e) {
				}
				if (input.length()>0){
					try {
						int inputInt = Integer.parseInt(input);
						menuItem=inputInt;
					} catch (NumberFormatException e){
						// If the input is not a number This area will sort out that code
						if ((input.equalsIgnoreCase("quit"))||
							(input.equalsIgnoreCase("exit"))){
							finished=true;
						} else if ((input.startsWith("http"))||
								   (input.startsWith("ftp"))){
							System.out.println("url detected");
						} else if ((subMenu==1) &&
								   (input.length()<3)){
							int finalNumber=0;
							if (input.length()>1){
								finalNumber=1;
								for (int charCount=0; charCount < input.length()-1; charCount++){
									finalNumber=finalNumber*26*(int)(input.toUpperCase().charAt(charCount)-64);
								}
								finalNumber=finalNumber+(int)(input.toUpperCase().charAt(input.length()-1)-64);
							} else if (input.length()==1){
								finalNumber=(int)(input.toUpperCase().charAt(0)-64);
							}
							selectedPodcast = finalNumber-1;
							if ((selectedPodcast<podcasts.size())&&
								(selectedPodcast>=0)){
								printPodcastSubmenu(selectedPodcast);
							}  else
								System.out.println("Error: Command not recognised");
						} else
							System.out.println("Error: Command not recognised");
					}
				}
			}
		}
		System.out.println("Please Standby for system Shutdown.");
		synchronized (waitObject){
			waitObject.notify();
		}
	}
	
	private void printPodcastSubmenu(int selectedPodcast) {
		System.out.println ("Podcast: "+podcasts.get(selectedPodcast).getName()+ " - Selected");
		
	}

	private void printPreferencesMenu() {
		System.out.println();
		System.out.println("1. List Preferences");
		System.out.println("0. Return to Main Menu");
	}

	private void printDownloadsMenu() {
		System.out.println();
		System.out.println("1. List Downloads");
		System.out.println("0. Return to Main Menu");
	}

	private void printPodcastMenu() {
		int podcastCount=1;
		
		for (Podcast podcast : podcasts){
			String podcastChar="";

			if (podcastCount<27)
				podcastChar = getCharForNumber(podcastCount);
			else {
				podcastChar.concat(getCharForNumber(podcastCount/26));
				podcastChar.concat(getCharForNumber(podcastCount%26));
			}
			
			System.out.println(podcastChar+". "+podcast.getName());
			podcastCount++;
		}
		
		System.out.println();
		System.out.println("1. List Podcasts");
		System.out.println("(A-Z) Enter Podcast letter to select Podcast.");
		System.out.println();
		System.out.println("0. Return to Main Menu");
	}

	public void printMainMenu(){
		System.out.println(podcasts.size()+" - Podcasts. "+urlDownloads.getDownloads().size()+" - Downloads Queued");
		System.out.println();
		System.out.println("1. Podcasts Menu");
		System.out.println("2. Downloads Menu");
		System.out.println("3. Preferences");
		System.out.println("4. Quit");
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
