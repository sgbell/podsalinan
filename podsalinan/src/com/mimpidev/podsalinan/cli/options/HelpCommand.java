/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import java.util.HashMap;
import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.options.help.*;

/**
 * @author sbell
 *
 */
public class HelpCommand extends CLIOption {

	private Map<String, CLIOption> options;
	/**
	 * @param newData
	 */
	public HelpCommand(DataStorage newData) {
		super(newData);
		options = new HashMap<String, CLIOption>();
		options.put("", new Help(newData));
		options.put("select", new HelpSelect(newData));
		options.put("list", new HelpList(newData));
		options.put("set", new HelpSet(newData));
	}

	@Override
	public void execute(String command) {
		System.out.println("");
		
		if (!options.containsKey(command.split(" ", 2)[1])){
            System.out.println("Error: Invalid Help request.");
			((CLIOption)options.get("")).execute(command);
		} else {
		    ((CLIOption)options.get(command.split(" ", 2)[1])).execute(command);
		
		System.out.println("");
		System.out.println("");
	}

}
