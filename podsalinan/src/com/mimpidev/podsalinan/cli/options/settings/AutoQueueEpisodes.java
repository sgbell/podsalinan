/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.settings;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnCall;

/**
 * @author sbell
 *
 */
public class AutoQueueEpisodes extends CLIOption {

	/**
	 * @param newData
	 */
	public AutoQueueEpisodes(DataStorage newData) {
		super(newData);
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ReturnCall execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo("["+getClass().getName()+"] command: "+command);
		
		System.out.println ();
		if (!settings.isValidSetting("autoQueue"))
			settings.addSetting("autoQueue", "false");
		System.out.print ("Do you want new episodes Automatically Queued to Download? (Y/N) ["+
				          settings.findSetting("autoQueue")+"]: ");
		String autoDownloadResponse = input.getStringInput();
		if (autoDownloadResponse.length()==1){
			switch (autoDownloadResponse.charAt(0)){
				case 'Y':
				case 'y':
					settings.updateSetting("autoQueue","true");
					settings.getWaitObject().notify();
					break;
				case 'N':
				case 'n':
					settings.updateSetting("autoQueue","false");
					break;
				default:
					System.err.println ("Error: User entered Value is invalid. No change made");
					break;
			}
		} else if (autoDownloadResponse.length()>1) {
			if (autoDownloadResponse.equalsIgnoreCase("yes"))
				settings.updateSetting("autoQueue","true");
			else if (autoDownloadResponse.equalsIgnoreCase("no"))
				settings.updateSetting("autoQueue","false");
			else if (autoDownloadResponse.equalsIgnoreCase("no"))
				System.err.println ("Error: User entered Value is invalid. No change made");
		}
		System.out.println("Auto Queue Downloads: "+settings.findSetting("autoQueue"));
		
		return returnObject;
	}

}
