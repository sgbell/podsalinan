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
public class SelectDownload extends CLIOption {

	/**
	 * @param newData
	 */
	public SelectDownload(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		if (debug) Podsalinan.debugLog.logMap(this, functionParms);

		if (functionParms.containsKey("userInput")){
			String userInput=functionParms.get("userInput");
			String selectedDownload=null;
			
			if (data.getUrlDownloads().getNumberOfQueuedDownloads()>convertCharToNumber(userInput)){
				selectedDownload = data.getUrlDownloads().getDownloadUid(convertCharToNumber(userInput));
			}
			if (selectedDownload==null){
				System.out.println("Error: Invalid download requested.");
				returnObject.methodCall="downloads showmenu";
			} else {
				if (!(CLInterface.cliGlobals.getGlobalSelection().containsKey("downloads")&&
					selectedDownload.equals(CLInterface.cliGlobals.getGlobalSelection().get("downloads")))){
					CLInterface.cliGlobals.getGlobalSelection().clear();
					CLInterface.cliGlobals.getGlobalSelection().put("downloads", selectedDownload);
				}
				returnObject.methodCall="downloads "+selectedDownload;
			}
		}
		returnObject.parameterMap.clear();
		returnObject.execute=true;

		return returnObject;
	}

}
