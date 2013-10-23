/**
 * 
 */
package podsalinan;

import java.util.Vector;

/**
 * @author sbell
 *
 */
public class CLPodcastSelectedMenu extends CLMenu {

	/**
	 * @param newMenuList
	 * @param "podcast"
	 */
	public CLPodcastSelectedMenu(ProgSettings newMenuList, Vector<Podcast> podcasts) {
		super(newMenuList, "podcast_selected");
		String[] mainMenuList = {"1. List Episodes",
								 "2. Update List",
								 "3. Remove Podcast",
								 "<AA>. Select Episode",
								 "",
								 "9. Return to List of Podcasts"};
		setMainMenuList(mainMenuList);
	}

}
