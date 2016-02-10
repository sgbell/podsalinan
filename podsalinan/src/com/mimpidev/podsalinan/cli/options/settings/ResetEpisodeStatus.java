/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.settings;

import java.util.Map;

import com.mimpidev.dev.debug.Log;
import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInput;
import com.mimpidev.podsalinan.cli.ReturnObject;

/**
 * @author sam
 *
 */
public class ResetEpisodeStatus extends CLIOption {

	private CLInput input = new CLInput();
	/**
	 * @param newData
	 */
	public ResetEpisodeStatus(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		if (Log.isDebug())Log.logMap(this, functionParms);
		String userInput="";
		
		if (functionParms.containsKey("userInput")){
			userInput=functionParms.get("userInput");
			returnObject.methodCall="";
			returnObject.execute=false;
		} else {
			returnObject.methodCall="settings showmenu";
			returnObject.execute=true;
			System.out.println("Update Status of Episodes marked as downloaded");
			System.out.println();
			System.out.print("Do you want episodes marked \"Downloaded\" updated from file system search? (Y/N) ["+data.getSettings().findSetting("updateDownloadedEpisodeStatus")+"]: ");
			userInput = input.getStringInput();
		}

		if (userInput.length()==1){
			switch (userInput.toLowerCase().charAt(0)){
				case 'y':
				case '1':
					if (!data.getSettings().updateSetting("updateDownloadedEpisodeStatus", "true"))
						data.getSettings().addSetting("updateDownloadedEpisodeStatus","true");
					try {
						data.getSettings().getWaitObject().notify();
					} catch (IllegalMonitorStateException e){
						// Just catching an error, incase nothing is waiting to do an update
					}
					break;
				case 'n':
				case '0':
					if (!data.getSettings().updateSetting("updateDownloadedEpisodeStatus","false"))
						data.getSettings().addSetting("updateDownloadedEpisodeStatus","false");
					break;
				default:
					System.out.println ("Error: User entered Value is invalid. No change made");
					break;
			}
		} else if (userInput.length()>1) {
			if (userInput.equalsIgnoreCase("yes")||
				userInput.equalsIgnoreCase("true"))
				data.getSettings().updateSetting("updateDownloadedEpisodeStatus", "true");
			else if (userInput.equalsIgnoreCase("no")||
					 userInput.equalsIgnoreCase("false"))
				data.getSettings().updateSetting("updateDownloadedEpisodeStatus","false");
			else if (!userInput.equalsIgnoreCase("no"))
				System.out.println ("Error: User entered Value is invalid. No change made");
		}
		
		return returnObject;
	}

}
