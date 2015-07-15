/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;

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
	}

	@SuppressWarnings("unused")
	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		String command="";
		try {
			// newURL is only used to confirm that the user input is a url
			URL newURL = new URL(command);
			data.getUrlDownloads().addDownload(command, data.getSettings().getSettingValue("defaultDirectory"),"-1",false);
			System.out.println("Downloading URL: "+command);
		} catch (MalformedURLException e) {
			// menuInput is not a url
			System.out.println("Error: Invalid Input");
		}

		return returnObject;
	}

}
