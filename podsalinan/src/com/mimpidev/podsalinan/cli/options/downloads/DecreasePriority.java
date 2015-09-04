/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.downloads;

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
public class DecreasePriority extends CLIOption {

	/**
	 * @param newData
	 */
	public DecreasePriority(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		if (debug) Podsalinan.debugLog.logMap(this,functionParms);

		boolean decreased=false;
		if (!functionParms.containsKey("uid") &&
			(CLInterface.cliGlobals.getGlobalSelection().containsKey("downloads"))){
			functionParms.put("uid", CLInterface.cliGlobals.getGlobalSelection().get("downloads"));
		}
		
		if (functionParms.containsKey("uid")){
			decreased=data.getUrlDownloads().decreasePriority(functionParms.get("uid"));
			if (decreased){
				   System.out.println("Decreased Priority: "+functionParms.get("uid"));
			} else {
				System.out.println("Error: Download already at the bottom of the list.");
			}
		}
		
		returnObject.methodCall="downloads "+functionParms.get("uid");
		returnObject.parameterMap.clear();
		returnObject.execute=true;
		
		return returnObject;
	}

}
