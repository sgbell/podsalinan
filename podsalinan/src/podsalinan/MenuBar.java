/*******************************************************************************
 * Copyright (c) 2011 Sam Bell.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or  any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See
 * the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Sam Bell - initial API and implementation
 ******************************************************************************/
/**
 *   MenuBar
 *   This class will be used to create the menu bar for our application. It uses an xml file to
 *   build the Menus
 *   
 *    Written By: Sam Bell
 */

package podsalinan;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar{
	private CommandPass actionListener;
	
	public MenuBar(){
	}
	
	public MenuBar(CommandPass aListener){
		actionListener = aListener;
	}

	public void createMenu(String filename) {
		XmlReader menuXml = new XmlReader(filename);
		
		for (int menuCount=0; menuCount < menuXml.getMenuCount(); menuCount++){
			JMenu menu = new JMenu(menuXml.getMenuName(menuCount));
			add(menu);
			
			for (int menuItemCount=0; menuItemCount < menuXml.getMenuItemCount(menuCount); menuItemCount++){
				addMenuItem(menuXml.getMenuItemValue(menuCount,menuItemCount,"name"),
						    KeyConvertor.keyCode(menuXml.getMenuItemValue(menuCount,menuItemCount,"keycode")),
					  	    menuXml.getMenuItemValue(menuCount,menuItemCount,"quickkeycode"),
					  	    menuXml.getMenuItemValue(menuCount,menuItemCount,"tooltip"),
					  	    actionListener,
					  	    menuXml.getMenuItemValue(menuCount,menuItemCount,"action"),
					  	    menu);
			}
		}
	}

	/**
	 *  This will streamline the functions I require for creating a menu
	 * item. It will create a new item and add it to the current sub-menu.
	 * @param menuItem is the string that will be displayed on the menu.
	 * @param menuKeyCode is the key that can be pressed with the menu open.
	 * @param quickKeyCode is the key that is pressed with the keyModifier from anywhere in the
	 *   application.
	 * @param description may be used in tooltips and the like.
	 * @param actionListener2 is the Action Listener that will handle all actions in the app.
	 * @param actionCommand is the string sent to the Action Listener.
	 */
	public void addMenuItem(String menuItemName, int menuKeyCode, String keyStroke, String description, CommandPass actionListener2,
			String actionCommand, JMenu menu){

			JMenuItem menuItem;

			if (menuKeyCode != 0)
				menuItem = new JMenuItem(menuItemName, menuKeyCode);
			else
				menuItem = new JMenuItem(menuItemName);

			menuItem.setAccelerator(KeyStroke.getKeyStroke(keyStroke));
			if (description != null)
				menuItem.getAccessibleContext().setAccessibleDescription(description);
			menuItem.addActionListener(actionListener2);
			menuItem.setActionCommand(actionCommand);
			menu.add(menuItem);
	}
}
