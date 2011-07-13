/**
 * This is the class for building the treepane.
 */
package bgdownloader;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author bugman
 *
 */
@SuppressWarnings("serial")
public class TreePane extends JPanel {
	private DefaultMutableTreeNode rootNode;
	private DefaultMutableTreeNode rssFeeds;
	private TreeEvents treeEventHandler;
	private JTree treePanel;

	public TreePane(){
		rootNode = new DefaultMutableTreeNode("Downloads");
		treePanel = new JTree(rootNode);
		treeEventHandler = new TreeEvents(treePanel);
		treePanel.addTreeSelectionListener(treeEventHandler);
		
		JScrollPane scrollPane = new JScrollPane(treePanel);
		
		this.setLayout(new BorderLayout());
		this.add(scrollPane);
		
		treePanel.setShowsRootHandles(true);
		
		DefaultMutableTreeNode category = null;
		
		category = new DefaultMutableTreeNode("RSS Feeds",true);
		rssFeeds = category;
		rootNode.add(category);
		
	}
	/**
	 * addrssFeed is used to add new RSS Feeds into the downloader.
	 * @param newFeed
	 */
	public void addrssFeed (RssFeedDetails newFeed){
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newFeed,false);
		rssFeeds.add(newNode);
	}
	
	/**
	 * setDownloads is used to access & modify the URL downloads list
	 * @param downloadsList
	 */
	public void setDownloads (URLDownloadList downloadsList){
		DefaultMutableTreeNode newNode;
		newNode = new DefaultMutableTreeNode(downloadsList,false);
		rootNode.add(newNode);
	}

	public void cardView(JPanel cardPane) {
		treeEventHandler.cardView(cardPane);
	}
	
	public JTree getTree(){
		return treePanel;
	}
}
