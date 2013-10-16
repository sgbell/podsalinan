/**
 * 
 */
package podsalinan;

import java.util.ArrayList;
import java.util.Vector;

/**
 * @author bugman
 *
 */
public class CLMainMenu extends CLMenu {
	private Vector<Podcast> podcasts;
	private URLDownloadList urlDownloads;
	
	public CLMainMenu(ArrayList<Setting> parentMenuList, Vector<Podcast> podcasts, URLDownloadList urlDownloads) {
		super(parentMenuList,"Main Menu");
		this.podcasts = podcasts;
		this.urlDownloads = urlDownloads;
		
		String[] mainMenuList = {
				"1. Podcasts Menu",
				"2. Downloads Menu",
				"3. Preferences",
				"4. Quit"};
		setMainMenuList(mainMenuList);
	}
	
	public void printMainMenu(){
		System.out.println(podcasts.size()+" - Podcasts. "+urlDownloads.getDownloads().size()+" - Downloads Queued");
		super.printMainMenu();
	}
}
