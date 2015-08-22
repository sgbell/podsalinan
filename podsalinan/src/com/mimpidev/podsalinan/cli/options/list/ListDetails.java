/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.list;

import java.util.HashMap;
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
public class ListDetails extends CLIOption {

	private Map<String,String> globalToMenu = new HashMap<String,String>();
	/**
	 * @param newData
	 */
	public ListDetails(DataStorage newData) {
		super(newData);
		
		globalToMenu.put("podcastid", "podcast");
		globalToMenu.put("downloads", "downloads");
		globalToMenu.put("episode", "episode");
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		if (debug) Podsalinan.debugLog.logMap(this,functionParms);
		
		String[] globalSelectList = {"downloads","episode","podcastid"};
		boolean detailsFound=false;
		int selectionCount=0;
		while (!detailsFound && selectionCount<globalSelectList.length){
			if (CLInterface.cliGlobals.getGlobalSelection().containsKey(globalSelectList[selectionCount])){
				returnObject.methodCall=globalToMenu.get(globalSelectList[selectionCount])+" "+CLInterface.cliGlobals.getGlobalSelection().get(globalSelectList[selectionCount]);
				detailsFound=true;
			} else 
				selectionCount++;
		}
		if (selectionCount<globalSelectList.length && globalSelectList[selectionCount].equals("episode")){
			returnObject.methodCall="podcast "+CLInterface.cliGlobals.getGlobalSelection().get("podcastid")+" "+returnObject.methodCall;
		}
		if (!detailsFound){
			System.out.println("Nothing Selected.");
		} else {
			returnObject.methodCall+=" showdetails";
		}
		returnObject.execute=detailsFound;
		
		return returnObject;
	}

}
