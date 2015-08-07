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
		Episode episode = null;

		if (functionParms.containsKey("uid") && functionParms.containsKey("episode")){
			episode = this.getEpisode(functionParms.get("uid"), functionParms.get("episode"));
		} 
        if (episode!=null){
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
				
				returnObject.methodCall = "podcast "+getPodcast().getDatafile()+" episode ";
				returnObject.methodCall+=functionParms.get("episode");
				returnObject.parameterMap.clear();
				returnObject.execute=false;
			}
            
		
		return returnObject;
	}

}
