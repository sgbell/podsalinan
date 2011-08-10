/**
 *   bgdownloader is a project written to 1, get me familiar with java again. and 2, create a
 *   program that will download any url it is given, as well as podcasts from rss feeds that a user
 *   sources.
 *   
 *   Written By: Sam Bell
 *   
 */
package bgdownloader;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class BGDownloader extends JFrame {
	
	private URLDownloadList downloads;
	private TreePane treePane;
	private JPanel cardPane;

	/**
	 * Upon execution the program will create a new instance of bgdownloader, which is where
	 * most of the work happens. Creating the new instance builds the main window.
	 * 
	 * @param args
	 *   Nothing to pass in just yet
	 */
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		BGDownloader mainProgram = new BGDownloader();
	}

	public BGDownloader(){
		@SuppressWarnings("unused")
		JPanel pane = new JPanel();
		
		CommandPass aListener = new CommandPass();
		aListener.setParent(this);
		/* 
		 * creating a new MenuBar passing in the instance of bgdownloader, so bgdownloader can then handle all of
		 * the gui events, care of ActionListener. This is true, up until the time I decide it's too much, and need
		 * to create a whole class to handle the actions.  
		 */
		MenuBar menubar = new MenuBar(aListener);
		menubar.createMenu("src/bgdownloader/menu.xml");
		
		/**
		 * Following lines are not compatible with gentoo :(
		 */
		
		try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            System.err.println("Couldn't use system look and feel.");
        }
		
		/*
		 *  For the moment the default size iss 800x600. Eventually I will configure a ini file to store useful information
		 *  like windows size, position, and anything else I can think of that's needed.
		 */
		setSize (800,600);
		setTitle ("bgdownloader");
	
		setJMenuBar(menubar);
		treePane = new TreePane();
		aListener.setTree(treePane.getTree());
		
		cardPane = new JPanel(new CardLayout()); 
		treePane.cardView(cardPane);
		
		
		JSplitPane splitpane = new JSplitPane (JSplitPane.HORIZONTAL_SPLIT,treePane,cardPane);
		splitpane.setDividerLocation(250);
		
		getContentPane().add(splitpane,BorderLayout.CENTER);
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		// rss feed
		addRssFeed("http://revision3.com/hak5/feed/Xvid-Large");
		
		// url download list
		downloads = new URLDownloadList();
		treePane.setDownloads(downloads);
		cardPane.add(downloads.getDownloadList(),"Downloads");
	}

	public URLDownloadList getDownloadList(){
		return downloads;
	}
	
	public void addRssFeed(String newFeed){
		RssFeedDetails newRss = new RssFeedDetails("New Feed",
				newFeed,"/home/bugman/Videos",
				new DownloadList(true));
		newRss.start();
		treePane.addrssFeed(newRss);
		cardPane.add(newRss.getDownloadList(),newRss.getFeedName());
	}
}
