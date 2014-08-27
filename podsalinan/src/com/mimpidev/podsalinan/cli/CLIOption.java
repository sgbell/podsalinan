/**
 * 
 */
package com.mimpidev.podsalinan.cli;

import com.mimpidev.podsalinan.DataStorage;

/**
 * @author sbell
 *
 */
public abstract class CLIOption {

	protected DataStorage data;
	
	/**
	 * 
	 */
	public CLIOption(DataStorage newData) {
		data=newData;
	}

	public abstract void execute(String command);
}
