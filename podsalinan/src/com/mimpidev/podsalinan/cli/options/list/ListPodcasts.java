/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.list;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.data.Podcast;

/**
 * @author sbell
 *
 */
public class ListPodcasts extends CLIOption {

	/**
	 * @param newData
	 */
	public ListPodcasts(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		int podcastCount=1;
		
		synchronized(data.getPodcasts().getList()){
			for (Podcast podcast : data.getPodcasts().getList()){
				if (!podcast.isRemoved()){
					if (functionParms.containsKey("showcount")){
						
						System.out.print(getEncodingFromNumber(podcastCount));
					} else {
						System.out.print(podcast.getDatafile());
					}
					System.out.println(". "+podcast.getName());
				
				    podcastCount++;
				}
			}
		}
		
		returnObject.execute=false;
		return returnObject;
	}

}
