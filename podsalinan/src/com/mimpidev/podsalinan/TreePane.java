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
 * This is the class for building the treepane.
 */
package com.mimpidev.podsalinan;

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
