/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObjcet;

/**
 * @author sbell
 *
 */
public class QuitCommand extends CLIOption {

	/**
	 * @param newData
	 * @param returnObject 
	 */
	public QuitCommand(DataStorage newData, ReturnObjcet returnObject) {
		super(newData,returnObject);
	}

	public ReturnObjcet execute(String command){
		data.getSettings().setFinished(true);
		return returnObject;
	}
}
