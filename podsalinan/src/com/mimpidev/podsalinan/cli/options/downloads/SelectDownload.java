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
import com.mimpidev.podsalinan.data.URLDownload;

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
		String selectedDownload=null;

		if (functionParms.containsKey("userInput")){
			String userInput=functionParms.get("userInput");
			
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
		} else if (functionParms.containsKey("uid")){
			URLDownload download = data.getUrlDownloads().findDownloadByUid(functionParms.get("uid"));
			if (download!=null){
				selectedDownload=download.getUid();
				if (!(CLInterface.cliGlobals.getGlobalSelection().containsKey("downloads")&&
						selectedDownload.equals(CLInterface.cliGlobals.getGlobalSelection().get("downloads")))){
						CLInterface.cliGlobals.getGlobalSelection().clear();
						CLInterface.cliGlobals.getGlobalSelection().put("downloads", selectedDownload);
					}
					returnObject.methodCall="downloads "+selectedDownload;
			} else {
				System.out.println("Error: Invalid download requested.");
				returnObject.methodCall="downloads showmenu";
			}
		}
		returnObject.parameterMap.clear();
		returnObject.execute=true;

		return returnObject;
	}

}
