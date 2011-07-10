/**
 *   MenuBar
 *   This class will be used to create the menu bar for our application, I am tempted to hard code the menu initially,
 *   with the idea of copying the idea from KDE to use the same xml format for building the menubar.
 *   
 *    Written By: Sam Bell
 */

package bgdownloader;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar{
	private CommandPass actionListener;
	
	public MenuBar(){
	}
	
	public MenuBar(CommandPass aListener){
		actionListener = aListener;
	}

	/**
	 * 
	 * @param filename
	 */
	public void openXML(String filename){
		try {
			  File file = new File(filename);
			  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			  DocumentBuilder db = dbf.newDocumentBuilder();
			  Document doc = db.parse(file);
			  doc.getDocumentElement().normalize();
			  NodeList nodeList = doc.getElementsByTagName("menu");
			  
			  for (int s = 0; s < nodeList.getLength(); s++) {
				  Node currentNode = nodeList.item(s);
				  if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
					  // This next block of code retrieves the Menu name
					  //System.out.println("Menu : " + getNodeData(currentNode,"title"));
					  JMenu menu = new JMenu(getNodeData(currentNode,"title"));
					  add(menu);
					  
					  // This block will extract the menuitems
					  Element fstElmnt = (Element) currentNode;
					  NodeList menuItems = fstElmnt.getElementsByTagName("menuitem");
					  for (int menuItemCount = 0; menuItemCount < menuItems.getLength(); menuItemCount++) {
						  Node currentMenuItemNode = menuItems.item(menuItemCount);
						  if (currentMenuItemNode.getNodeType() == Node.ELEMENT_NODE){
							  // Menu Items

							  addMenuItem(getNodeData(currentMenuItemNode,"name"),
									       KeyConvertor.keyCode(getNodeData(currentMenuItemNode,"keycode")),
									  	   getNodeData(currentMenuItemNode,"quickkeycode"),
									  	   getNodeData(currentMenuItemNode,"tooltip"),
									  	   actionListener,
									  	   getNodeData(currentMenuItemNode,"action"),
									  	   menu);
						  }
					  }
				  }
			  }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This is used to pull the string data out of the xml file, if the tag does not exist
	 * null value is returned for the string.  
	 * @param currentNode passes in the current Node in the XML document.
	 * @param tagName passes in the tag we are looking for in the current Node.
	 * @return A string value. If the tag does not exist in the current Node, null will be
	 *   returned.
	 */
	private String getNodeData (Node currentNode, String tagName){
		String nodeValue;
		Element fstElmnt = (Element) currentNode;
		NodeList titleElmntLst = fstElmnt.getElementsByTagName(tagName);
		Element titleElmnt = (Element) titleElmntLst.item(0);
		if (titleElmnt != null) {
			NodeList menuName = titleElmnt.getChildNodes();
			nodeValue = ((Node) menuName.item(0)).getNodeValue();
		} else
			nodeValue = null;
		return nodeValue;
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