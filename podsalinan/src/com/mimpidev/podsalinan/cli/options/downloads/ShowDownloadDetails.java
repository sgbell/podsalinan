/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.downloads;

import java.io.File;
import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.dev.debug.Log;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.data.URLDownload;

/**
 * @author sbell
 *
 */
public class ShowDownloadDetails extends CLIOption {


	/**
	 * @param newData
	 */
	public ShowDownloadDetails(DataStorage newData) {
		super(newData);
	}

	public void printDetails(URLDownload selectedDownload, boolean showDirectory){
		if (selectedDownload!=null){
			System.out.println("URL: "+selectedDownload.getURL().toString());
			System.out.println("Status: "+selectedDownload.getCurrentStatus());

			if ((showDirectory)&&(selectedDownload.getDestination()!=null))
				System.out.println("Destination: "+selectedDownload.getDestination());

			long fileSize;
			//String filePath=selectedDownload.getDestination()+fileSystemSlash+getFilenameDownload();
			if (!selectedDownload.getDestinationFile().isDirectory()){
				File destination = selectedDownload.getDestinationFile();
				if (destination.exists())
					fileSize = destination.length();
				else
					fileSize = 0;
			} else {
				fileSize=0;
			}

			// Need to make these sizes human readable
			long fullSize=0;
			try {
				fullSize = new Long(selectedDownload.getSize()).longValue();
			} catch (NumberFormatException e){
				fullSize = -1;
			}
			
			System.out.println ("Downloaded: "+humanReadableSize(fileSize)+" / "+humanReadableSize(fullSize));
			//TODO: 0.1 - Create static method in DownloadQueue, that will create an array list of currently downloading urls, and speeds
		}
	}
	
	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		if (debug) if (Log.isDebug())Log.logMap(this,functionParms);

		if (functionParms.containsKey("uid")){
			URLDownload selectedDownload = data.getUrlDownloads().findDownloadByUid(functionParms.get("uid"));
			System.out.println();
			printDetails(selectedDownload,!functionParms.containsKey("menuCall"));
			System.out.println();
		} else {
			System.out.println("Error: Invalid command call");
		}
		
		returnObject.methodCall = "downloads "+functionParms.get("uid");
		returnObject.execute=false;
		
		return returnObject;
	}

}
