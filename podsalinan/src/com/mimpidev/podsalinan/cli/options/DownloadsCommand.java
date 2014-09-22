/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import java.util.HashMap;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnCall;
import com.mimpidev.podsalinan.cli.options.downloads.SelectDownload;
import com.mimpidev.podsalinan.cli.options.downloads.ShowMenu;

/**
 * @author sbell
 *
 */
public class DownloadsCommand extends CLIOption {

	/**
	 * @param newData
	 */
	public DownloadsCommand(DataStorage newData) {
		super(newData);
		ShowMenu showMenu = new ShowMenu(newData);
		options = new HashMap<String, CLIOption>();
		options.put("<aa>", new SelectDownload(newData));
		options.put("showMenu", showMenu);
		options.put("", showMenu);
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ReturnCall execute(String command) {
		returnObject = new ReturnCall();
		returnObject.methodCall="podcast";
		
		
		
		return returnObject;
	}

}
