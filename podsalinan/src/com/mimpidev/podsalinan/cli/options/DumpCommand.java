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
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public void execute(String command) {
		menuInput = menuInput.replaceFirst(menuInput.split(" ")[0]+" ", "");
		
		if ((menuInput.equalsIgnoreCase("dump"))||
			(menuInput.equalsIgnoreCase("urldownloads"))){
			
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			Date date = new Date();
				
			Podsalinan.debugLog.println("--URLDownload Contents - "+dateFormat.format(date)+" --");
			Podsalinan.debugLog.println("DownladURL,status,destination,podcastid");
			for (URLDownload currentDownload : data.getUrlDownloads().getDownloads()){
				Podsalinan.debugLog.println(currentDownload.getURL().toString()+
						","+currentDownload.getCurrentStatus()+
						","+currentDownload.getDestination()+
						","+currentDownload.getPodcastId());
			}
			Podsalinan.debugLog.println("-- URLDownload Contents end --");
		}
	}

}
