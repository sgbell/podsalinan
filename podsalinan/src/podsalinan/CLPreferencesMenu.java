/**
 * 
 */
package podsalinan;


/**
 * @author bugman
 *
 */
public class CLPreferencesMenu extends CLMenu{
	private ProgSettings settings;

	public CLPreferencesMenu(ProgSettings newMenuList, ProgSettings preferences) {
		super(newMenuList,"preferences");
		String[] mainMenuList = {"1. Change Podcast Update Rate",
								 "2. Number of Downloaders",
								 "3. Default Download Directory",
								 "4. Automatically Download New Podcast Episodes",
								 "5. Set Download Speed Limit",
								 "",
								 "0. Return to Preferences Menu"};
		setMainMenuList(mainMenuList);
		setSettings(preferences);
	}

	@Override
	public void printMainMenu() {
		super.printMainMenu();
	}

	public void process(int userInputInt) {
		switch (userInputInt){
			case 0:
				menuList.clear();
		}
		super.process(userInputInt);
	}

	public ProgSettings getSettings() {
		return settings;
	}

	public void setSettings(ProgSettings settings) {
		this.settings = settings;
	}

}
