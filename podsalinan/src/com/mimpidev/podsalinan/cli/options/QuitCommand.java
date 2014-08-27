/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;

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

	public void execute(String command){
		data.getSettings().setFinished(true);
	}
}
