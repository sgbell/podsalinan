/**
 * 
 */
package com.mimpidev.podsalinan.cli.options;

import java.util.HashMap;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnCall;
import com.mimpidev.podsalinan.cli.options.podcast.*;
import com.mimpidev.podsalinan.data.Podcast;

/**
 * @author sbell
 *
 */
public class PodcastCommand extends CLIOption {

	/**
	 * @param newData
	 */
	public PodcastCommand(DataStorage newData) {
		super(newData);
		options = new HashMap<String, CLIOption>();
		options.put("<a-zz>", new SelectPodcast(newData));
		options.put("showMenu", new ShowMenu(newData));
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ReturnCall execute(String command) {
		returnObject = new ReturnCall();
		returnObject.methodCall="podcast";
		if (options.containsKey(command))
			options.get(command).execute(command);
		else{
			try {
				// Check if the value is a number and act accordingly
				Integer.parseInt(command);
				if (command.equals("9")){
					returnObject.methodCall="";
					returnObject.methodParameters="";
				} else {
					// If the user has entered 8 characters find the right podcast in the list, and the hash happens to
					// be completely numerical
					for (int count=0; count<data.getPodcasts().getList().size(); count++){
						Podcast currentPodcast = data.getPodcasts().getList().get(count);
						if (currentPodcast.getDatafile().equals(command))
							returnObject = options.get("<a-zz>").execute(Integer.toString(count));
					}
				}
			} catch (NumberFormatException e) {
				if (command.length()==0){
					returnObject = options.get("showMenu").execute("");
				// 8 characters is the unique identifier we are using for the podcast. So if it is less than 8 numbers
				// it will be the number in the array
				}else if (command.length()<8){
					if (command.split(" ").length==1)
						command = Integer.toString(convertCharToNumber(command));
					returnObject = options.get("<a-zz>").execute(command);
				} else {
					// If the user has entered 8 characters find the right podcast in the list
					for (int count=0; count<data.getPodcasts().getList().size(); count++){
						Podcast currentPodcast = data.getPodcasts().getList().get(count);
						if (currentPodcast.getDatafile().equals(command))
							returnObject = options.get("<a-zz>").execute(Integer.toString(count));
					}
				}
			}
		}
		return returnObject;
	}
}
