package bgdownloader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * @author bugman
 *
 */
public class CommandPass implements ActionListener {
	private JTree tree;

	@Override
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		
		if (command.compareTo("quit")==0){
			System.exit(0);
		}
		
		if (command.compareTo("addURL")==0){
			System.out.println("command adding url");
		}
		if (command.compareTo("addRSS")==0){
			System.out.println("command adding rss feed");
			AddFeedDialog newfeed = new AddFeedDialog();
			
		}
		if (command.compareTo("setDownloadFolder")==0){
			System.out.println("command changing download folder");
		}
		if (command.compareTo("updateInterval")==0){
			System.out.println("command update interval");
		}
		if (command.compareTo("removeFeed")==0){
			// Grab the selected Node, so we can remove it.
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
			// Make an Object out of the selected node.
			Object nodeInfo = selectedNode.getUserObject();
			// Read below. if the node is a Leaf, not a branch, and it's also an RssFeed then we want to delete it. 
		    if (selectedNode.isLeaf() && (nodeInfo instanceof RssFeedDetails)){
		      DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
		      model.removeNodeFromParent(selectedNode);
		    }
		}
	}

	/**
	 * setTree - grab a copy of the Tree in the tree pane, so we can work with it
	 * @param mainTree
	 */
	public void setTree(JTree mainTree){
		tree = mainTree;
	}
}
