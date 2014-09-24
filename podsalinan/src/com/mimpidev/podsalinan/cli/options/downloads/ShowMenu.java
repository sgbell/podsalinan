/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.downloads;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnCall;
import com.mimpidev.podsalinan.data.URLDownload;

/**
 * @author sbell
 *
 */
public class ShowMenu extends CLIOption {

	/**
	 * @param newData
	 */
	public ShowMenu(DataStorage newData) {
		super(newData);
	}

	public void listDownloads(){
		int downloadCount=1;
		
		for (URLDownload download : data.getUrlDownloads().getDownloads()){
			if (!download.isRemoved()){
				System.out.println(getEncodingFromNumber(downloadCount)+". ("+download.getCharStatus()+") "+download.getURL().toString());
				downloadCount++;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ReturnCall execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo("["+getClass().getName()+"] command: "+command);
		
		listDownloads();
		System.out.println();
		System.out.println("(A-ZZ) Enter Download letter to select Download.");
		System.out.println("To add a new download to the queue just enter the the url to be downloaded.");
		System.out.println();
		System.out.println("9. Return to Main Menu");

		return returnObject;
	}

}
