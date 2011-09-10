/**
 * This is the class for building the treepane.
 */
package podsalinan;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * @author bugman
 *
 */
@SuppressWarnings("serial")
public class TreePane extends JPanel {
	private DefaultMutableTreeNode rootNode;
	private DefaultMutableTreeNode rssFeeds;
	private URLDownloadList downloads;
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
	public void addrssFeed (Podcast newFeed){
		DefaultTreeModel model = (DefaultTreeModel)treePanel.getModel();
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newFeed,false);

		model.insertNodeInto(newNode, rssFeeds, rssFeeds.getChildCount());
	}
	
	/**
	 * setDownloads is used to access & modify the URL downloads list
	 * @param downloadsList
	 */
	public void setDownloads (URLDownloadList downloadsList){
		DefaultMutableTreeNode newDownloads = new DefaultMutableTreeNode(downloadsList,false);
		rootNode.add(newDownloads);
		downloads = downloadsList;
	}
	
	public URLDownloadList getDownloads (){
		return downloads;
	}

	public void changeDownloadList(String viewName){
		treeEventHandler.setDownloadPane(viewName);
	}
	
	public void cardView(JPanel cardPane) {
		treeEventHandler.cardView(cardPane);
	}
	
	public JTree getTree(){
		return treePanel;
	}
	
	public DefaultMutableTreeNode getRssFeeds(){
		return rssFeeds;
	}
}