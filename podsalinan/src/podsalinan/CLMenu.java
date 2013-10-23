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
	private ArrayList<CLMenu> submenus;

	public CLMenu(ProgSettings newMenuList, String newMenuName){
		menuList = newMenuList;
		setMenuName(newMenuName);
		submenus = new ArrayList<CLMenu>();
	}
	
	public void printMainMenu(){
		System.out.println();
		for (String line : mainMenuList){
			System.out.println(line);
		}
		System.out.println();
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

	public ArrayList<CLMenu> getSubmenus() {
		return submenus;
	}

	public void setSubmenus(ArrayList<CLMenu> submenus) {
		this.submenus = submenus;
	}
	
	public void addSubmenu(CLMenu newSubmenu){
		submenus.add(newSubmenu);
	}
	
	public void removeSubmenu(String name){
		boolean found = false;
		int count=0;
		while ((!found)&&(count<submenus.size())){
			if (submenus.get(count).getMenuName().equalsIgnoreCase(name))
				found=true;
		}
		if ((true)&&(count<submenus.size()))
			submenus.remove(count);
	}
	
	public CLMenu findSubmenu(String name){
		for (CLMenu submenu : submenus){
			if (submenu.getMenuName().equalsIgnoreCase(name)){
				return submenu; 
			}
		}
		return null;
	}
	
	public void process(int userInputInt){
		if (userInputInt== -1000)
			printMainMenu();
	}
	
	public void process(String userInput){
		if (userInput==null)
			printMainMenu();
	}
	
	public int convertCharToNumber(String input) {
		int number=1;
		if (input.length()>1){
			for (int charCount=0; charCount < input.length()-1; charCount++)
				number=number*26*(int)(input.toUpperCase().charAt(charCount)-64);
			number+=(int)(input.toUpperCase().charAt(input.length()-1)-64);
		} else if (input.length()==1)
			number=(int)(input.toUpperCase().charAt(0)-64);
		number--;
		
		return number;
	}
}
