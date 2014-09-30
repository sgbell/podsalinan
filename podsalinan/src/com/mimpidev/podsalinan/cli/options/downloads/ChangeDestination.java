/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.downloads;

import java.io.File;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInput;
import com.mimpidev.podsalinan.cli.ReturnCall;
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
		input = new CLInput();
	}

	/* (non-Javadoc)
	 * @see com.mimpidev.podsalinan.cli.CLIOption#execute(java.lang.String)
	 */
	@Override
	public ReturnCall execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo("["+getClass().getName()+"] command: "+command);

		if (command.split(" ").length>1){
			String downloadUid=command.split(" ")[0];
			System.out.println("Enter Download Destination ["+data.getUrlDownloads().findDownloadByUid(downloadUid).getDestination()+"]: ");
			String userInput = input.getStringInput();
			changeDirectory(downloadUid,userInput);
		}
		
		return returnObject;
	}
	
	public boolean changeDirectory(String downloadUid, String userInput){
		URLDownload selectedDownload= data.getUrlDownloads().findDownloadByUid(downloadUid);
		File newPath=null;
		if ((userInput.length()>0)&&(userInput!=null)){
			newPath = new File(userInput);
			if (newPath!=null){
				if ((newPath.exists())&&(newPath.isDirectory())){
					selectedDownload.setDestination(userInput);
					selectedDownload.setUpdated(true);
					System.out.println("Download path: "+selectedDownload.getDestination());
					return true;
				} else if ((newPath.getParentFile()!=null)&&((newPath.getParentFile().exists())&&
						   (newPath.getParentFile().isDirectory()))){
					System.out.println("Error: Directory does no exist.");
					if (input.confirmCreation()){
						newPath.mkdir();
						System.out.println("Directory Created: "+userInput);
						selectedDownload.setDestination(userInput);
						selectedDownload.setUpdated(true);
						return true;
					}
				} else {
					System.out.println ("Error: Invalid path");
				}
			}
		}
		return false;
	}
}
