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
	public boolean added;		// This has been added to the database.
	public int	   downloaded;  // This is used to track if the system has already downloaded the file.
								// 0 - not started
								// 1 - was previously started, but not yet handled by a downloader
								// 2 - currently downloading
								// 3 - finished
	public final int NOT_STARTED=0,
					 PREVIOUSLY_STARTED=1,
					 CURRENTLY_DOWNLOADING=2,
					 FINISHED=3;

	public Episode(String published, String title, String url, String length, String desc) {
		date = published;
		this.title=title;
		this.url=url;
		size=length;
		description=desc;
	}
};