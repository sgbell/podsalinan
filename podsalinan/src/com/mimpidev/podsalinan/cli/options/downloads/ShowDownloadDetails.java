/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.downloads;

import java.io.File;

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
			switch (selectedDownload.getStatus()){
				case URLDownload.DOWNLOAD_QUEUED:
					System.out.println ("Status: Download Queued");
					break;
				case URLDownload.CURRENTLY_DOWNLOADING:
					System.out.println ("Status: Currently Downloading");
					break;
				case URLDownload.INCOMPLETE_DOWNLOAD:
					System.out.println ("Status: Download Incomplete");
					break;
				case URLDownload.FINISHED:
					System.out.println ("Status: Completed Download");
					break;
				case URLDownload.DOWNLOAD_CANCELLED:
					System.out.println ("Status: Download Cancelled");
				default:
					System.out.println ("Status: "+selectedDownload.getStatus());
			}
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
	public ReturnObject execute(String command) {
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
