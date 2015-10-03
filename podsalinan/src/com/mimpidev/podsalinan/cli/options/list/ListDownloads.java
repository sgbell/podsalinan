/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.list;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.data.URLDownload;

/**
 * @author sbell
 *
 */
public class ListDownloads extends CLIOption {

	/**
	 * @param newData
	 */
	public ListDownloads(DataStorage newData) {
		super(newData);
	}


	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		int downloadCount=1;
		
		synchronized(data.getUrlDownloads().getDownloads()){
			for (URLDownload download : data.getUrlDownloads().getDownloads()){
				if (!download.isRemoved()){
					System.out.println(getEncodingFromNumber(downloadCount)+". ("+download.getCharStatus()+") "+download.getURL().toString());
					downloadCount++;
				}
			}
		}
		
		return returnObject;
	}
}
