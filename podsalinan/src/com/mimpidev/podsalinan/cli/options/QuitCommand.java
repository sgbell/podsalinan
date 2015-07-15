/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;

/**
 * @author sbell
 *
 */
public class QuitCommand extends CLIOption {

	/**
	 * @param newData
	 */
	public QuitCommand(DataStorage newData) {
		super(newData);
	}

	public ReturnObject execute(Map<String, String> functionParms) {
		data.getSettings().setFinished(true);
		return returnObject;
	}
}
