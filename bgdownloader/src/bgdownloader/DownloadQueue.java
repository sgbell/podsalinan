/**
 * 
 */
package bgdownloader;

import java.net.URL;
import java.util.Vector;

/**
 * @author bugman
 *
 */
public class DownloadQueue implements Runnable{
	private Vector<Downloader> downloaders;	// Collection of running downloaders
	private TreePane tree;	// This will be used to track stuff that needs to be downloaded
	private boolean programExiting;
	private int maxDownloaders=1;	// Maximum Number of Downloaders;
	private Object syncObject=new Object();

	public DownloadQueue(boolean programExiting, TreePane treePane){
		this.programExiting=programExiting;
		tree=treePane;
	}
	
	public Object getSyncObject(){
		return syncObject;
	}
	
	public void run(){
		downloaders = new Vector<Downloader>();
		
		// While the program is running continue downloading.
		while (!programExiting){
			// Downloaders size, determines how many files can be downloaded at a time.
			if (downloaders.size()<maxDownloaders){
				boolean foundNew=false;
				/* Need to set this to travel through the tree, to each download queue
				 *  and start maxDownloaders number of downloaders on files found in the queues
				 */
				
			}
			// Pausing this until notify() is sent from one of the downloaders,
			// or when a new item is added to the queue.
			synchronized (syncObject){
				try {
					syncObject.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			/* Need to set the program up to delete dead downloaders again
			 * Look at bookmark for instructions on making Runnable throw an action
			 * when it finishes.
			 */
		}
	}
}
