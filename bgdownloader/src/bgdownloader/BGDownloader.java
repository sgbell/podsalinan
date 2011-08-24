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
import java.util.Timer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

@SuppressWarnings({ "serial", "unused" })
public class BGDownloader extends JFrame {
	
	private URLDownloadList downloads;
	private Vector<Podcast> podcasts;
	private Vector<ProgSettings> progSettings;
	
	private TreePane treePane;
	private JPanel cardPane;
	
	private DataStorage settings;
	
	private DownloadQueue podcastQueue; // This scans through the lists of files to download and starts a downloader with the next available file
	
	private boolean programExiting=false;
	private Object syncObject=new Object();

	/**
	 * Upon execution the program will create a new instance of bgdownloader, which is where
	 * most of the work happens. Creating the new instance builds the main window.
	 * 
	 * @param args
	 *   Nothing to pass in just yet
	 */
	public static void main(String[] args) {
		BGDownloader mainProgram = new BGDownloader();
	}

	public BGDownloader(){
		// Centralizing all of the podcast information, to make it easier to pass around the system
		podcasts = new Vector<Podcast>();
		// URL Downloads
		downloads = new URLDownloadList(syncObject);
		// Program Settings
		progSettings = new Vector<ProgSettings>();
		
		// Main Window Pane;
		JPanel pane = new JPanel();
		
		// Action Listener for the main window
		CommandPass aListener = new CommandPass(programExiting);
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
		/*
		try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            System.err.println("Couldn't use system look and feel.");
        }
		*/
		
		// disables sqlite4java's logging
		Logger.getLogger("com.almworks.sqlite4java").setLevel(Level.OFF);
		
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

		// In the process of Sorting out DownloadList and the singleDownloads vector
		treePane.setDownloads(downloads);
		cardPane.add(downloads.getDownloadList(),"Downloads");
		
		settings = new DataStorage(syncObject);
		settings.loadSettings(podcasts, downloads, progSettings, treePane, cardPane);
		
		JSplitPane splitpane = new JSplitPane (JSplitPane.HORIZONTAL_SPLIT,treePane,cardPane);
		splitpane.setDividerLocation(250);
		
		getContentPane().add(splitpane,BorderLayout.CENTER);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		showPodcasts();
		
		// Following 3 lines Set downloads window as current view 
		CardLayout cardLayout = (CardLayout)(cardPane.getLayout());
		cardLayout.show(cardPane, "Downloads");
		
		// show the window
		setVisible(true);

		/* This is the class that does all of the downloading. */
		podcastQueue = new DownloadQueue(programExiting,treePane, progSettings, syncObject);
		Thread downloadingQueue = new Thread(podcastQueue);
		downloadingQueue.start();
		
	}
	
	/** This is used to start the podcast Threads, which populate the system with
	 * the data from the podcasts. 
	 */
	public void showPodcasts(){
		for (int pcc=00; pcc < podcasts.size(); pcc++){
			Thread podcast = new Thread(podcasts.get(pcc));
			podcast.start();
		}
	}

	public void saveSettings(){
		settings.saveSettings(podcasts, downloads);
	}
	
	public URLDownloadList getDownloadList(){
		return downloads;
	}
	
	/** Adds a new podcast to the system, this is called from CommandPass when the 
	 * user adds the new podcast
	 * @param newFeed - Url to the new podcast
	 */
	public void addRssFeed(String newFeed){
		Podcast newPodcast = new Podcast(newFeed, settings, treePane, cardPane, syncObject);
		podcasts.add(newPodcast);
		Thread podcastRunner = new Thread(newPodcast);
		podcastRunner.start();
	}
	
	public TreePane getTreePane(){
		return treePane;
	}

	public void addDownload(String url) {
		downloads.addDownload(url);
	}

}
