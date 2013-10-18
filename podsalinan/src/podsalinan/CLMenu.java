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
	protected ProgSettings menuList;
	private String[] mainMenuList; 
	private String menuName;

	public CLMenu(ProgSettings newMenuList, String newMenuName){
		menuList = newMenuList;
		setMenuName(newMenuName);
	}
	
	public void printMainMenu(){
		System.out.println();
		for (String line : mainMenuList){
			System.out.println(line);
		}
		System.out.println();
	}
	
	public void printSubMenu(){
		
	}
	
	/**
	 * @return the menuList
	 */
	public ProgSettings getMenuList() {
		return menuList;
	}
	/**
	 * @param menuList the menuList to set
	 */
	public void setMenuList(ProgSettings menuList) {
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

	public String getCharForNumber(int i){
		return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
	}
	
	public String getEncodingFromNumber(int number){
		String charOutput="";
		if (number<27)
			charOutput = getCharForNumber(number);
		else {
			if (number%26!=0){
				charOutput+=getCharForNumber(number/26);
				charOutput+=getCharForNumber(number%26);
			} else {
				charOutput=getCharForNumber((number/26)-1)+"Z";
			}
		}
		
		return charOutput;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
}
