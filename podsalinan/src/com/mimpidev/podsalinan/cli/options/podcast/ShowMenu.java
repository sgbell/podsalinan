/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.podcast;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.cli.options.list.ListPodcasts;

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
		debug=true;
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ReturnObject execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo(command);
		System.out.println();
		
		ListPodcasts list = new ListPodcasts(data);
		list.execute("showCount");

		System.out.println();
		System.out.println("(A-Z) Enter Podcast letter to select Podcast.");
		System.out.println();
		System.out.println("9. Return to Main Menu");

		return returnObject;
	}

}
