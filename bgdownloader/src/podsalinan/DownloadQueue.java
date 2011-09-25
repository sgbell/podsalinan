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
package podsalinan;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
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
	
	private DefaultTableModel queueGui;
	private JTable table;

	public class Download {
		public String url,
			   		  destination,
			   		  size;
		public int	  itemNum;
		public DownloadList list;
	}
	
	@SuppressWarnings("serial")
	public DownloadQueue(boolean programExiting, TreePane treePane, Vector<ProgSettings> progSettings, DownloadList downloadView, Object syncObject){
		this.programExiting=programExiting;
		tree=treePane;
		this.syncObject=syncObject;
		this.progSettings=progSettings;
		
		String headers[] = {"Filename","Progress","Speed"};
		queueGui = new DefaultTableModel(headers,0);
		table = new JTable(queueGui){
			public boolean isCellEditable(int rowIndex, int vColIndex){
				return false;
			}
		};
		table.setShowGrid(false);
		table.setRowSelectionAllowed(true);
		
	}
	
	public void run(){
		downloaders = new Vector<Downloader>();
		runningPoddownloaders=0;
		runningDownloaders=0;
		
		// While the program is running continue downloading.
		while (!programExiting){
			int maxPodcastDownloaders=0;
			int maxDownloaders=0;
			for (int ps=0; ps < progSettings.size(); ps++){
				if (progSettings.get(ps).setting.equals("maxPodcastDownloaders"))
					maxPodcastDownloaders=Integer.parseInt(progSettings.get(ps).value);
				if (progSettings.get(ps).setting.equals("maxDownloaders"))
					maxDownloaders=Integer.parseInt(progSettings.get(ps).value);
			}
			if (progSettings.size()<1){
				progSettings.add(new ProgSettings("maxPodcastDownloaders","3"));
				maxPodcastDownloaders=3;
				progSettings.add(new ProgSettings("maxDownloaders","3"));
				maxDownloaders=3;
			}
			//System.out.println ("DownloadQueue: num downloaders set");
			
			fileToDownload=true;
			// Need to Create downloaders for download window
			while ((fileToDownload)&&(runningDownloaders<maxDownloaders)){
				// Check out download list, set it up to download.
				Vector<Details> urlDownloads=tree.getDownloads().getDownloads();
				//System.out.println("URL Downloads size: "+urlDownloads.size());
				int udc=0;
				if (urlDownloads.size()>0){
					while (udc<urlDownloads.size()){
						//System.out.println("Jumping through URL Downloads: "+udc);
						if (urlDownloads.get(udc).downloaded<2){
							urlDownloads.get(udc).downloaded=2;
							try {
								Downloader newDownloader= new Downloader(new URL(urlDownloads.get(udc).url),
																		 tree.getDownloads().getDirectory()+"/"+
																				 urlDownloads.get(udc).url.substring(
																						 urlDownloads.get(udc).url.lastIndexOf('/')+1),
																		 syncObject,
																		 tree.getDownloads().getDownloadList(),
																		 queueGui);
								startDownload(newDownloader);
								runningDownloaders++;
							} catch (MalformedURLException e) {
								e.printStackTrace();
							}
						} else {
							fileToDownload=false;
						}
						udc++;
					}
				} else {
					fileToDownload=false;
				}
			}
			//System.out.println("Exited URL downloads");
			podcastToDownload=true;
			// Downloaders size, determines how many files can be downloaded at a time.
			while ((podcastToDownload)&&(runningPoddownloaders<maxPodcastDownloaders)){
				boolean foundNew=false;
				currentPodcast=-1;
				Download newDownload = new Download();
					   
				/* Need to set this to travel through the tree, to each download queue
				 *  and start maxDownloaders number of downloaders on files found in the queues
				 */
				//System.out.println("There is a podcast to download");
				while ((podcastToDownload)&&(!foundNew)){
					currentPodcast++;
					//System.out.println ("New Podcast not found yet");
					
					int numPodcasts=tree.getTree().getModel().getChildCount(tree.getRssFeeds());
					//System.out.println("Number of podcasts in the system: "+numPodcasts);
					// If there are rssfeeds available
					if (numPodcasts>0){
						if (currentPodcast>=numPodcasts){
							podcastToDownload=false;
							currentPodcast=0;
						}
						DefaultMutableTreeNode currentrssFeed = (DefaultMutableTreeNode)tree.getTree().getModel().getChild(tree.getRssFeeds(),currentPodcast);
						if (currentrssFeed!=null){
							Object nodeInfo = currentrssFeed.getUserObject();
							if (currentrssFeed.isLeaf() && (nodeInfo instanceof Podcast)){
								Podcast podcast = (Podcast) currentrssFeed.getUserObject();
								//System.out.println("Current Podcast: "+podcast.getName());
								currentfile=0;
								Vector<Episode> downloadData = podcast.getDownloadData();
								while ((currentfile < downloadData.size())&&(!foundNew)){
									//System.out.println("DownloadQueue - File: "+downloadData.get(currentfile).url);
									//System.out.println("DownloadQueue - Status: "+downloadData.get(currentfile).downloaded);
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

										//System.out.println("DownloadQueue: Creating new Downloader");
										Downloader newDownloader;
										try {
											newDownloader = new Downloader(new URL(newDownload.url),
																					  newDownload.destination,
																					  newDownload.size,
																					  syncObject,
																					  newDownload.list,
																					  queueGui,
																					  newDownload.itemNum,
																					  true);
											boolean found=false;
											for (int dqc=0; dqc < downloaders.size(); dqc++){
												if (downloaders.get(dqc).getFilenameDownload().equals(newDownloader.getFilenameDownload()))
													found=true;
											}
											if (!found){
												startDownload(newDownloader);
												runningPoddownloaders++;
												//System.out.println("DownloadQueue: Running Podcast Downloaders - "+runningPoddownloaders);
												//System.out.println("DownloadQueue: Running URL Downloaders - "+runningDownloaders);
											}
										} catch (MalformedURLException e) {
											e.printStackTrace();
										}

									} else {
										currentfile++;
									}
								}
								//System.out.println("DownloadQueue: Finished Scanning for new download");
							}
						}
					} else {
						podcastToDownload=false;
					}
				}
				//System.out.println("No podcasts to download");
			}
			// Pausing this until notify() is sent from one of the downloaders,
			// or when a new item is added to the queue.
			//System.out.println("DownloadQueue: Going to Sleep");
			synchronized (syncObject){
				try {
					syncObject.wait();
					//System.out.println("DownloadQueue: Woken up");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Moved these lines to it's own method, as podcasts and url downloads are essentially doing the same
	 * @param newDownloader
	 */
	public void startDownload(Downloader newDownloader){
		downloaders.add(newDownloader);
		newDownloader.addListener(this);
		Thread downloadThread = new Thread(newDownloader,"Downloader");
		downloadThread.start();
		//System.out.println("Download Started: "+newDownloader.getFilenameDownload());		
	}
	
	@Override
	public void notifyOfThreadComplete(Runnable runnable) {
		for (int dc=0; dc < downloaders.size(); dc++){
			if (downloaders.get(dc).downloadCompleted()==100){
				//System.out.println("Finished: "+downloaders.get(dc).getFilenameDownload());
				queueGui.removeRow(downloaders.get(dc).getRow());
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

	public JTable getdownloadQueue() {
		return table;
	}
	
	
}
