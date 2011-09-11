/*******************************************************************************
 * Copyright (c) 2011 Sam Bell.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Sam Bell - initial API and implementation
 ******************************************************************************/
/**
 * 
 */
package podsalinan;

import java.awt.CardLayout;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author bugman
 *
 */
public class TreeEvents implements TreeSelectionListener {
	
	private JTree treePane;
	private JPanel cardsView;
	public TreeEvents(JTree tree){
		treePane = tree;
	}
	
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePane.getLastSelectedPathComponent();
		
		if (node == null) return;
		
		Object nodeInfo = node.getUserObject();
		if (node.isLeaf()){
			if (nodeInfo instanceof Podcast){
				Podcast rssFeeds = (Podcast)nodeInfo;
				setDownloadPane(rssFeeds.getName());
			} else if (nodeInfo instanceof URLDownloadList){
				setDownloadPane("Downloads");
			}
		}
	}

	public void setDownloadPane(String feedName){
		CardLayout cardsLayout = (CardLayout)(cardsView.getLayout());
		cardsLayout.show(cardsView,feedName);
	}

	public void cardView(JPanel cardPane) {
		cardsView = cardPane;
	}
}
