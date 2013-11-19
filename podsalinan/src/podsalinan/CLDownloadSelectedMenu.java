/**
 * 
 */
package podsalinan;

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
				             "3. Increase Priority",
				             "4. Decrease Priority",
				             "",
				             "9. Quit"};
	}

	public void setDownload(URLDownload urlDownload) {
		download = urlDownload;
	}

	public URLDownload getDownload(){
		return download;
	}
	
	public void process(int inputInt){
		System.out.println("CLDownloadSelectedMenu.process(int)");
		if (menuList.size()==2){
			switch (inputInt){
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
