/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.generic;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInput;
import com.mimpidev.podsalinan.cli.CLInterface;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.data.Podcast;
import com.mimpidev.podsalinan.data.URLDownload;

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
				Podcast selectedPodcast = data.getPodcasts().getPodcastByUid(functionParms.get("uid"));
				URLDownload selectedDownload=null;
				if (selectedPodcast !=null){
					returnObject.methodCall="podcast "+functionParms.get("uid")+" 3";
					returnObject.execute=true;
				} else {
					selectedDownload = data.getUrlDownloads().findDownloadByUid(functionParms.get("uid"));
					if (selectedDownload!=null){
						returnObject.methodCall="downloads "+functionParms.get("uid")+" 1";
						returnObject.execute=true;
					}
				}
				if (selectedPodcast==null && selectedDownload==null){
					System.out.println("Error: Invalid User Input.");
				}
			} else if (functionParms.containsKey("all")){
				// This is used to purge all download from the download queue.
				CLInput input = new CLInput();
				if (input.confirmRemoval("all downloads from the queue")){
					data.getUrlDownloads().deleteAllDownloads();
				}
				returnObject.methodCall="mainmenu showmenu";
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
