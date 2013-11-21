/**
 * 
 */
package podsalinan;

import java.io.File;
import java.text.DecimalFormat;

/**
 * @author sbell
 *
 */
public class CLDownloadSelectedMenu extends CLMenu {
	private URLDownload download;
	private URLDownloadList downloadList;
	
	/**
	 * @param newMenuList
	 * @param downloadList2 
	 * @param newMenuName
	 */
	public CLDownloadSelectedMenu(ProgSettings newMenuList, URLDownloadList downloads) {
		super(newMenuList, "downloadSelected_menu");
		String[] menuList = {"1. Delete Download",
				             "2. Restart Download",
				             "3. Stop Download",
				             "4. Increase Priority",
				             "5. Decrease Priority",
				             "",
				             "9. Return to Download List"};
		setMainMenuList(menuList);
		setDownloadList(downloads);
	}

	public void setDownload(URLDownload urlDownload) {
		download = urlDownload;
	}

	public URLDownload getDownload(){
		return download;
	}

	/**
	 * @return the downloadList
	 */
	public URLDownloadList getDownloadList() {
		return downloadList;
	}

	/**
	 * @param downloadList the downloadList to set
	 */
	public void setDownloadList(URLDownloadList downloadList) {
		this.downloadList = downloadList;
	}

	public void printMainMenu() {
		System.out.println("URL: "+download.getURL().toString());
		switch (download.getStatus()){
			case Episode.NOT_STARTED:
				System.out.println ("Status: Not Downloaded");
				break;
			case Episode.CURRENTLY_DOWNLOADING:
				System.out.println ("Status: Currently Downloading");
				break;
			case Episode.PREVIOUSLY_STARTED:
				System.out.println ("Status: Download Incomplete");
				break;
			case Episode.FINISHED:
				System.out.println ("Status: Completed Download");
				break;
		}
		File destination = new File(download.getDestination());
		long fileSize;
		if (destination.exists())
			fileSize = destination.length();
		else
			fileSize = 0;

		
		// Need to make these sizes human readable
		System.out.println ("Downloaded: "+humanReadableSize(fileSize)+" / "+humanReadableSize(new Long(download.getSize()).longValue()));
		
		super.printMainMenu();
	}
	
	private String humanReadableSize(long fileSize) {
		String fileSizeModifier="";
		double newOutputSize;
		
		if (fileSize>1073741824){
			fileSizeModifier=" Gb";
			newOutputSize = (double)fileSize/1073741824;
		} else if (fileSize>1048576){
			fileSizeModifier=" Mb";
			newOutputSize = (double)fileSize/1048576;
		} else if (fileSize>1024){
			fileSizeModifier=" Kb";
			newOutputSize = (double)fileSize/1024;
		} else
			newOutputSize = (double)fileSize;

		newOutputSize=new Double(new DecimalFormat("#.##").format(newOutputSize)).doubleValue();
		
        if (newOutputSize==0)
        	return "0";
        else
        	return Double.toString(newOutputSize)+fileSizeModifier;
	}

	public void process(int inputInt){
		//System.out.println("CLDownloadSelectedMenu.process(int)");
		if (menuList.size()==2){
			switch (inputInt){
		        case 1:
		        	// Delete the download from the queue.
		        	
 		    	    // search downloaders array to see if it is currently downloading.
 		    	    // stop downloader if active
		        	
 		    	    downloadList.deleteDownload(downloadList.findDownload(download.getURL()));
 		    	    // because the download is being deleted, remove the selected download from the
 		    	    // menuList
 		    	    setDownload(null);
 		    	    menuList.removeSetting("selectedDownload");
		    	    break;
		        case 2:
		        	// Restart the download in the queue
		        	
 		    	    // search downloaders array to see if it is currently downloading.
 		    	    // stop downloader if active
		        	
		        	downloadList.restartDownload(downloadList.findDownload(download.getURL()));
		    	    break;
		        case 3:
		        	// Stop Download

		        	// search downloaders array to see if it is currently downloading.
 		    	    // stop downloader if active
		        	downloadList.cancelDownload(downloadList.findDownload(download.getURL()));
		    	    break;
		        case 4:
		        	// Increase Download priority
		        	downloadList.increasePriority(downloadList.findDownload(download.getURL()));
		        	break;
		        case 5:
		        	// Decrease download priority
		        	downloadList.decreasePriority(downloadList.findDownload(download.getURL()));
		        	break;
				case 9:
					setDownload(null);
					menuList.removeSetting("selectedDownload");
					break;
			}
		}
		//System.out.println("menuList.size()="+menuList.size());
		if (menuList.size()==2){
			inputInt=-1000;
			super.process(inputInt);
		}
	}
	
	public void process(String userInput){
		//System.out.println("CLDownloadSelectedMenu.process(String)");

		super.process(userInput);
	}
}
