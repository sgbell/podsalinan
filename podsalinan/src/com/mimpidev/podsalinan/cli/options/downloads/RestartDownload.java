/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.downloads;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;

/**
 * @author sbell
 *
 */
public class RestartDownload extends CLIOption {

	/**
	 * @param newData
	 */
	public RestartDownload(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(String command) {
		debug = true;
		
		if (debug) Podsalinan.debugLog.logInfo(this,"command: "+command);

		if (command.split(" ").length>1){
			data.getUrlDownloads().restartDownload(command.split(" ")[0]);
		}
		return returnObject;
	}

}
