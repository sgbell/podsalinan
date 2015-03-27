/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.episode;

import java.net.MalformedURLException;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.data.Episode;
import com.mimpidev.podsalinan.data.Podcast;

/**
 * @author sbell
 *
 */
public class ShowEpisodeDetails extends BaseEpisodeOption {

	/**
	 * @param newData
	 */
	public ShowEpisodeDetails(DataStorage newData) {
		super(newData);
	}

	public void printDetails(Episode episode, boolean showAll){
		if (episode!=null){
			System.out.println ("Podcast: "+getPodcast().getName());
			System.out.println ("Episode: "+episode.getTitle());
			System.out.println ("Date: "+episode.getDate());
			System.out.println ("Status: "+episode.getCurrentStatus());
			if (showAll){
				System.out.println ("File size: "+episode.getSize());
				System.out.println ("URL: "+episode.getURL());
				try {
					System.out.println ("Destination: "+episode.getFilename());
				} catch (MalformedURLException e) {
					Podsalinan.debugLog.logError(this, "Faulty URL");
					Podsalinan.debugLog.logError(this, "URL: "+episode.getURL());
					Podsalinan.debugLog.printStackTrace(e.getStackTrace());
				}
			}
		}
	}
	
	@Override
	public ReturnObject execute(String command) {
		debug=true;
		if (debug) Podsalinan.debugLog.logInfo(this,"command: "+command);
		if (command.length()==0){
			//TODO Working here and ShowSelectedMenu to finish this up
		}
		
		return returnObject;
	}
}
