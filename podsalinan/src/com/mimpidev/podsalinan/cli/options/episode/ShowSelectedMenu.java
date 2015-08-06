/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.episode;

import java.util.Map;

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
	public ReturnObject execute(Map<String, String> functionParms) {
		debug=true;
		if (debug) Podsalinan.debugLog.logInfo(this,"Called");
		if (debug) Podsalinan.debugLog.logMap(functionParms);

		if (functionParms.containsKey("uid") && functionParms.containsKey("episode")){
			
		}
		/*if (commandOptions.length>1){
			int episodeNum = -1;
            if (commandOptions[1].equalsIgnoreCase("episode"))
               episodeNum = convertCharToNumber(commandOptions[2]);
            else
               episodeNum = convertCharToNumber(commandOptions[1]);*/
            
            Episode episode = //getEpisode(commandOptions[0],episodeNum);
            if (episode!=null){
				ShowEpisodeDetails printDetails = new ShowEpisodeDetails(data);
				//printDetails.execute(command);
				
				System.out.println ();
				System.out.println ("1. Download episode");
				System.out.println ("2. Delete episode from drive");
				System.out.println ("3. Cancel download of episode");
				System.out.println ("4. Change Status");
				System.out.println ();
				System.out.println ("9. Return to Podcast Menu");
				System.out.println ();
				
				returnObject.methodCall = "podcast";
				returnObject.parameterMap.clear();
				returnObject.execute=false;
			}
		//}
		
		return returnObject;
	}

}
