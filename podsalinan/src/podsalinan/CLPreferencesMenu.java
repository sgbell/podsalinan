/**
 * 
 */
package podsalinan;

import java.util.ArrayList;

/**
 * @author bugman
 *
 */
public class CLPreferencesMenu extends CLMenu implements CLMenuInterface {
	private ProgSettings settings;

	public CLPreferencesMenu(ProgSettings newMenuList, ProgSettings preferences) {
		super(newMenuList,"Preferences Menu");
		setSettings(preferences);
	}

	@Override
	public void printMainMenu() {

	}

	@Override
	public void process(int userInputInt) {
		// TODO Auto-generated method stub
		
	}

	public ProgSettings getSettings() {
		return settings;
	}

	public void setSettings(ProgSettings settings) {
		this.settings = settings;
	}

}
