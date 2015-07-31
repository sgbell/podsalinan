/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.podcast;

import java.util.HashMap;
import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInput;
import com.mimpidev.podsalinan.cli.CLInterface;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.data.Episode;
import com.mimpidev.podsalinan.data.Podcast;

/**
 * @author sbell
 *
 */
public class ListEpisodes extends CLIOption {

	/**
	 * @param newData
	 */
	public ListEpisodes(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(final Map<String, String> functionParms) {
		debug=true;
		
		CLInput input = new CLInput();
		
		int epCount=1;
		returnObject = new ReturnObject();
		returnObject.methodCall="podcast";
		
        if (functionParms.containsKey("podcastId")){
    		Podcast selectedPodcast = data.getPodcasts().getPodcastByUid(functionParms.get("podcastId"));
    		if (selectedPodcast==null && CLInterface.cliGlobals.getGlobalSelection().containsKey("podcastId")){
    			selectedPodcast = data.getPodcasts().getPodcastByUid(CLInterface.cliGlobals.getGlobalSelection().get("podcastId"));
    		}
    		if (selectedPodcast!=null){
    			System.out.println ();
    			synchronized (selectedPodcast.getEpisodes()){
    				for (Episode episode : selectedPodcast.getEpisodes()){
    					System.out.println (getEncodingFromNumber(epCount)+" - " +
    							episode.getTitle()+" : "+episode.getDate());
    					epCount++;
    					if ((epCount%20)==0){
    						System.out.println("-- Press any key to continue, q to quit --");
    						char charInput=input.getSingleCharInput();
    						if (charInput=='q')
    							break;
    					}
    				}
    			}
    			returnObject.methodCall += " <podcastid>";
    			returnObject.parameterMap=new HashMap<String,String>(){/**
					 * 
					 */
					private static final long serialVersionUID = -4227936048587147659L;
				    {put("podcastId",functionParms.get("podcastId"));}};
    		}
   		} else {
				System.out.println("Error: No podcast has been selected");
				returnObject.methodCall += " showmenu";
				returnObject.parameterMap=new HashMap<String,String>();
		}
		returnObject.execute=true;
		
		return returnObject;
	}

}
