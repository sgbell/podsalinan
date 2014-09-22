/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.podcast;

import java.io.File;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInput;
import com.mimpidev.podsalinan.cli.ReturnCall;
import com.mimpidev.podsalinan.data.Podcast;

/**
 * @author sbell
 *
 */
public class ChangeDownloadDirectory extends CLIOption {


	private CLInput input;
	/**
	 * @param newData
	 */
	public ChangeDownloadDirectory(DataStorage newData) {
		super(newData);
		debug=true;
		input = new CLInput();
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ReturnCall execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo("["+getClass().getName()+"] command: "+command);
		
		if (command.split(" ").length>1){
			Podcast selectedPodcast = data.getPodcasts().getPodcastByUid(command.split(" ")[0]);
			if (selectedPodcast!=null){

				System.out.println ();
				System.out.print ("Enter Podcast Download Directory["+selectedPodcast.getDirectory()+"]: ");
				String userInput=input.getStringInput();
		    	changeDirectory(selectedPodcast,userInput);
				returnObject.methodCall = "Podcast";
				returnObject.methodParameters = command.split(" ")[0];
			}

		}
		
		return returnObject;
	}

	public boolean changeDirectory(Podcast podcast, String userInput){
		File newPath;
		if ((userInput.length()>0)&&(userInput!=null)){
			newPath=new File(userInput);
			if ((newPath.exists())&&(newPath.isDirectory())){
				podcast.setDirectory(userInput);
				System.out.println("Podcast Download Directory: "+podcast.getDirectory());
				return true;
			} else if ((newPath.getParentFile().exists())&&
					   (newPath.getParentFile().isDirectory())){
				System.out.println("Error: Directory does no exist.");
				if (input.confirmCreation()){
					newPath.mkdir();
					System.out.println("Podcast Download Directory Created: "+newPath);
					podcast.setDirectory(userInput);
				}
			} else {
				System.out.println ("Error: Invalid path");
			}
		}
		return false;
	}
}
