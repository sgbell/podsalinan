package bgdownloader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * @author bugman
 *
 */
public class CommandPass implements ActionListener {
	private JTree tree;
	private BGDownloader bgdownloader;
	private boolean programExiting;

	public CommandPass(boolean programExiting) {
		this.programExiting=programExiting;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		
		if (command.compareTo("quit")==0){
			programExiting=true;
			bgdownloader.saveSettings();
			System.exit(0);
		}
		if (command.compareTo("addURL")==0){
			String url = (String)JOptionPane.showInputDialog(
                    bgdownloader,
                    "Please enter the URL to download",
                    "Add URL",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    null);
			if (!url.isEmpty())
				bgdownloader.addDownload(url);
		}
		if (command.compareTo("addRSS")==0){
			String url = (String)JOptionPane.showInputDialog(
                    bgdownloader,
                    "Please enter the URL to download",
                    "Add RSS Feed",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    null);
			if (!url.isEmpty())
				bgdownloader.addRssFeed(url);
			
		}
		if (command.compareTo("setDownloadFolder")==0){
			// Grab the selected Node, so we can change it's download folder.
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
			if (selectedNode!=null){
				// Make an Object out of the selected node.
				Object nodeInfo = selectedNode.getUserObject();
				// Read below. if the node is a Leaf, not a branch, and it's also an RssFeed then we want to delete it. 
			    if (selectedNode.isLeaf() && (nodeInfo instanceof RssFeedDetails)){
			    	// Grab the object so we can delete the file and the entry
			    	// in the podcast databast
			    	RssFeedDetails podcast = (RssFeedDetails) selectedNode.getUserObject();
			    	// Create and Show a Directory Dialog for the user to choose the feed
			    	// directory.
			    	JFileChooser browseWindow= new JFileChooser();
			    	// Sets what is able to be selected. in this case, a folder.
			    	browseWindow.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    	// bwr is browser window result
			    	int bwr = browseWindow.showOpenDialog(bgdownloader);
			    	// 
			    	if (bwr==JFileChooser.APPROVE_OPTION){
			    		File chosenDir = browseWindow.getSelectedFile();
		    			// Don't need to test if chosen Dir is a folder, as it has already
			    		// been tested in the if statement.
			    		
			    		// If podcast folder has been changed, re-queue downloads, as download folder has changed
			    		podcast.setLocalStore(chosenDir.toString());
			    			
			    	} else {
			    		
			    	}
			    }
			}
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
		    	// Grab the object so we can delete the file and the entry
		    	// in the podcast databast
		    	RssFeedDetails podcast = (RssFeedDetails) selectedNode.getUserObject();
		    	podcast.getDetails().remove=true;
		    	
		    	// Following 2 lines remove rss feed from node.
		    	DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
		    	model.removeNodeFromParent(selectedNode);
		    	bgdownloader.getTreePane().changeDownloadList("Downloads");
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
	
	public void setParent(BGDownloader parent){
		bgdownloader = parent;
	}
}
