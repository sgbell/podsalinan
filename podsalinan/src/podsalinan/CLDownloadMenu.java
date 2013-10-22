/**
 * 
 */
package podsalinan;

/**
 * @author bugman
 *
 */
public class CLDownloadMenu extends CLMenu{
	private URLDownloadList urlDownloads;

	public CLDownloadMenu(ProgSettings newMenuList, URLDownloadList downloadList) {
		super(newMenuList,"downloads");
		String[] mainMenuList = {"(A-ZZ) Enter Download letter to select Download.",
								 "To add a new download to the queue just enter the the url to be downloaded.",
								 "",
								 "9. Return to Main Menu"

		};
		setMainMenuList(mainMenuList);
		setUrlDownloads(downloadList);
	}

	public void printMainMenu() {
		int downloadCount=1;
		for (URLDownload download : urlDownloads.getDownloads()){
			if (!download.isRemoved()){
				System.out.println(getEncodingFromNumber(downloadCount)+". "+download.getURL().toString());
			}
		}
		
		super.printMainMenu();
	}

	public void process(int userInputInt) {
		switch (userInputInt){
			case 9:
				menuList.clear();
				break;
		}
		super.process(userInputInt);
	}

	public URLDownloadList getUrlDownloads() {
		return urlDownloads;
	}

	public void setUrlDownloads(URLDownloadList urlDownloads) {
		this.urlDownloads = urlDownloads;
	}

}
