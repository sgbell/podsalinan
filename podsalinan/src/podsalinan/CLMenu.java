/**
 * 
 */
package podsalinan;

import java.util.ArrayList;

/**
 * @author bugman
 *
 */
public class CLMenu {
	private ArrayList<Setting> menuList;
	private String[] mainMenuList; 

	public CLMenu(ArrayList<Setting> newMenuList){
		menuList = newMenuList;
	}
	
	public void printMainMenu(){
		
	}
	
	public void printSubMenu(){
		
	}
	
	/**
	 * @return the menuList
	 */
	public ArrayList<Setting> getMenuList() {
		return menuList;
	}
	/**
	 * @param menuList the menuList to set
	 */
	public void setMenuList(ArrayList<Setting> menuList) {
		this.menuList = menuList;
	}

	/**
	 * @return the mainMenuList
	 */
	public String[] getMainMenuList() {
		return mainMenuList;
	}

	/**
	 * @param mainMenuList the mainMenuList to set
	 */
	public void setMainMenuList(String[] mainMenuList) {
		this.mainMenuList = mainMenuList;
	}
}
