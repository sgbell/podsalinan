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
	}
	
	public void run(){
		downloaders = new Vector<Downloader>();
		
		// While the program is running continue downloading.
		while (!programExiting){
			// Downloaders size, determines how many files can be downloaded at a time.
			if (downloaders.size()<1){
				boolean foundNew=false;
				int qc=0;
				// If there are files to download
				if (queue.size()>0){
					while (!foundNew){
						// qc - queue count. Travel through the queue to file an item not currently being downloaded
						if (queue.get(qc).state!=0)
							qc++;
						else
							foundNew=true;
					}
					// If item not being downloaded is found, create a downloader and start it.
					if (queue.get(qc).state==0){
						Downloader newDownloader = new Downloader(queue.get(qc).file,queue.get(qc).destination);
						downloaders.add(newDownloader);
						newDownloader.start();
						// Set the state to being downloaded.
						queue.get(qc).state=1;
					}
				}
			}
			// Need to update the gui, on the status of the download, and thought here is the best place to do it from.
			for (int dlc=0; dlc<downloaders.size(); dlc++){
				int qc=0;
				boolean itemFound=false;
				while ((qc<queue.size())&&(!itemFound)){
					if (downloaders.get(dlc).getFilenameDownload()==queue.get(qc).file.toString())
						itemFound=true;
				}
				System.out.println("Percentage in DownloadQueue: "+downloaders.get(dlc).downloadCompleted());
				queue.get(qc).downloadTable.downloadProgress(queue.get(qc).file.toString(),
															 downloaders.get(dlc).downloadCompleted());
				if (downloaders.get(dlc).downloadCompleted()==100)
					downloaders.remove(dlc);
			}
		}
	}
}
