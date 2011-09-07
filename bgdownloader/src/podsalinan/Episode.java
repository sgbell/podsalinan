/**
 * 
 */
package podsalinan;

/**
 * @author bugman
 *
 */
public class Episode extends Details {
	public String date,
				  title,
				  description;

	public Episode(String published, String title, String url, String length, String desc) {
		super(url,length);
		date = published;
		this.title=title;
		description=desc;
	}
};