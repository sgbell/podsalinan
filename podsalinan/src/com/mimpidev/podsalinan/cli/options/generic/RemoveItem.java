/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.generic;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInterface;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.cli.options.downloads.DeleteDownload;
import com.mimpidev.podsalinan.cli.options.podcast.DeletePodcast;

/**
 * @author sbell
 *
 */
public class RemoveItem extends CLIOption {

	/**
	 * @param newData
	 */
	public RemoveItem(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		if (debug) Podsalinan.debugLog.logMap(this, functionParms);
		
		if (functionParms.isEmpty()){
			String[] deleteType = {"downloads","podcastid"};
			boolean found=false;
			for (String deleteItem: deleteType){
				if (debug) Podsalinan.debugLog.logInfo(this, "Check: "+deleteItem);
				if (!found && CLInterface.cliGlobals.getGlobalSelection().containsKey(deleteItem)){
				    if (debug) Podsalinan.debugLog.logInfo(this, "Global Selection contains: "+deleteItem);
				    if (deleteItem.equals("downloads"))
				    	returnObject.methodCall="remove download";
				    else {
				    	returnObject.methodCall="remove podcast";
				    }
				    returnObject.execute=true;
				    found=true;
				}
			}
			if (!found){
				System.out.println("Error: Invalid User Input, no Podcast or Download currently selected");
				returnObject.execute=true;
			}
		} else {
			if (functionParms.containsKey("uid")){
				returnObject.methodCall="podcast "+functionParms.get("uid")+" 3";
				returnObject.execute=true;
			} else if (functionParms.containsKey("")){
				returnObject.methodCall="downloads "+functionParms.get("")+" 1";
				returnObject.execute=true;
			} else {
				System.out.println("Error: Invalid User Input");
				returnObject.execute=true;
			}
		}
		
		if (debug) Podsalinan.debugLog.logInfo(this, "methodCall: "+returnObject.methodCall);
		
		return returnObject;
	}
}
