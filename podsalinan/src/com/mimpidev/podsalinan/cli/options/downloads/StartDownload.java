/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.downloads;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.dev.debug.Log;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;

/**
 * @author sbell
 *
 */
public class StartDownload extends CLIOption {

	/**
	 * @param newData
	 */
	public StartDownload(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {

		if (debug) if (Log.isDebug())Log.logMap(this, functionParms);

		if (functionParms.containsKey("uid")){
			data.getUrlDownloads().reQueueDownload(functionParms.get("uid"));
		}

		returnObject.methodCall="downloads "+functionParms.get("uid");
		returnObject.parameterMap.clear();
		returnObject.execute=true;
		
		return returnObject;
	}

}
