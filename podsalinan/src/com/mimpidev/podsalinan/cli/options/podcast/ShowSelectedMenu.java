/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.podcast;

import java.util.HashMap;
import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInterface;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.data.Podcast;

/**
 * @author sbell
 *
 */
public class ShowSelectedMenu extends CLIOption {

	public ShowSelectedMenu(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		final String podcastId = functionParms.get("podcastId");
		Podcast currentPodcast = data.getPodcasts().getPodcastByUid(podcastId);
			if (currentPodcast!=null){
				System.out.println();
				ShowPodcastDetails podcastDetail=new ShowPodcastDetails(data);
				podcastDetail.execute(new HashMap<String,String>(){/**
					 * 
					 */
					private static final long serialVersionUID = -393124687274598350L;
				    {put("podcastId",podcastId);
				     put("command","selectedMenu");}});
				System.out.println("1. List Episodes");
				System.out.println("2. Update List");
				System.out.println("3. Delete Podcast");
				System.out.println("4. Change Download Directory");
				System.out.println("5. Autoqueue Episodes");
				System.out.println("<AA>. Select Episode");
				System.out.println();
				System.out.println("9. Return to List of Podcasts");
				System.out.println();
				
				returnObject.methodCall = "podcast <aaaaaaaa>";
				returnObject.parameterMap = new HashMap<String,String>(){/**
					 * 
					 */
					private static final long serialVersionUID = -2874925380286596826L;
                    {put("podcastId",podcastId);}};
				
			}
		returnObject.execute=false;
		return returnObject;
	}

}
