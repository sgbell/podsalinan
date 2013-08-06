/**
 * 
 */
package podsalinan;

/**
 * @author bugman
 *
 */
public class URLDownload extends Details {
	
	private String destination;
	
	public URLDownload(){
		super(null);
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
		this.destination = destination;
	}
	
	public URLDownload(String url, boolean added, String destination){
		super(url, added);
		this.destination=destination;
	}

	public URLDownload(String url, String length, boolean added, String destination){
		super(url, added);
		setSize(length);
		this.destination = destination;
	}
	/**
	 * @return the destination
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * @param destination the destination to set
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}
}
