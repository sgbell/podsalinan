/**
 * 
 */
package podsalinan;

import java.io.File;
import java.net.URL;

/**
 * @author bugman
 *
 */
public class URLDownload extends Details {
	
	private File destinationFile;
	private String destinationFolder;
	private String podcastId;
	
	public URLDownload(){
		super();
	}
	
	public URLDownload(String url) {
		super(url);
	}

	public URLDownload(String url, String length){
		super(url, length);
	}
	
	public URLDownload(String url, boolean added){
		super(url, added);
	}
	
	public URLDownload(String url, String length, String destination){
		super(url, length);
		setDestinationFolder(destination);
	}
	
	public URLDownload(String url, boolean added, String destination){
		super(url, added);
		setDestinationFolder(destination);
	}

	public URLDownload(String url, String length, boolean added, String destination){
		super(url, added);
		setSize(length);
		setDestinationFolder(destination);
	}
	
	public URLDownload(String url, String length, String destination, String podcast, 
			           int status){
		super(url, length);
		setDestinationFolder(destination);
		setStatus(status);
	}
	
	public URLDownload(URL url, boolean added){
		super(url,added);
	}
	
	/**
	 * @return the destination, including the filename
	 */
	public String getDestinationFolder() {
		return destinationFolder;
	}

	/**
	 * @param destination the destination to set
	 */
	public void setDestinationFolder(String newDestinationFolder) {
		destinationFolder = newDestinationFolder;
	}

	public void setDestinationFile(File outputFile) {
		destinationFile = outputFile;
	}
	
	public File getDestinationFile(){
		return destinationFile;
	}

	/**
	 * @return the podcastId
	 */
	public String getPodcastId() {
		return podcastId;
	}

	/**
	 * @param podcastId the podcastId to set
	 */
	public void setPodcastId(String podcastId) {
		this.podcastId = podcastId;
		this.setUpdated(true);
	}
}
