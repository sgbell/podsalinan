/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.podcast;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnCall;

/**
 * @author sbell
 *
 */
public class SelectPodcast extends CLIOption {

	public SelectPodcast(DataStorage newData) {
		super(newData);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ReturnCall execute(String command) {
		returnObject = new ReturnCall();
		returnObject.methodCall = "podcast";
		returnObject.methodParameters = command.replaceFirst("showMenu ", "");
		
		System.out.println();
		System.out.println("Podcast: "+data.getPodcasts().getList().get(Integer.parseInt(command)).getName()+ " - Selected");
		System.out.println("1. List Episodes");
		System.out.println("2. Update List");
		System.out.println("3. Delete Podcast");
		System.out.println("4. Change Download Directory");
		System.out.println("5. Autoqueue Episodes");
		System.out.println("<AA>. Select Episode");
		System.out.println();
		System.out.println("9. Return to List of Podcasts");
		System.out.println();
		
		return returnObject;
	}

}
