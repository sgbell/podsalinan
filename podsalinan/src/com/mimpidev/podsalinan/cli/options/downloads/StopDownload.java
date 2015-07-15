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
public class StopDownload extends CLIOption {

	/**
	 * @param newData
	 */
	public StopDownload(DataStorage newData) {
		super(newData);
		debug=true;
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		String command="";
		if (debug) Podsalinan.debugLog.logInfo(this, "command: "+command);

		if (command.split(" ").length>1){
			data.getUrlDownloads().cancelDownload(command.split(" ")[0]);
		}
		returnObject = CLInterface.cliGlobals.createReturnObject();
		returnObject.execute=true;
		
		return returnObject;
	}

}
