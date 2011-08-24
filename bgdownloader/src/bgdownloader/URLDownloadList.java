/**
 * 
 */
package bgdownloader;

import java.util.Vector;

/**
 * @author bugman
 *
 */
public class URLDownloadList extends DownloadDetails {

	private Vector<Details> downloads;
	
	public URLDownloadList(Object syncObject){
		super("URLS", syncObject);
		setDownloadList(new DownloadList(false));
		downloads = new Vector<Details>();
	}
	
	public Vector<Details> getDownloads(){
		return downloads;
	}
	
	public void addDownload(String url) {
		downloads.add(new Details(url));
		getDownloadList().addDownload(url);
	}
}
