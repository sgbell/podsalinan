/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObjcet;
import com.mimpidev.podsalinan.cli.options.downloads.SelectDownload;
import com.mimpidev.podsalinan.cli.options.downloads.ShowMenu;

/**
 * @author sbell
 *
 */
public class DownloadsCommand extends CLIOption {

	/**
	 * @param newData
	 * @param returnObject 
	 */
	public DownloadsCommand(DataStorage newData, ReturnObjcet returnObject) {
		super(newData,returnObject);
		ShowMenu showMenu = new ShowMenu(newData);
		options.put("<aa>", new SelectDownload(newData));
		options.put("showmenu", showMenu);
		options.put("", showMenu);
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ReturnObjcet execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo("["+getClass().getName()+"] command: "+command);
		returnObject.methodCall="downloads";
		
		if (options.containsKey(command))
			options.get(command).execute(command);
		else {
			try {
				Integer.parseInt(command);
				if (command.equals("9")){
					returnObject.methodCall="";
					returnObject.methodParameters="";
				}
			} catch (NumberFormatException e) {
				returnObject = options.get("<aa>").execute(command);
			}
		}
		
		return returnObject;
	}

}
