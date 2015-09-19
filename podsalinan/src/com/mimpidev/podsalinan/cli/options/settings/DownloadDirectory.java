/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.settings;

import java.io.File;
import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.dev.debug.Log;
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
	
	public ReturnObject execute(Map<String, String> functionParms) {
		if (debug) if (Log.isDebug())Log.logMap(this, functionParms);
		String userInput="";

		if (functionParms.containsKey("path") &&
			!functionParms.containsKey("passedDirectory")){
			functionParms.put("passedDirectory", functionParms.get("path"));
		}
		
        if (functionParms.containsKey("passedDirectory")){
        	userInput = functionParms.get("passedDirectory");
        } else {
        	userInput=executeMenuOption();
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
        if (functionParms.containsKey("passedDirectory")){
        	returnObject.methodCall="";
        } else {
        	returnObject.methodCall="settings";
        }
		returnObject.parameterMap.clear();
		returnObject.execute=true;
		
		return returnObject;
	}

}
