/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnCall;

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

	public ReturnCall execute(String command){
		data.getSettings().setFinished(true);
		return null;
	}
}
