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
	private Vector<Details> downloads;
	private String title;
	
	public URLDownloadList(){
		downloadlist = new DownloadList(false);
		downloads = new Vector<Details>();
		title = "URLS";
	}
	
	public DownloadList getDownloadList(){
		return downloadlist;
	}
	
	public Vector<Details> getDownloads(){
		return downloads;
	}
	
	public String toString(){
		return title;
	}

	public void addDownload(String url) {
		downloads.add(new Details(url));
		downloadlist.addDownload(url);
	}
}
