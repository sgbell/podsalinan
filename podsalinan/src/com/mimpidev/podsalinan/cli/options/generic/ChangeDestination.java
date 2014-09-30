/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.generic;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInput;
import com.mimpidev.podsalinan.cli.ReturnCall;
import com.mimpidev.podsalinan.data.Podcast;
import com.mimpidev.podsalinan.data.URLDownload;

/**
 * @author sbell
 *
 */
public class ChangeDestination extends CLIOption {

	private CLInput input;
	/**
	 * @param newData
	 */
	public ChangeDestination(DataStorage newData) {
		super(newData);
		debug=true;
		input= new CLInput();
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ReturnCall execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo("["+getClass().getName()+"] command: "+command);
		
	    String commands[] = command.split(" ");
	    if (commands.length==3){
	    	if (commands[2].equalsIgnoreCase("podcast")){
				Podcast selectedPodcast = data.getPodcasts().getPodcastByUid(commands[0]);
				if (selectedPodcast!=null){
					System.out.println ();
					System.out.print ("Enter Podcast Download Directory["+selectedPodcast.getDirectory()+"]: ");
					String userInput=input.getStringInput();
			    	changeDirectory(selectedPodcast,userInput);
					returnObject.methodCall = "Podcast";
					returnObject.methodParameters = command.split(" ")[0];
				}
	    	} else if (commands[2].equalsIgnoreCase("download")){
				URLDownload selectedDownload = data.getUrlDownloads().findDownloadByUid(commands[0]);
				System.out.println("Enter Download Destination ["+selectedDownload.getDestination()+"]: ");
				String userInput = input.getStringInput();
				changeDirectory(,userInput);
	    	}
	    } 
		
		return returnObject;
	}

}
