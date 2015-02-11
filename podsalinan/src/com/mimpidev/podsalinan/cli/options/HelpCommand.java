/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import java.util.HashMap;
import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ObjectCall;
import com.mimpidev.podsalinan.cli.options.help.*;

/**
 * @author sbell
 *
 */
public class HelpCommand extends CLIOption {

	/**
	 * @param newData
	 * @param returnObject 
	 */
	public HelpCommand(DataStorage newData, ObjectCall returnObject) {
		super(newData,returnObject);

		options.put("", new Help(newData));
		options.put("select", new HelpSelect(newData));
		options.put("list", new HelpList(newData));
		options.put("set", new HelpSet(newData));
	}

	@Override
	public ObjectCall execute(String command) {
		debug=true;

		if (debug) Podsalinan.debugLog.logInfo("["+getClass().getName()+"] command: "+command);
		
		System.out.println("");
		String subOption;
        if (command.split(" ").length>1)
		   subOption=command.split(" ", 2)[1];
        else
        	subOption="";
        
		
		if (!options.containsKey(subOption.toLowerCase())){
            System.out.println("Error: Invalid Help request.");
    		System.out.println("");
			options.get("").execute(command);
		} else {
		    options.get(subOption.toLowerCase()).execute(command);
		}
		
		System.out.println("");
		System.out.println("");
		return returnObject;
	}
}
