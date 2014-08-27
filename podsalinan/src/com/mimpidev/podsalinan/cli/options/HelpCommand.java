/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import java.util.HashMap;
import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;

/**
 * @author sbell
 *
 */
public class HelpCommand extends CLIOption {

	private Map<String, CLIOption> options;
	/**
	 * @param newData
	 */
	public HelpCommand(DataStorage newData) {
		super(newData);
		options = new HashMap<String, CLIOption>();
		
	}

	@Override
	public void execute(String command) {
		System.out.println("");
		
		if (command.equalsIgnoreCase("help")){
			// Main help text
			System.out.println("A url is accepted at almost any time to add it to the download queue.");
			System.out.println("It will have to start with (http/https/ftp):// for the system to download it.");
			System.out.println("");
			System.out.println("root commands");
			System.out.println("");
			System.out.println("   help <command>                 will show this screen");
			System.out.println("   select                         used to select podcast/episode/queued download");
			System.out.println("   set <preference name> <value>  used for changing system preferences");
			System.out.println("   list                           used for listing podcasts/episodes/queued downloads/preferences");
			System.out.println("   hide menu                      used to disable the menu");
			System.out.println("   show menu                      used to enable the menu");
			System.out.println("   show details                   used to show details of podcast/episode/queued download");
			System.out.println("");
			System.out.println("podcast specific commands");
			System.out.println("   update                         used to force an update");
			System.out.println("   download <episode number>      used to download an episode");
			System.out.println("   set podcast directory <path>   used to change download directory");
			System.out.println("");
			System.out.println("download specific commands");
			System.out.println("   increase <download number>     used to move download up in the list");
			System.out.println("   decrease <download number>     used to move download down in the list");
			System.out.println("   remove <download number>       used to cancel download");
			System.out.println("   restart <download number>      used to restart download");
			System.out.println("   stop <download number>         used to stop download");
			System.out.println("   set destination <path>         used to change download directory");
			System.out.println("");
			System.out.println("Commands to exit the program");
			System.out.println("   quit");
			System.out.println("   exit");
		} else if (menuInput.toLowerCase().contains("select")){
			// If user enters "help select"
			System.out.println("select is used to traverse around the system, when not using the menu");
			System.out.println("");
			System.out.println("   select podcast <podcast name>       this will select the podcast");
			System.out.println("            If the podcast name is not exact, the system will try to guess the podcast.");
			System.out.println("   select podcast <podcast number>     this will select the podcast");
			System.out.println("   select episode <episode number>     this will select the episode");
			System.out.println("            If the podcast is not selected this will tell you to select a podcast first.");
			System.out.println("   select download <download number>   this will select the download");
		} else if (menuInput.toLowerCase().contains("list")){
			// If user enters "help list"
		    // Show sub commands for list command
			System.out.println("list can have the following parameters");
			System.out.println("");
			System.out.println("   list podcast           show list of podcasts to the screen");
			System.out.println("   list episodes          show list of episodes to the screen, if a podcast has been selected");
			System.out.println("   list downloads         show list of queued downloads to the screen");
			System.out.println("   list preferences       show list of preferences to the screen");
			System.out.println("   list select            show list of current selection made to the screen");
			System.out.println("   list details           show details about currently selected item to the screen");
		} else if (menuInput.toLowerCase().contains("set")){
			// If the user enters "help set"
			System.out.println("set is used to change settings");
			System.out.println("");
			System.out.println("   set <preference name> <value>  this will update the preference in the system");
		}
		System.out.println("");
		System.out.println("");
	}

}
