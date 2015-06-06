/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.settings;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInput;
import com.mimpidev.podsalinan.cli.ReturnObject;

/**
 * @author sbell
 *
 */
public class PodcastUpdateRate extends CLIOption {

	private CLInput input = new CLInput();
	/**
	 * @param newData
	 */
	public PodcastUpdateRate(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(String command) {
		debug=true;
		if (debug) Podsalinan.debugLog.logInfo(this,29," command: "+command);
		String[] commandOptions = command.split(" ");
		String updateValue="";
		if (commandOptions.length==1 && !command.equalsIgnoreCase("updateinterval")){
			updateValue=executeMenuOption();
		} else if (command.equalsIgnoreCase("updateinterval")){
			System.out.println("Error: No interval specified");
		} else if (commandOptions[0].equalsIgnoreCase("updateinterval")){
			try {
				updateValue=commandOptions[1];
			} catch (NumberFormatException e) {
				System.out.println("Error: Invalid value.");
			}
		} else {
			try {
				// Check that the second value passed in is a number, and between 1 & 6
			    if ((Integer.parseInt(commandOptions[1])<1)||
			    	(Integer.parseInt(commandOptions[1])>6)){
			    	throw new NumberFormatException("Number out of Bounds");
			    }
			    updateValue=commandOptions[1];
			} catch (NumberFormatException e){
				System.out.println("Error: Invalid Value.");
				System.out.println("Valid values: (1)Hourly, (2)Every 2 Hours, (3)Every 3 Hours, (4)Every 6 Hours, (5)Every 12 Hours, (6)Daily");
			}
		}

		if (updateValue!=null){
			if (updateValue.length()>0){
				switch (Integer.parseInt(updateValue)){
					case 1:
					case 60:
						// 1 Hour
						updateValue="60";
						break;
					case 2:
					case 120:
						//2 Hours
						updateValue="120";
						break;
					case 3:
					case 180:
						// 3 Hours
						updateValue="180";
						break;
					case 4:
					case 360:
						// 6 Hours
						updateValue="360";
						break;
					case 5:
					case 720:
						// 12 Hours
						updateValue="720";
						break;
					case 6:
					case 1440:
						// 24 Hours
						updateValue="1440";
						break;
				}
				data.getSettings().updateSetting("updateInterval",updateValue);
				System.out.println("Update Interval now set to: "+printUserFriendlyUpdateRate());
				// Wake up the main thread in Podsalinan to update the wait value
				
				synchronized (data.getSettings().getWaitObject()){
					data.getSettings().getWaitObject().notify();
				}
			}
		}
		if (!commandOptions[0].equalsIgnoreCase("updateinterval")){
			returnObject.methodCall="settings";
			returnObject.methodParameters="";
			returnObject.execute=true;
		} else {
			returnObject.execute=false;
		}
		
		return returnObject;
	}

	private String executeMenuOption() {

		System.out.println ();
		System.out.println ("How often to update the podcast feeds?");
		System.out.println ("1. Hourly");
		System.out.println ("2. Every 2 Hours");
		System.out.println ("3. Every 3 Hours");
		System.out.println ("4. Every 6 Hours");
		System.out.println ("5. Every 12 Hours");
		System.out.println ("6. Daily");
		if (!data.getSettings().isValidSetting("updateInterval"))
			data.getSettings().addSetting("updateInterval", "1440");
        
        System.out.print ("Choice ["+printUserFriendlyUpdateRate()+"]: ");
		/* Take user input.
		 * Make sure it is between 1 & 6
		 * If not leave PodcastRate as it's current value.
		 */
		
		return input.getValidNumber(1,6);
	}

	private String printUserFriendlyUpdateRate(){
    	switch (Integer.parseInt(data.getSettings().findSetting("updateInterval"))){
		    case 60:
			    return "Hourly";
		    case 120:
		    	return "2 Hours";
		    case 180:
		    	return "3 Hours";
		    case 360:
		    	return "6 Hours";
		    case 720:
		    	return "12 Hours";
		    case 1440:
		    	return "Daily";
		    default:
		    	return null;
    	}

	}
}
