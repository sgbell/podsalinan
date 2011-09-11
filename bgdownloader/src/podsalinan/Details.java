/*******************************************************************************
 * Copyright (c) 2011 Sam Bell.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Sam Bell - initial API and implementation
 ******************************************************************************/
/**
 * 
 */
package podsalinan;

/**
 * @author bugman
 *
 */
public class Details {

	public String url,
				  size;
	public boolean added,	// Has this podcast been added to the database
	   			   remove=false; // Does this podcast need to be deleted from the system
	public int	   downloaded;  // This is used to track if the system has already downloaded the file.
								// 0 - not started
								// 1 - was previously started, but not yet handled by a downloader
								// 2 - currently downloading
								// 3 - finished
	
	public final int NOT_STARTED=0,
			 		 PREVIOUSLY_STARTED=1,
			 		 CURRENTLY_DOWNLOADING=2,
			 		 FINISHED=3;
	
	public Details (String url){
		this.url=url;
		added=false;
		size="0";
	}
	
	public Details(String url, String length){
		this(url);
		this.size=length;
	}
	
	public Details(String url, boolean added){
		this.url=url;
		this.added=added;
	}
	
	public String getURL(){
		return url;
	}
	
	public void setURL(String url){
		this.url=url;
	}
	
	public String getSize(){
		return size;
	}
	
	public void setSize(String size){
		this.size=size;
		//System.out.println("Details: "+this.size);
	}
}
