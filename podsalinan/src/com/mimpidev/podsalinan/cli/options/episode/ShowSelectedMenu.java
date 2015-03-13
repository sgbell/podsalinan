/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.episode;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.data.Episode;

/**
 * @author sbell
 *
 */
public class ShowSelectedMenu extends BaseEpisodeOption {

	public ShowSelectedMenu(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo(this.getClass().getName()+":"+command);
		String[] commandOptions = command.split(" ");
		if (commandOptions.length>1){
			int episodeNum = -1;
            if (commandOptions[1].equalsIgnoreCase("episode"))
               episodeNum = convertCharToNumber(commandOptions[2]);
            else
               episodeNum = convertCharToNumber(commandOptions[1]);
            
            Episode episode = getEpisode(commandOptions[0],episodeNum);
            if (episode!=null){
				
				System.out.println ("Podcast: "+getPodcast().getName());
				System.out.println ("Episode: "+episode.getTitle());
				System.out.println ("Date: "+episode.getDate());
				System.out.println ("Status: "+episode.getCurrentStatus());
				System.out.println ();
				System.out.println ("1. Download episode");
				System.out.println ("2. Delete episode from drive");
				System.out.println ("3. Cancel download of episode");
				System.out.println ("4. Change Status");
				System.out.println ();
				System.out.println ("9. Return to Podcast Menu");
				System.out.println ();
				
				returnObject.methodCall = "podcast";
				returnObject.methodParameters = command;
			}
		}
		
		return returnObject;
	}

}
