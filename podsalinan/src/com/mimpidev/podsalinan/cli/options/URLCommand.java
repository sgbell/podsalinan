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
public class URLCommand extends CLIOption {

	/**
	 * @param newData
	 */
	public URLCommand(DataStorage newData) {
		super(newData);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ReturnCall execute(String command) {
		data.getUrlDownloads().addDownload(command,data.getSettings().getSettingValue("defaultDirectory"),"-1",false);
		return null;
	}

}
