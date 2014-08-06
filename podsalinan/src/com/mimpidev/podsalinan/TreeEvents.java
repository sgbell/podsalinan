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
 * 
 */
package com.mimpidev.podsalinan;

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
