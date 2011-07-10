/**
 * 
 */
package bgdownloader;

/**
 * @author bugman
 *
 */
public class URLDownloadList {

	private DownloadList downloads;
	private String title;
	
	public URLDownloadList(){
		downloads = new DownloadList();
		title = "URLS";
		
	}
	
	public URLDownloadList(DownloadList downloadList){
		downloads = downloadList;
	}
	
	public void setDownloads(DownloadList downloadList){
		this.downloads = downloadList;
	}
	
	public DownloadList getDownloadList(){
		return downloads;
	}
	
	public String toString(){
		return title;
	}
}
