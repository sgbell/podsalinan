/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

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
public class RestartCommand extends CLIOption {

	/**
	 * @param newData
	 */
	public RestartCommand(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		String command="";
		debug=true;
		if (debug) Podsalinan.debugLog.logInfo(this,"command: "+command);
		
		String[] commandSplit = command.split(" ");
		if (debug) Podsalinan.debugLog.logInfo(this, "size: "+commandSplit.length);
		String downloadUid="";
		//Check length of command
		if (command.length()==0 && CLInterface.cliGlobals.getGlobalSelection().containsKey("downloads")){
			downloadUid=CLInterface.cliGlobals.getGlobalSelection().get("downloads");
		} else {
			if (data.getUrlDownloads().findDownloadByUid(commandSplit[0])!=null){
				downloadUid=commandSplit[0];
			}
		}
		if (downloadUid!=""){
			data.getUrlDownloads().restartDownload(downloadUid);
		}
		
		return returnObject;
	}

}
