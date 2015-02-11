/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.settings;

import java.io.File;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInput;
import com.mimpidev.podsalinan.cli.ObjectCall;

/**
 * @author sbell
 *
 */
public class DownloadDirectory extends CLIOption {

	private CLInput input = new CLInput();
	/**
	 * @param newData
	 */
	public DownloadDirectory(DataStorage newData) {
		super(newData);
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ObjectCall execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo("["+getClass().getName()+"] command: "+command);
		
		File newPath;
		System.out.println ();
		System.out.print ("Enter Default Directory["+data.getSettings().findSetting("defaultDirectory")+"]: ");
		/* Take user input.
		 * Create File object to test if directory exists.
		 * If directory exists set defaultDirectory to file Input
		 * If not show and error and leave defaultDirectory as is
		 */
		String userInput=input.getStringInput();
		if ((userInput.length()>0)&&(userInput!=null)){
			newPath=new File(userInput);
			if ((newPath.exists())&&(newPath.isDirectory())){
				data.getSettings().updateSetting("defaultDirectory",userInput);
			} else {
				System.out.println ("Error: User Input invalid");
			}
		}
		System.out.println("Default Directory: "+data.getSettings().findSetting("defaultDirectory"));
		
		return returnObject;
	}

}
