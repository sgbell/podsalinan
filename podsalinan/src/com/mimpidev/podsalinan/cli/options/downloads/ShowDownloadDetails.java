/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.downloads;

import java.io.File;
import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInterface;
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
		}
	}
	
	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		String command="";
		debug=true;
		if (debug) Podsalinan.debugLog.logInfo(this,"command: "+command);
		String[] commandOptions = command.split(" ");
		if (debug) Podsalinan.debugLog.logInfo(this, "selection: "+CLInterface.cliGlobals.globalSelectionToString());
		if (command.length()==0 && CLInterface.cliGlobals.getGlobalSelection().containsKey("downloads")){
			String downloadUid = CLInterface.cliGlobals.getGlobalSelection().get("downloads");
			URLDownload currentDownload = data.getUrlDownloads().findDownloadByUid(downloadUid);
			
			System.out.println();
			printDetails(currentDownload,true);
			System.out.println();
			returnObject.execute=true;
		} else {
			URLDownload currentDownload = data.getUrlDownloads().findDownloadByUid(commandOptions[0]);
			System.out.println();
			printDetails(currentDownload,false);
			System.out.println();
		}
		
		return returnObject;
	}

}
