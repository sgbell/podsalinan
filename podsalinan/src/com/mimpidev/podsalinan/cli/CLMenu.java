/**
 * 
 */
package com.mimpidev.podsalinan.cli;

import java.util.ArrayList;

import com.mimpidev.podsalinan.data.ProgSettings;

/**
 * @author bugman
 *
 */
public class CLMenu {
	protected ArrayList<CLInterface.MenuPath> menuList;
	private String[] mainMenuList; 
	private String menuName;
	private ArrayList<CLMenu> submenus;

	public CLMenu(ArrayList<CLInterface.MenuPath> newMenuList, String newMenuName){
		menuList = newMenuList;
		setMenuName(newMenuName);
		submenus = new ArrayList<CLMenu>();
		mainMenuList = new String[]{ "I'm sorry there are currently no entries for this menu module."};
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
	public ArrayList<CLInterface.MenuPath> getMenuList() {
		return menuList;
	}
	/**
	 * @param menuList the menuList to set
	 */
	public void setMenuList(ArrayList<CLInterface.MenuPath> menuList) {
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
			// Going to search the entire menu tree for the menu we want
			CLMenu childMenu = submenu.findSubmenu(name);
			if (childMenu!=null)
				return childMenu;
		}
		return null;
	}
	
	public void process(int userInput){
		//System.out.println("Debug: CLMenu.process(int)");
		//System.out.println("userInput="+userInput);
		if (userInput== -1000)
			printMainMenu();
	}
	
	public void process(String userInput){
		//System.out.println("Debug: CLMenu.process(String)");
		if (userInput==null)
			printMainMenu();
	}
	

}