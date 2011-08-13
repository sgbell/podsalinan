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
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

@SuppressWarnings("serial")
public class BGDownloader extends JFrame {
	
	private URLDownloadList downloads;
	private TreePane treePane;
	private JPanel cardPane;
	private SQLiteConnection rssListdb;
	private DataStorage settings;

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
		boolean rssExists;
		
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
		
		// cardPane is used for the individual views of each downloding screen, otherwise
		// we can't swap between download queues
		cardPane = new JPanel(new CardLayout()); 
		treePane.cardView(cardPane);

		settings = new DataStorage();
		settings.openSettings();
		
		JSplitPane splitpane = new JSplitPane (JSplitPane.HORIZONTAL_SPLIT,treePane,cardPane);
		splitpane.setDividerLocation(250);
		
		getContentPane().add(splitpane,BorderLayout.CENTER);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		// Currently in the process of moving stuff into a settings class.
		
		try {
			// Do a search in the podcasts table for podcasts stored in the system
			SQLiteStatement sql = rssListdb.prepare("SELECT * from podcasts;");
			while (sql.step()){
				if (sql.hasRow()){
					// With the podcasts found, import the information into the system
					// and load the podcast data from the database.
					RssFeedDetails podcast = new RssFeedDetails(sql.columnString(1),
																sql.columnString(2),
																sql.columnString(3),
																sql.columnString(4));
					podcast.start();
					treePane.addrssFeed(podcast);
					cardPane.add(podcast.getDownloadList(),podcast.getFeedName());
				} else {
					
				}
			}
			sql.dispose();
		} catch (SQLiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// rss feed
		//addRssFeed("http://revision3.com/hak5/feed/Xvid-Large");
		
		// url download list
		downloads = new URLDownloadList();
		treePane.setDownloads(downloads);
		cardPane.add(downloads.getDownloadList(),"Downloads");
	}

	public URLDownloadList getDownloadList(){
		return downloads;
	}
	
	public void addRssFeed(String newFeed){
	
		RssFeedDetails newPodcast = new RssFeedDetails(newFeed);
		
		newPodcast.start();
		// Loop installed to wait till downloading of the podcast is done.
		// Might shift this off to a thread at some stage
		while(!newPodcast.isFinished()){
		}
		// The following SQL statement adds the newly created podcast into the main database.
		try {
			SQLiteStatement sql = rssListdb.prepare("INSERT INTO podcasts(name,localfile,url,directory) VALUES (" +
													"'"+newPodcast.getFeedName()+"'," +
													"'"+newPodcast.getdb()+"'," +
													"'"+newFeed+"'," +
													"'"+newPodcast.getLocalStore()+"');");
			sql.stepThrough();
			sql.dispose();
		} catch (SQLiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		treePane.addrssFeed(newPodcast);
		cardPane.add(newPodcast.getDownloadList(),newPodcast.getFeedName());
	}
}
