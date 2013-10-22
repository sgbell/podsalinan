/**
 * 
 */
package podsalinan;

/**
 * @author bugman
 *
 */
public class CLDownloadMenu extends CLMenu implements CLMenuInterface {
	private URLDownloadList urlDownloads;

	public CLDownloadMenu(ProgSettings newMenuList, URLDownloadList downloadList) {
		super(newMenuList,"Download Menu");
		String[] mainMenuList = {"(A-ZZ) Enter Download letter to select Download.",
								 "To add a new download to the queue just enter the the url to be downloaded.",
								 "",
								 "9. Return to Main Menu"

		};
		setMainMenuList(mainMenuList);
		setUrlDownloads(downloadList);
	}

	@Override
	public void printMainMenu() {
		super.printMainMenu();
	}

	@Override
	public void process(int userInputInt) {
		// TODO Auto-generated method stub
		
	}

	public URLDownloadList getUrlDownloads() {
		return urlDownloads;
	}

	public void setUrlDownloads(URLDownloadList urlDownloads) {
		this.urlDownloads = urlDownloads;
	}

}
