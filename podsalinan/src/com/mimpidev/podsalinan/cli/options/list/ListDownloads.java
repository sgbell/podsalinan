/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.list;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObjcet;
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

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ReturnObjcet execute(String command) {

		int downloadCount=1;
			
		for (URLDownload download : data.getUrlDownloads().getDownloads()){
			if (!download.isRemoved()){
				System.out.println(getEncodingFromNumber(downloadCount)+". ("+download.getCharStatus()+") "+download.getURL().toString());
				downloadCount++;
			}
		}
		
		return returnObject;
	}
}
