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
		if (functionParms.containsKey("url")){
			try {
				// newURL is only used to confirm that the user input is a url
				URL newURL = new URL(functionParms.get("url"));
				data.getUrlDownloads().addDownload(functionParms.get("url"), data.getSettings().getSettingValue("defaultDirectory"),"-1",true);
				System.out.println("Downloading URL: "+functionParms.get("url"));
			} catch (MalformedURLException e) {
				System.out.println("Error: Invalid Input");
			}
		} else {
			System.out.println("Error: Invalid Input");
		}
		returnObject.methodCall="";
		returnObject.execute=false;

		return returnObject;
	}

}
