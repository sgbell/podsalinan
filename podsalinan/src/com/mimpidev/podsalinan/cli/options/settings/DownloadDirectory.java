/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.settings;

import java.io.File;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInput;
import com.mimpidev.podsalinan.cli.ReturnObject;

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

	public String executeMenuOption(){
		System.out.println ();
		System.out.print ("Enter Default Directory["+data.getSettings().findSetting("defaultDirectory")+"]: ");
		/* Take user input.
		 * Create File object to test if directory exists.
		 * If directory exists set defaultDirectory to file Input
		 * If not show and error and leave defaultDirectory as is
		 */
		return input.getStringInput();
	}
	
	public ReturnObject execute(String command) {
		//TODO: 1.2.5 Check defaultdirectory command
		
		if (debug) Podsalinan.debugLog.logInfo(this, 42, "command: "+command);
		String userInput="";
		
		String[] commandOptions = command.split(" ");
        if (commandOptions.length==1){
        	userInput=executeMenuOption();
        	returnObject.methodCall="settings";
        	returnObject.execute=true;
        } else {
        	userInput=commandOptions[1];
        }
        
		File newPath;
		if ((userInput.length()>0)&&(userInput!=null)){
			newPath=new File(userInput);
			if ((newPath.exists())&&(newPath.isDirectory())){
				data.getSettings().updateSetting("defaultDirectory",userInput);
			} else {
				System.out.println ("Error: User Input invalid");
			}
		}
		System.out.println("Default Directory: "+data.getSettings().findSetting("defaultDirectory"));
		returnObject.methodCall="settings";
		returnObject.methodParameters="";
		returnObject.execute=true;
		
		return returnObject;
	}

}
