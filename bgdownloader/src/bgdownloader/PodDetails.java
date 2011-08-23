/**
 * 
 */
package bgdownloader;

/**
 * @author bugman
 *
 */
public class PodDetails extends Details {
	
	public String name,
				  datafile,
				  directory;
	public boolean changed;

	public PodDetails(String url) {
		super(url);
	}

	public PodDetails(String name, String datafile, String url, String directory){
		super(url);

		this.name = name;
		this.datafile=datafile;
		this.directory=directory;
	}
}
