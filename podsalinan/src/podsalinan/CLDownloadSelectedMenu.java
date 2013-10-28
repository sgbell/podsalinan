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
		// TODO Auto-generated constructor stub
	}

	public void setDownload(URLDownload urlDownload) {
		download = urlDownload;
	}

	public URLDownload getDownload(){
		return download;
	}
}