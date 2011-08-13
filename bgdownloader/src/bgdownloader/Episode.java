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
	public boolean added;

	public Episode(String published, String title, String url, String length, String desc) {
		date = published;
		this.title=title;
		this.url=url;
		size=length;
		description=desc;
	}

};