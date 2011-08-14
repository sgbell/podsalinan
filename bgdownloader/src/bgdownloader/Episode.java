/**
 * 
 */
package bgdownloader;

/**
 * @author bugman
 *
 */
public class Episode {
	public String date,
				  title,
				  url,
				  description,
				  size;
	public boolean added,		// This has been added to the database.
				   downloaded;  // This is used to track if the system has already downloaded the file.

	public Episode(String published, String title, String url, String length, String desc) {
		date = published;
		this.title=title;
		this.url=url;
		size=length;
		description=desc;
	}

};