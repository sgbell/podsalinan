/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.episode;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;

/**
 * @author sbell
 *
 */
public class SelectEpisode extends CLIOption {

	public SelectEpisode(DataStorage newData) {
		super(newData);
		ShowSelectedMenu showMenu = new ShowSelectedMenu(newData);
		options.put("", showMenu);
		options.put("showmenu", showMenu);
		options.put("1", new DownloadEpisode(data));
		options.put("2", new DeleteEpisodeFromDrive(data));
		options.put("3", new CancelDownload(data));
		options.put("4", new ChangeStatus(data));
	}

	@Override
	public ReturnObject execute(String command) {
		debug=true;
		if (debug) Podsalinan.debugLog.logInfo(this,31,"Command: "+command);
		String[] commandOptions = command.split(" ");
		if (debug) Podsalinan.debugLog.logInfo(this, 36, "Length: "+commandOptions.length);
		
		// Find out where the episode id is in the command String
		int episodePosition=-1;
		if (commandOptions.length>2){
			if (commandOptions[2].equalsIgnoreCase("episode"))
				episodePosition=2;
		} else if (commandOptions.length==2){
			episodePosition=1;
		}
		
		// If episode 1 is identified (approximately) compare it with globalSelection array
		if (episodePosition!=-1){
			try {
				Integer.parseInt(commandOptions[episodePosition]);
			} catch (NumberFormatException e){
				if (((globalSelection.containsKey("episode"))&&
					 (!commandOptions[episodePosition].equalsIgnoreCase(globalSelection.get("episode"))))||
					(!globalSelection.containsKey("episode"))){
					globalSelection.put("episode", commandOptions[episodePosition]);
				}
			}
		}
		
        switch (commandOptions.length){
        	case 4:
        		if (commandOptions[3].equals("9")){
        			returnObject.methodCall="podcast";
        			returnObject.methodParameters=commandOptions[0];
        			returnObject.execute=true;
        			globalSelection.remove("episode");
        		} else if (commandOptions[1].equalsIgnoreCase("episode")){
            		try {
            			Integer.parseInt(commandOptions[3]);
            			if (debug) Podsalinan.debugLog.logInfo(this,45,"Command: "+command);
            			if (options.containsKey(commandOptions[3]))
            				returnObject = options.get(commandOptions[3]).execute(command);
            			else{
                			System.out.println("Error: Invalid option selected");
                			returnObject.methodCall="podcast";
                			returnObject.methodParameters=commandOptions[0]+" "+commandOptions[1]+" "+commandOptions[2];
            			}
            		} catch (NumberFormatException e){
            			System.out.println("Error: Invalid option selected");
            			returnObject.methodCall="podcast";
            			returnObject.methodParameters=commandOptions[0]+" "+commandOptions[1]+" "+commandOptions[2];
            		}
        		}
        		break;
        	case 2:
        		if (debug) Podsalinan.debugLog.logInfo(this, 60,"Command: "+command);

        		returnObject = options.get("showmenu").execute(command);
        		break;
        	default:
        }
		
		return returnObject;
	}

}
