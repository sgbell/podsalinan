/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.episode;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ObjectCall;

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
	public ObjectCall execute(String command) {
		debug=true;
		if (debug) Podsalinan.debugLog.logInfo(this.getClass().getName()+":"+command);
		String[] commandOptions = command.split(" ");
        switch (commandOptions.length){
        	case 3:
        		if (commandOptions[2].equals("9")){
        			returnObject.methodCall="podcast";
        			returnObject.methodParameters=commandOptions[0];
        		} else {
            		try {
            			Integer.parseInt(commandOptions[2]);
            			if (debug) Podsalinan.debugLog.logInfo(this.getClass().getName()+":"+command);
            			if (options.containsKey(commandOptions[2]))
            				returnObject = options.get(commandOptions[2]).execute(command);
            			else{
                			System.out.println("Error: Invalid option selected");
                			returnObject.methodCall="podcast";
                			returnObject.methodParameters=commandOptions[0]+" "+commandOptions[1];
            			}
            		} catch (NumberFormatException e){
            			System.out.println("Error: Invalid option selected");
            			returnObject.methodCall="podcast";
            			returnObject.methodParameters=commandOptions[0]+" "+commandOptions[1];
            		}
        		}
        		break;
        	case 2:
    			returnObject = options.get("showmenu").execute(command);
        		break;
        	default:
        }
		
		return returnObject;
	}

}
