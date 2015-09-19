/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.downloads;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.dev.debug.Log;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInterface;
import com.mimpidev.podsalinan.cli.ReturnObject;

/**
 * @author sbell
 *
 */
public class IncreasePriority extends CLIOption {

	/**
	 * @param newData
	 */
	public IncreasePriority(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		if (debug) if (Log.isDebug())Log.logMap(this, functionParms);

		boolean increased=false;
		if (!functionParms.containsKey("uid") &&
			(CLInterface.cliGlobals.getGlobalSelection().containsKey("downloads"))){
			functionParms.put("uid", CLInterface.cliGlobals.getGlobalSelection().get("downloads"));
		}
		
		if (functionParms.containsKey("uid")){
			increased=data.getUrlDownloads().increasePriority(functionParms.get("uid"));
			if (increased){
				   System.out.println("Increased Priority: "+functionParms.get("uid"));
			} else {
				System.out.println("Error: Download already at the top of the list.");
			}
		}
		
		returnObject.methodCall="downloads "+functionParms.get("uid");
		returnObject.parameterMap.clear();
		returnObject.execute=true;
		
		return returnObject;
	}

}
