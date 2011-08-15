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
public class DownloadQueue extends Thread{
	private Vector<Download> queue;
	private Vector<Downloader> downloaders;
	private boolean programExiting;
	private int maxDownloaders=1;	// Maximum Number of Downloaders;
	private Object syncObject=new Object();

	private class Download {
		public URL file;			// The file to be downloaded.
		public String destination;  // Where its being downloaded to.
		public int state;			// State of download in list
									// 0 - Not started
									// 1 - Started
		public DownloadList downloadTable;
	}
	
	public DownloadQueue(boolean programExiting){
		queue = new Vector<Download>();
		this.programExiting=programExiting;
	}
	
	public void addDownload(URL file, String whereTo, DownloadList downloads){
		Download newDownload = new Download();
		newDownload.file = file;
		newDownload.destination = whereTo;
		newDownload.state=0;
		newDownload.downloadTable=downloads;
		
		queue.add(newDownload);
		
		// If the queue is asleep wake it up when a new item is added.
		synchronized (syncObject){
			syncObject.notify();
		}

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
				int qc=0;
				// If there are files to download
				if (queue.size()>0){
					while ((!foundNew)&&(qc<queue.size())){
						// qc - queue count. Travel through the queue to file an item not currently being downloaded
						if (queue.get(qc).state!=0)
							qc++;
						else
							foundNew=true;
					}
					// If item not being downloaded is found, create a downloader and start it.
					if ((foundNew)&&(queue.get(qc).state==0)){
						Downloader newDownloader = new Downloader(queue.get(qc).file,
																  queue.get(qc).destination,
																  syncObject,
																  queue.get(qc).downloadTable);
						downloaders.add(newDownloader);
						newDownloader.start();
						// Set the state to being downloaded.
						queue.get(qc).state=1;
					}
				}
			}
			// Pausing this until notify() is sent from one of the downloaders,
			// or when a new item is added to the queue.
			synchronized (syncObject){
				try {
					syncObject.wait();
					// For the moment using an extra wait, timed so if a downloader has
					// just finished and woken this up, it will have enough time to end
					// for us to pick it up.
					syncObject.wait(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			for (int dc=0; dc<downloaders.size(); dc++){
				if (!downloaders.get(dc).isAlive())
					downloaders.remove(dc);
			}
		}
	}
}
