/**
 * 
 */
package bgdownloader;

/**
 * @author bugman
 *
 */
public class Details {

	public String url;
	public boolean added,	// Has this podcast been added to the database
	   			   remove=false; // Does this podcast need to be deleted from the system
	
	public Details(String url){
		this.url=url;
		added=false;
	}
	
	public Details(String url, boolean added){
		this.url=url;
		this.added=added;
	}
}
