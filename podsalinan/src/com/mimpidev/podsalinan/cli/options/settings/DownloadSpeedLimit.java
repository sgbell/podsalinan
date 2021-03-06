/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.settings;

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
public class DownloadSpeedLimit extends CLIOption {

	private CLInput input = new CLInput();
	/**
	 * @param newData
	 */
	public DownloadSpeedLimit(DataStorage newData) {
		super(newData);
	}

    public String executeMenuOption(){
		System.out.println();
		System.out.println("Valid values: 0 (Means no limit); 25 (Means 25Kbps); 1M (Means 1 Mbps)");
		System.out.println("The value you set here is the total limit, which is shared evenly across downloaders");
		if (data.getSettings().findSetting("downloadLimit")==null)
			data.getSettings().addSetting("downloadLimit", "0");
		System.out.print ("Please enter the Download Speed Limit["+data.getSettings().findSetting("downloadLimit")+"]:");
		return input.getStringInput();
	}
	
	public ReturnObject execute(Map<String, String> functionParms) {
		if (debug) if (Log.isDebug())Log.logMap(this, functionParms);
		String userInput="";

        if (functionParms.containsKey("userInput")){
        	userInput=functionParms.get("userInput");
        	returnObject.methodCall="";
        	returnObject.execute=false;
        } else {
        	userInput=executeMenuOption();
        	returnObject.methodCall="settings showmenu";
        	returnObject.execute=true;
        }

        int speed=-1;
		if (userInput.length()>0){
			try {
				speed = Integer.parseInt(userInput);
			} catch (NumberFormatException e){
				speed= -1;
			}
			if (speed<0){
				if ((userInput.toUpperCase().endsWith("M"))||
					(userInput.toUpperCase().endsWith("MBPS"))||
					(userInput.toUpperCase().endsWith("MB"))){
					String speedString=userInput.split("M")[0];
					try{
						speed = (Integer.parseInt(speedString)*1024);
					} catch (NumberFormatException e){
						speed = -1;
					}
				}
			}
			if (speed<0){
				System.out.println("Invalid Speed. Download Speed Limit unchanged");
			} else {
				data.getSettings().updateSetting("downloadLimit",Integer.toString(speed));
			}
		}
		System.out.println("Max Download Speed: "+speedToString());
		
		return returnObject;
	}

	public String speedToString (){
		int speed=-1;
		try {
			speed = Integer.parseInt(data.getSettings().findSetting("downloadLimit"));
		} catch (NumberFormatException e){
			speed = -1;
		}
		if (speed>1024){
			return (speed/1024)+"Mb/s";
		} else {
		    return speed+"Kb/s";
		}
	}
}
