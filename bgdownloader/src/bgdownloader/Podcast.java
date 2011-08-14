/**
 * 
 */
package bgdownloader;

/**
 * @author bugman
 *
 */
public class Podcast {

	public String name,
				  datafile,
				  url,
				  directory;
	public boolean added,	// Has this podcast been added to the database
	   			   remove=false; // Does this podcast need to be deleted from the system
	
	public Podcast(String name, String datafile, String url, String directory){
		this.name = name;
		this.datafile=datafile;
		this.url=url;
		this.directory=directory;
	}
}
