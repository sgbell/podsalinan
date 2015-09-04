/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.settings;

import java.util.Map;

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
	public ReturnObject execute(Map<String, String> functionParms) {
		if (debug) Podsalinan.debugLog.logMap(this, functionParms);
		String updateValue="";
		
		if (!functionParms.containsKey("updateInterval") &&
			functionParms.containsKey("userInput")){
			functionParms.put("updateInterval", functionParms.get("userInput"));
		}

		if (debug) Podsalinan.debugLog.logMap(this, functionParms);
		
		if (!functionParms.containsKey("updateInterval")){
			updateValue=executeMenuOption();
		} else {
			try {
				// Check that the value passed in is a number, and between 1 & 6
				int updateInterval = Integer.parseInt(functionParms.get("updateInterval"));
				if (debug) Podsalinan.debugLog.logInfo(this, 46, "Update interval is an int: "+updateInterval);
				if ((updateInterval!=60)&&
			    	(updateInterval!=120)&&
			    	(updateInterval!=180)&&
			    	(updateInterval!=360)&&
			    	(updateInterval!=720)&&
			    	(updateInterval!=1440)){
					if ((updateInterval<1)||
						(updateInterval>6)){
						throw new NumberFormatException("Number out of Bounds");
					}
			    }
			    updateValue=functionParms.get("updateInterval");
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
		if (!functionParms.containsKey("updateinterval")){
			returnObject.methodCall="settings";
			returnObject.execute=true;
		} else {
			returnObject.methodCall="";
			
			returnObject.execute=false;
		}

		returnObject.parameterMap.clear();
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
