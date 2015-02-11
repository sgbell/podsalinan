/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.settings;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInput;
import com.mimpidev.podsalinan.cli.ObjectCall;

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

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ObjectCall execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo("["+getClass().getName()+"] command: "+command);

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
		
		String updateValue = input.getValidNumber(1,6);
		if (updateValue!=null){
			if (updateValue.length()>0){
				switch (Integer.parseInt(updateValue)){
					case 1:
						// 1 Hour
						updateValue="60";
						break;
					case 2:
						//2 Hours
						updateValue="120";
						break;
					case 3:
						// 3 Hours
						updateValue="180";
						break;
					case 4:
						// 6 Hours
						updateValue="360";
						break;
					case 5:
						// 12 Hours
						updateValue="720";
						break;
					case 6:
						// 24 Hours
						updateValue="1440";
						break;
				}
				data.getSettings().updateSetting("updateInterval",updateValue);
				System.out.println("Update Interval now set to:"+data.getSettings().findSetting("updateInterval"));
				// Wake up the main thread in Podsalinan to update the wait value
				
				synchronized (data.getSettings().getWaitObject()){
					data.getSettings().getWaitObject().notify();
				}
			}
		}		
		
		return returnObject;
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
