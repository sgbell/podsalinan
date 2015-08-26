/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.generic;

import java.io.File;
import java.util.Map;

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

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		if (debug) Podsalinan.debugLog.logMap(this, functionParms);
		
        if (functionParms.containsKey("uid")){
        	Podcast selectedPodcast = data.getPodcasts().getPodcastByUid(functionParms.get("uid"));
        	if (selectedPodcast!=null){
			   System.out.print ("Enter Podcast Download Directory["+selectedPodcast.getDirectory()+"]: ");
			   String userInput=input.getStringInput();
	    	   changeDirectory(selectedPodcast,userInput);
			   returnObject.methodCall = "podcast "+selectedPodcast.getDatafile();
			   returnObject.parameterMap.clear();
			   returnObject.execute=true;
        	} else {
               URLDownload selectedDownload = data.getUrlDownloads().findDownloadByUid(functionParms.get("uid"));
    		   if (selectedDownload!=null){
				  System.out.println("Enter Download Destination ["+selectedDownload.getDestination()+"]: ");
				  String userInput = input.getStringInput();
				  changeDirectory(selectedDownload,userInput);
				  returnObject.methodCall = "downloads "+selectedDownload.getUid();
                  returnObject.parameterMap.clear();
				  returnObject.execute=true;
    		    } else {
    			  System.out.println("Error: I'm not sure what destination you wanted to change.");
    		    }
            }
        } else {
        	// Handle direct call
        }
	    // Call direct with "set destination <path>"
	    /*if (commandOptions.length>1 && commandOptions[0].equalsIgnoreCase("destination")){
	    	Object selectedItem=null;
	    	if (CLInterface.cliGlobals.getGlobalSelection().containsKey("podcast")){
	    		selectedItem = data.getPodcasts().getPodcastByUid(CLInterface.cliGlobals.getGlobalSelection().get("podcast"));
	    	} else if (CLInterface.cliGlobals.getGlobalSelection().containsKey("downloads")){
	    		selectedItem = data.getUrlDownloads().findDownloadByUid(CLInterface.cliGlobals.getGlobalSelection().get("downloads"));
	    	}
	    	if (selectedItem!=null){
	    		changeDirectory(selectedItem,commandOptions[1]);
	    	}
	    } else {
		  System.out.println ();
		  Podcast selectedPodcast = data.getPodcasts().getPodcastByUid(commandOptions[0]);
		  if (selectedPodcast!=null){
    	  } else {
    		  URLDownload selectedDownload = data.getUrlDownloads().findDownloadByUid(commandOptions[0]);
    		  if (selectedDownload!=null){
    		  }
	      } 
		  //returnObject.methodParameters = command.split(" ")[0];
		  returnObject.execute=true;
	    }*/
	    
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
