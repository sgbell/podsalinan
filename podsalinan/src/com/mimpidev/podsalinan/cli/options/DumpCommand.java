/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
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
	public ReturnObject execute(String command) {
		String menuInput = command.replaceFirst(command.split(" ")[0]+" ", "");
		
		if ((menuInput.equalsIgnoreCase("dump"))||
			(menuInput.equalsIgnoreCase("urldownloads"))){
			
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			Date date = new Date();
				
			Podsalinan.debugLog.logInfo("--URLDownload Contents - "+dateFormat.format(date)+" --");
			Podsalinan.debugLog.logInfo("DownladURL,status,destination,podcastid");
			for (URLDownload currentDownload : data.getUrlDownloads().getDownloads()){
				Podsalinan.debugLog.logInfo(currentDownload.getURL().toString()+
						","+currentDownload.getCurrentStatus()+
						","+currentDownload.getDestination()+
						","+currentDownload.getPodcastSource());
			}
			Podsalinan.debugLog.logInfo("-- URLDownload Contents end --");
		}
		return returnObject;
	}

}
