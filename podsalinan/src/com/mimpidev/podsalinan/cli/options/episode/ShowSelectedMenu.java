/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.episode;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.dev.debug.Log;
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
		if (debug) if (Log.isDebug())Log.logMap(this, functionParms);
		Episode episode = null;

		if (functionParms.containsKey("uid") && functionParms.containsKey("userInput")){
			episode = this.getEpisode(functionParms.get("uid"), functionParms.get("userInput"));
		} 
        if (episode!=null){
			if (data.getSettings().getSettingValue("menuVisible").equalsIgnoreCase("true")){
				ShowEpisodeDetails printDetails = new ShowEpisodeDetails(data);
				functionParms.put("menuCalled", "true");
				printDetails.execute(functionParms);
				
				System.out.println ();
				System.out.println ("1. Download episode");
				System.out.println ("2. Delete episode from drive");
				System.out.println ("3. Cancel download of episode");
				System.out.println ("4. Change Status");
				System.out.println ();
				System.out.println ("9. Return to Podcast Menu");
				System.out.println ();
			}
				
			returnObject.methodCall = "podcast "+getPodcast().getDatafile()+" episode "+functionParms.get("userInput");
			returnObject.parameterMap.clear();
			returnObject.execute=false;
		}
            
		
		return returnObject;
	}

}
