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
	private boolean programExiting,
					podcastToDownload,  // This is used to prevent a loop. if there is nothing to download in the podcasts it need to get out of the loop
					fileToDownload; // This is used to prevent an endless loop also in the downloads list.
	private Object syncObject;
	private int currentPodcast;
	private int currentfile;
	private Vector<ProgSettings> progSettings;
	private int runningPoddownloaders,
				runningDownloaders;

	public class Download {
		public String url,
			   		  destination,
			   		  size;
		public int	  itemNum;
		public DownloadList list;
	}
	
	public DownloadQueue(boolean programExiting, TreePane treePane, Vector<ProgSettings> progSettings, Object syncObject){
		this.programExiting=programExiting;
		tree=treePane;
		this.syncObject=syncObject;
		this.progSettings=progSettings;
	}
	
	public void run(){
		downloaders = new Vector<Downloader>();
		runningPoddownloaders=0;
		runningDownloaders=0;
		
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

			int maxPodcastDownloaders=0;
			int maxDownloaders=0;
			for (int ps=0; ps < progSettings.size(); ps++){
				if (progSettings.get(ps).setting.equals("maxPodcastDownloaders"))
					maxPodcastDownloaders=Integer.parseInt(progSettings.get(ps).value);
				if (progSettings.get(ps).setting.equals("maxDownloaders"))
					maxDownloaders=Integer.parseInt(progSettings.get(ps).value);
			}
			
			fileToDownload=true;
			// Need to Create downloaders for download window
			while ((fileToDownload)&&(runningDownloaders<maxDownloaders)){
				// Check out download list, set it up to download.
				
			}
			
			podcastToDownload=true;
			// Downloaders size, determines how many files can be downloaded at a time.
			while ((podcastToDownload)&&(runningPoddownloaders<maxPodcastDownloaders)){
				boolean foundNew=false;
				currentPodcast=0;
				Download newDownload = new Download();
					   
				/* Need to set this to travel through the tree, to each download queue
				 *  and start maxDownloaders number of downloaders on files found in the queues
				 */
				while ((podcastToDownload)&&(!foundNew)){
					currentPodcast++;
					
					int numPodcasts=tree.getTree().getModel().getChildCount(tree.getRssFeeds());
					// If there are rssfeeds available
					if (numPodcasts>0){
						if (currentPodcast>=numPodcasts){
							podcastToDownload=false;
						}
						DefaultMutableTreeNode currentrssFeed = (DefaultMutableTreeNode)tree.getTree().getModel().getChild(tree.getRssFeeds(),currentPodcast);
						if (currentrssFeed!=null){
							Object nodeInfo = currentrssFeed.getUserObject();
							if (currentrssFeed.isLeaf() && (nodeInfo instanceof Podcast)){
								Podcast podcast = (Podcast) currentrssFeed.getUserObject();
								currentfile=0;
								Vector<Episode> downloadData = podcast.getDownloadData();
								while ((currentfile < downloadData.size())&&(!foundNew)){
									if (downloadData.get(currentfile).downloaded<2){
										// Tell the system it's in the process of being downloaded
										downloadData.get(currentfile).downloaded=2;
										
										// Fill the new download object with the data.
										newDownload.url=downloadData.get(currentfile).url;
										// Destination is the path + the filename
										newDownload.destination=podcast.getDirectory()+"/"+
																downloadData.get(currentfile).url.substring(
																		downloadData.get(currentfile).url.lastIndexOf('/')+1);
										// String version of the file size
										newDownload.size=downloadData.get(currentfile).size;
										// The podcast gui download list
										newDownload.list=podcast.getDownloadList();
										newDownload.itemNum=currentfile;
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
															  newDownload.list,
															  newDownload.itemNum,
															  true);
					downloaders.add(newDownloader);
					newDownloader.addListener(this);
					Thread downloadThread = new Thread(newDownloader);
					downloadThread.start();
					runningPoddownloaders++;
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
				if (downloaders.get(dc).isPodcast()){
					runningPoddownloaders--;
				} else {
					runningDownloaders--;
				}
					downloaders.remove(dc);
			}
			synchronized (syncObject){
				syncObject.notify();
			}
		}
	}
}
