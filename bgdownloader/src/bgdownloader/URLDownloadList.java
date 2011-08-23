/**
 * 
 */
package bgdownloader;

import java.util.Vector;

/**
 * @author bugman
 *
 */
public class URLDownloadList {

	private DownloadList downloadlist;
	private Vector<String> downloads;
	private String title;
	
	public URLDownloadList(){
		downloadlist = new DownloadList(false);
		downloads = new Vector<String>();
		title = "URLS";
	}
	
	public DownloadList getDownloadList(){
		return downloadlist;
	}
	
	public Vector<String> getDownloads(){
		return downloads;
	}
	
	public String toString(){
		return title;
	}

	public void addDownload(String url) {
		downloads.add(url);
		downloadlist.addDownload(url);
	}
}
