/**
 * 
 */
package bgdownloader;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author bugman
 *
 */
public class DownloadQueue implements Runnable, RunnableCompleteListener{
	private Vector<Downloader> downloaders;	// Collection of running downloaders
	private TreePane tree;	// This will be used to track stuff that needs to be downloaded
	private boolean programExiting;
	private int maxDownloaders=3;	// Maximum Number of Downloaders;
	private Object syncObject;
	private int currentPodcast;
	private int currentfile;

	public class Download {
		public String url,
			   		  destination,
			   		  size;
		public DownloadList list;
	}
	
	public DownloadQueue(boolean programExiting, TreePane treePane, Object syncObject){
		this.programExiting=programExiting;
		tree=treePane;
		this.syncObject=syncObject;
	}
	
	public void run(){
		downloaders = new Vector<Downloader>();
		
		// While the program is running continue downloading.
		while (!programExiting){
			// Pausing this until notify() is sent from one of the downloaders,
			// or when a new item is added to the queue.
			synchronized (syncObject){
				try {
					syncObject.wait();
					//System.out.println("Wake up");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Downloaders size, determines how many files can be downloaded at a time.
			while (downloaders.size()<maxDownloaders){
				boolean foundNew=false;
				currentPodcast=0;
				Download newDownload = new Download();
					   
				/* Need to set this to travel through the tree, to each download queue
				 *  and start maxDownloaders number of downloaders on files found in the queues
				 */
				while (!foundNew){
					currentPodcast++;
					
					int numPodcasts=tree.getTree().getModel().getChildCount(tree.getRssFeeds());
					// If there are rssfeeds available
					if (numPodcasts>0){
						if (currentPodcast>=numPodcasts){
							currentPodcast=0;
						}
						DefaultMutableTreeNode currentrssFeed = (DefaultMutableTreeNode)tree.getTree().getModel().getChild(tree.getRssFeeds(),currentPodcast);
						if (currentrssFeed!=null){
							Object nodeInfo = currentrssFeed.getUserObject();
							if (currentrssFeed.isLeaf() && (nodeInfo instanceof RssFeedDetails)){
								RssFeedDetails podcast = (RssFeedDetails) currentrssFeed.getUserObject();
								currentfile=0;
								Vector<Episode> downloadData = podcast.getDownloadData();
								while ((currentfile < downloadData.size())&&(!foundNew)){
									//System.out.println("DQ - Download: "+downloadData.get(currentfile).url);
									//System.out.println("DQ - Status: "+downloadData.get(currentfile).downloaded);
									if (downloadData.get(currentfile).downloaded<2){
										//System.out.println("DQ2 - Download: "+downloadData.get(currentfile).url);
										//System.out.println("DQ2 - Status: "+downloadData.get(currentfile).downloaded);
										// Tell the system it's in the process of being downloaded
										downloadData.get(currentfile).downloaded=2;
										
										// Fill the new download object with the data.
										newDownload.url=downloadData.get(currentfile).url;
										// Destination is the path + the filename
										newDownload.destination=podcast.getLocalStore()+"/"+
																downloadData.get(currentfile).url.substring(
																		downloadData.get(currentfile).url.lastIndexOf('/')+1);
										// String version of the file size
										newDownload.size=downloadData.get(currentfile).size;
										// The podcast gui download list
										newDownload.list=podcast.getDownloadList();
										foundNew=true;
									} else {
										currentfile++;
									}
								}
							}
						}
					}	
				}
				Downloader newDownloader;
				try {
					newDownloader = new Downloader(new URL(newDownload.url),
															  newDownload.destination,
															  newDownload.size,
															  syncObject,
															  newDownload.list);
					downloaders.add(newDownloader);
					newDownloader.addListener(this);
					Thread downloadThread = new Thread(newDownloader);
					//downloadThread.start();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			}
			/* Need to set the program up to delete dead downloaders again
			 * Look at bookmark for instructions on making Runnable throw an action
			 * when it finishes.
			 */
			
		}
	}

	@Override
	public void notifyOfThreadComplete(Runnable runnable) {
		for (int dc=0; dc < downloaders.size(); dc++){
			if (downloaders.get(dc).downloadCompleted()==100){
				downloaders.remove(dc);
			}
			synchronized (syncObject){
				syncObject.notify();
			}
		}
	}
}
