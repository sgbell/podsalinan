/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.settings;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInput;
import com.mimpidev.podsalinan.cli.ReturnObjcet;

/**
 * @author sbell
 *
 */
public class AutoQueueEpisodes extends CLIOption {

	
	private CLInput input = new CLInput();
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
	public ReturnObjcet execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo("["+getClass().getName()+"] command: "+command);
		
		System.out.println ();
		if (!data.getSettings().isValidSetting("autoQueue"))
			data.getSettings().addSetting("autoQueue", "false");
		System.out.print ("Do you want new episodes Automatically Queued to Download? (Y/N) ["+
				          data.getSettings().findSetting("autoQueue")+"]: ");
		String autoDownloadResponse = input.getStringInput();
		if (autoDownloadResponse.length()==1){
			switch (autoDownloadResponse.charAt(0)){
				case 'Y':
				case 'y':
					if (!data.getSettings().updateSetting("autoQueue","true"))
						data.getSettings().addSetting("autoQueue","true");
					try {
						data.getSettings().getWaitObject().notify();
					} catch (IllegalMonitorStateException e){
						// Just catching an error, incase nothing is waiting to do an update
					}
					break;
				case 'N':
				case 'n':
					if (!data.getSettings().updateSetting("autoQueue","false"))
						data.getSettings().addSetting("autoQueue","false");
					break;
				default:
					System.out.println ("Error: User entered Value is invalid. No change made");
					break;
			}
		} else if (autoDownloadResponse.length()>1) {
			if (autoDownloadResponse.equalsIgnoreCase("yes"))
				data.getSettings().updateSetting("autoQueue","true");
			else if (autoDownloadResponse.equalsIgnoreCase("no"))
				data.getSettings().updateSetting("autoQueue","false");
			else if (autoDownloadResponse.equalsIgnoreCase("no"))
				System.out.println ("Error: User entered Value is invalid. No change made");
		}
		System.out.println("Auto Queue Downloads: "+data.getSettings().findSetting("autoQueue"));
		
		return returnObject;
	}

}
