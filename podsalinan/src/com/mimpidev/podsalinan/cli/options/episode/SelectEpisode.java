/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.episode;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInterface;
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
	public ReturnObject execute(Map<String, String> functionParms) {
		String command="";
		debug=true;
		if (debug) Podsalinan.debugLog.logInfo(this,31,"Command: "+command);
		String[] commandOptions = command.split(" ");
		if (debug) Podsalinan.debugLog.logInfo(this, 36, "Length: "+commandOptions.length);
		
		// Find out where the episode id is in the command String
		int episodePosition=-1;
		if (commandOptions.length>2){
			int posCount=0;
			for (String temp : commandOptions){
				if (temp.equalsIgnoreCase("episode")){
					episodePosition=posCount+1;
				} else {
					posCount++;
				}
			}
		} else if (commandOptions.length==2){
			episodePosition=1;
		}

		if (debug) Podsalinan.debugLog.logInfo(this, "episodePosition:"+episodePosition);
		// If episode 1 is identified (approximately) compare it with globalSelection array
		if (episodePosition!=-1){
			try {
				Integer.parseInt(commandOptions[episodePosition]);
			} catch (NumberFormatException e){
				if (((CLInterface.cliGlobals.getGlobalSelection().containsKey("episode"))&&
					 (!commandOptions[episodePosition].equalsIgnoreCase(CLInterface.cliGlobals.getGlobalSelection().get("episode"))))||
					(!CLInterface.cliGlobals.getGlobalSelection().containsKey("episode"))){
					CLInterface.cliGlobals.getGlobalSelection().put("episode", commandOptions[episodePosition]);
				}
			}
		}
		
        switch (commandOptions.length){
        	case 4:
        		if (commandOptions[episodePosition+1].equals("9")){
        			CLInterface.cliGlobals.getGlobalSelection().remove("episode");
                    returnObject = CLInterface.cliGlobals.createReturnObject();
        			returnObject.execute=true;
        		} else {
            		try {
            			Integer.parseInt(commandOptions[episodePosition+1]);
            			if (debug) Podsalinan.debugLog.logInfo(this,76,"Command: "+command);
            			if (options.containsKey(commandOptions[episodePosition+1])){
            				//returnObject = options.get(commandOptions[episodePosition+1]).execute(command);
            			} else{
                			System.out.println("Error: Invalid option selected");
                			returnObject.methodCall="podcast";
                			for (String option: commandOptions){
                				/*if (returnObject.methodParameters.length()>0){
                					returnObject.methodParameters+=" ";
                				}
                			    returnObject.methodParameters+=option;*/
                			}
            			}
            		} catch (NumberFormatException e){
            			System.out.println("Error: Invalid option selected");
            			returnObject.methodCall="podcast";
            			//returnObject.methodParameters=commandOptions[0]+" "+commandOptions[1]+" "+commandOptions[2];
            		}
        		}
        		break;
            case 3:
        	case 2:
        		if (debug) Podsalinan.debugLog.logInfo(this, 97,"Command: "+command);

        		//returnObject = options.get("showmenu").execute(command);
        		break;
        	default:
        }
		
		return returnObject;
	}

}
