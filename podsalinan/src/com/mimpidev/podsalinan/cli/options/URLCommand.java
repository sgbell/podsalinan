/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ObjectCall;

/**
 * @author sbell
 *
 */
public class URLCommand extends CLIOption {

	/**
	 * @param newData
	 * @param returnObject 
	 */
	public URLCommand(DataStorage newData, ObjectCall returnObject) {
		super(newData,returnObject);
	}

	@Override
	public ObjectCall execute(String command) {
		//TODO: check what addDownload will do if command is not a url
		data.getUrlDownloads().addDownload(command,data.getSettings().getSettingValue("defaultDirectory"),"-1",false);
		return null;
	}

}
