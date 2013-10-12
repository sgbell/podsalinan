/**
 * 
 */
package podsalinan;

import java.util.ArrayList;

/**
 * @author bugman
 *
 */
public class MainMenu extends CLMenu {
	
	/**
	 * 
	 */
	public MainMenu(ArrayList<Setting> parentMenuList) {
		super(parentMenuList);
		setMainMenuList((String[] {"1. Podcasts Menu","2. Downloads Menu"}));
	}

	@Override
	public void printMainMenu() {
		System.out.println();
		System.out.println("1. Podcasts Menu");
		System.out.println("2. Downloads Menu");
		System.out.println("3. Preferences");
		System.out.println("4. Quit");
		System.out.println();
	}

	@Override
	public void printSubMenu() {
		
	}

}
