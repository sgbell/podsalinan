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
public class DeleteDownload extends CLIOption {

	/**
	 * @param newData
	 */
	public DeleteDownload(DataStorage newData) {
		super(newData);
		debug=true;
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ReturnObject execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo("["+getClass().getName()+"] command: "+command);

		if (command.split(" ").length>1){
			data.getUrlDownloads().deleteActiveDownload(command.split(" ")[0]);
		}
		
        returnObject.methodCall="downloads";
        returnObject.methodParameters="";
        returnObject.execute=true;
		return returnObject;
	}

}
