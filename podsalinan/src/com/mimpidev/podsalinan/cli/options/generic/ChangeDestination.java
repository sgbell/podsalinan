/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.generic;

import java.io.File;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInput;
import com.mimpidev.podsalinan.cli.ReturnObject;
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
	public ReturnObject execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo("["+getClass().getName()+"] command: "+command);
		
		System.out.println ();
	    String commands[] = command.split(" ");
		Podcast selectedPodcast = data.getPodcasts().getPodcastByUid(commands[0]);
		if (selectedPodcast!=null){
			System.out.print ("Enter Podcast Download Directory["+selectedPodcast.getDirectory()+"]: ");
			String userInput=input.getStringInput();
	    	changeDirectory(selectedPodcast,userInput);
			returnObject.methodCall = "podcast";
			returnObject.methodParameters = command.split(" ")[0];
    	} else {
    		URLDownload selectedDownload = data.getUrlDownloads().findDownloadByUid(commands[0]);
    		if (selectedDownload!=null){
				System.out.println("Enter Download Destination ["+selectedDownload.getDestination()+"]: ");
				String userInput = input.getStringInput();
				changeDirectory(selectedDownload,userInput);
    		}
	    } 
		returnObject.execute=true;
		
		return returnObject;
	}

	public boolean changeDirectory(Object item, String userInput){
		File newPath=null;
		boolean setNewPath=false;
		if ((userInput.length()>0)&&(userInput!=null)){
			newPath=new File(userInput);
			if ((newPath!=null)&&(newPath.exists())&&(newPath.isDirectory())){
				setNewPath=true;
			} else if ((newPath.getParentFile()!=null)&&((newPath.getParentFile().exists())&&
					   (newPath.getParentFile().isDirectory()))){
				System.out.println("Error: Directory does not exist.");
				if (input.confirmCreation()){
					newPath.mkdir();
					System.out.println("Directory Created: "+userInput);
					setNewPath=true;
				}
			} else {
				System.out.println ("Error: Invalid path");
			}
			if (setNewPath){
				if (item instanceof Podcast){
					((Podcast)item).setDirectory(userInput);
					return true;
				} else if (item instanceof URLDownload){
					((URLDownload)item).setDestination(userInput);
					((URLDownload)item).setUpdated(true);
					return true;
				}
			}
		}
		return false;
	}
}
