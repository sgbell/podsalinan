/**
 * 
 */
package podsalinan;

import java.io.File;

/**
 * @author sbell
 *
 */
public class CLDownloadSelectedMenu extends CLMenu {
	private URLDownload download;
	
	/**
	 * @param newMenuList
	 * @param newMenuName
	 */
	public CLDownloadSelectedMenu(ProgSettings newMenuList) {
		super(newMenuList, "downloadSelected_menu");
		String[] menuList = {"1. Delete Download",
				             "2. Restart Download",
				             "3. Stop Download",
				             "4. Increase Priority",
				             "5. Decrease Priority",
				             "",
				             "9. Quit"};
		setMainMenuList(menuList);
	}

	public void setDownload(URLDownload urlDownload) {
		download = urlDownload;
	}

	public URLDownload getDownload(){
		return download;
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
		System.out.println ("Downloaded: "+fileSize+"/"+download.getSize());
		
		super.printMainMenu();
	}
	
	public void process(int inputInt){
		System.out.println("CLDownloadSelectedMenu.process(int)");
		if (menuList.size()==2){
			switch (inputInt){
		        case 1:
 		    	    System.out.println("Delete download from Queue.");
		    	    break;
		        case 2:
		    	    System.out.println("Restart download. (Stop download, delete local copy, and restart it in the queue).");
		    	    break;
		        case 3:
		    	    System.out.println("Stop Download. (Basically Pause the download, start downloading the next file in the queue).");
		    	    break;
		        case 4:
		        	System.out.println("Move the download up in the queue.");
		        	break;
		        case 5:
		        	System.out.println("Move the download down in the queue.");
		        	break;
				case 9:
					setDownload(null);
					menuList.removeSetting("selectedDownload");
					break;
			}
		}
		if (menuList.size()==2){
			inputInt=-1000;
			super.process(inputInt);
		}
	}
	
	public void process(String userInput){
		System.out.println("CLDownloadSelectedMenu.process(String)");

		super.process(userInput);
	}
}
