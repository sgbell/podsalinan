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
/** This will eventually become a super class to Pocast and URLDownloadList
 * 
 */

package podsalinan;


/**
 * 
 * @author bugman
 *
 */
public class DownloadDetails {

	// Common information between Podcast and URLDownload classes
	private DownloadList downloadList;
	private String name,	// Podcast name, or just URL for url list
				   directory; // Directory that files will be downloaded to.
	
	// The object used for wake and notify
	public Object syncObject;

	public DownloadDetails(String name, Object syncObject){
		this.name = name;
		this.syncObject= syncObject;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	/**Used by JTree class mostly to get the name, which will be seen
	 * in the JTree pane. 
	 */
	public String toString(){
		return name;
	}

	public void setDownloadList(DownloadList downloadList) {
		this.downloadList = downloadList;
	}

	public DownloadList getDownloadList() {
		return downloadList;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getDirectory() {
		return directory;
	}
}
