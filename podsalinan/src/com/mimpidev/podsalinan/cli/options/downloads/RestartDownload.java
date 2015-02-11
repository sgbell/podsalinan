/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.downloads;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ObjectCall;

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
		debug=true;
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ObjectCall execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo("["+getClass().getName()+"] command: "+command);

		if (command.split(" ").length>1){
			data.getUrlDownloads().restartDownload(command.split(" ")[0]);
		}
		return returnObject;
	}

}
