/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import com.mimpidev.dev.debug.Log;
import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.data.URLDownload;

/**
 * @author sbell
 *
 */
public class DumpCommand extends CLIOption {

	/**
	 * @param newData
	 */
	public DumpCommand(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Date date = new Date();
				
		if (Log.isDebug())Log.logInfo("--URLDownload Contents - "+dateFormat.format(date)+" --");
		if (Log.isDebug())Log.logInfo("DownladURL,status,destination,podcastid");
		ArrayList<URLDownload> downloadList = new ArrayList<URLDownload>(); 
		synchronized(data.getUrlDownloads().getDownloads()){
			downloadList = data.getUrlDownloads().clone();
		}
		for (URLDownload currentDownload : downloadList){
			if (Log.isDebug())Log.logInfo(currentDownload.getURL().toString()+
					","+currentDownload.getCurrentStatus()+
					","+currentDownload.getDestination()+
					","+currentDownload.getPodcastSource());
		}
		if (Log.isDebug())Log.logInfo("-- URLDownload Contents end --");
		return returnObject;
	}

}
