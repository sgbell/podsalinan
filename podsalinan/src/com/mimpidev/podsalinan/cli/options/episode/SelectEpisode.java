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
		/* TODO: working here to insert the episode details into the globalSelection array and also compare it with
		 * globalSelection array. No point chasing it, if we already have it.
		 */
		String[] commandOptions = command.split(" ");
		if (debug) Podsalinan.debugLog.logInfo(this, 36, "Length: "+commandOptions.length);
        switch (commandOptions.length){
        	case 4:
        		if (commandOptions[3].equals("9")){
        			returnObject.methodCall="podcast";
        			returnObject.methodParameters=commandOptions[0];
        			returnObject.execute=true;
        			globalSelection.remove("episode");
        		} else {
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
        		if ((!globalSelection.containsKey("episode"))||
      				((globalSelection.containsKey("episode"))&& 
      				 (!globalSelection.get("episode").equalsIgnoreCase(commandOptions[1])))){
        			globalSelection.put("episode", commandOptions[1]);
        		}
    			returnObject = options.get("showmenu").execute(command);
        		break;
        	default:
        }
		
		return returnObject;
	}

}
