/**
 * 
 */
package bgdownloader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JMenu;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author bugman
 *
 */
public class XmlReader {

	public XmlReader(String xmlString){
		if (xmlString.contains("http://")){
			// if xmlString is a file located on the internet, download it first.
			/**
			 *  I have already Downloader class to take url and download to output directory.
			 *  Podcasts will initially be downloaded to a temp file, which will then be read,
			 *  and renamed
			 *  
			 *  Things to do:
			 *  1. Read Podcast in temp.xml to get name.
			 *  2. Rename temp.xml to podcast name.
			 *  3. Create seperate directory under settings directory to store podcasts
			 *  4. On start up program will scan podcast directory under settings, to populate
			 *       rssfeeds on startup.
			 */
			try {
				String localDirectory = System.getProperty("user.home");
				localDirectory=localDirectory.concat("/.bgdownloader");
				System.out.println(localDirectory);
				File settingsDir = new File(localDirectory);
				if (!settingsDir.exists()){
					settingsDir.mkdir();
				}
				String outputFile = localDirectory.concat("/temp.xml");
				
				Downloader d = new Downloader(new URL(xmlString),outputFile);
				d.start();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
					  
					  // This block will extract the menuitems
					  Element fstElmnt = (Element) currentNode;
					  NodeList menuItems = fstElmnt.getElementsByTagName("menuitem");
					  for (int menuItemCount = 0; menuItemCount < menuItems.getLength(); menuItemCount++) {
						  Node currentMenuItemNode = menuItems.item(menuItemCount);
						  if (currentMenuItemNode.getNodeType() == Node.ELEMENT_NODE){
							  
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
}
