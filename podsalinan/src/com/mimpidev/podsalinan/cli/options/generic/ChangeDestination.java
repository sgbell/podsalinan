/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.generic;

import java.io.File;
import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.dev.debug.Log;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInput;
import com.mimpidev.podsalinan.cli.CLInterface;
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
		input= new CLInput();
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		if (debug) if (Log.isDebug())Log.logMap(this, functionParms);
		
        if (functionParms.containsKey("uid")){
        	Podcast selectedPodcast = data.getPodcasts().getPodcastByUid(functionParms.get("uid"));
        	if (selectedPodcast!=null){
			   System.out.print ("Enter Podcast Download Directory["+selectedPodcast.getDirectory()+"]: ");
			   String userInput=input.getStringInput();
	    	   changeDirectory(selectedPodcast,userInput);
			   returnObject.methodCall = "podcast "+selectedPodcast.getDatafile();
			   returnObject.execute=true;
        	} else {
               URLDownload selectedDownload = data.getUrlDownloads().findDownloadByUid(functionParms.get("uid"));
    		   if (selectedDownload!=null){
				  System.out.println("Enter Download Destination ["+selectedDownload.getDestination()+"]: ");
				  String userInput = input.getStringInput();
				  changeDirectory(selectedDownload,userInput);
				  returnObject.methodCall = "downloads "+selectedDownload.getUid();
				  returnObject.execute=true;
    		    } else {
    			  System.out.println("Error: I'm not sure what destination you wanted to change.");
    		    }
            }
        } else if (functionParms.containsKey("path") && CLInterface.cliGlobals.getGlobalSelection().size()>0){
    		File newPath = new File(functionParms.get("path"));
        	if ((CLInterface.cliGlobals.getGlobalSelection().containsKey("podcastid"))||
        		(CLInterface.cliGlobals.getGlobalSelection().containsKey("downloads"))){
        		if (!newPath.exists()){
        			if (newPath.getParentFile().exists()){
        				System.out.println("Directory does not exists.");
        				if (input.confirmCreation()){
        					newPath.mkdir();
        					System.out.println("Directory Created: "+newPath.getPath());
        				}
        			}
        		}
        		if (newPath.exists()){
        			if (CLInterface.cliGlobals.getGlobalSelection().containsKey("podcastid")){
        				Podcast selectedPodcast = data.getPodcasts().getPodcastByUid(CLInterface.cliGlobals.getGlobalSelection().get("podcastid"));
        				if (selectedPodcast!=null){
        					selectedPodcast.setDirectory(newPath.getPath());
        					selectedPodcast.setUpdated(true);
        					returnObject.methodCall="podcast "+selectedPodcast.getDatafile();
        				}
        			} else if (CLInterface.cliGlobals.getGlobalSelection().containsKey("downloads")){
        				URLDownload selectedDownload = data.getUrlDownloads().findDownloadByUid(CLInterface.cliGlobals.getGlobalSelection().get("downloads"));
        				if (selectedDownload!=null){
        					selectedDownload.setDirectory(newPath.getPath());
        					selectedDownload.setUpdated(true);
        					returnObject.methodCall="downloads "+selectedDownload.getUid();
        				}
        			}
        		} else {
        			System.out.println("Error: Directory does not exist.");
        		}
        	} else {
        		System.out.println("Error: No Podcast or Download selected.");
        	}
        } else {
        	System.out.println("Error: Invalid call.");
        }
        returnObject.parameterMap.clear();
	    
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
					((Podcast)item).setUpdated(true);
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
