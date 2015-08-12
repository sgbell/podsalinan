/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.downloads;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInput;
import com.mimpidev.podsalinan.cli.CLInterface;
import com.mimpidev.podsalinan.cli.ReturnObject;

/**
 * @author sbell
 *
 */
public class DeleteDownload extends CLIOption {

	/**
	 * @param newData
	 */
	public DeleteDownload(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		if (debug) Podsalinan.debugLog.logMap(this,functionParms);

		if (functionParms.containsKey("uid")){
			ShowDownloadDetails printDetails = new ShowDownloadDetails(data);
			printDetails.execute(functionParms);

			CLInput input = new CLInput();
			if(input.confirmRemoval()){
				data.getUrlDownloads().deleteActiveDownload(functionParms.get("uid"));
				System.out.println("Download Removed.");
				if ((CLInterface.cliGlobals.getGlobalSelection().containsKey("downloads"))&&
					(CLInterface.cliGlobals.getGlobalSelection().get("downloads").equalsIgnoreCase(functionParms.get("uid")))){
					CLInterface.cliGlobals.getGlobalSelection().clear();
				}
				
			}
			returnObject.methodCall = "downloads showmenu";
		}
		
		returnObject.parameterMap.clear();
		returnObject.execute=true;
		
		return returnObject;
	}

}
