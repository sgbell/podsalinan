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
public class StopDownload extends CLIOption {

	/**
	 * @param newData
	 */
	public StopDownload(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {

		if (debug) if (Log.isDebug())Log.logMap(this, functionParms);

		if (functionParms.size()==0 &&
			(CLInterface.cliGlobals.getGlobalSelection().containsKey("downloads"))){
			functionParms.put("uid", CLInterface.cliGlobals.getGlobalSelection().get("downloads"));
		}
		if (functionParms.containsKey("uid")){
			data.getUrlDownloads().cancelDownload(functionParms.get("uid"));
		}

		returnObject.methodCall="downloads "+functionParms.get("uid");
		returnObject.parameterMap.clear();
		returnObject.execute=true;
		
		return returnObject;
	}

}
