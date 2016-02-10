/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.mainmenu;

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;

/**
 * @author sbell
 *
 */
public class ShowMenu extends CLIOption {

	/**
	 * @param newData
	 */
	public ShowMenu(DataStorage newData) {
		super(newData);

	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		if (!data.getSettings().isValidSetting("menuVisible") || data.getSettings().getSettingValue("menuVisible").equalsIgnoreCase("true")){
			System.out.println(data.getPodcasts().getList().size()+" - Podcasts. "+data.getUrlDownloads().visibleSize()+" - Downloads Queued");
			System.out.println();
			System.out.println("1. Podcasts Menu");
			System.out.println("2. Downloads Menu");
			System.out.println("3. Preferences");
			System.out.println();
			System.out.println("4. Quit");
		}
		returnObject.execute=false;
		return returnObject;
	}

	/*NOTE: The startup time may not be able to be increased. It still waits for the episodes to be loaded because
	 * the podcasts are stored in a Vector. You might be able to optimize the startup time further by changing the type
	 * of podcastList to ArrayList, and placing synchronize on either the code blocks, or the methods.
	 */
}
