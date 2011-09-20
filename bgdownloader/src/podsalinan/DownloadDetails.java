/*******************************************************************************
 * Copyright (c) 2011 Sam Bell.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or  any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See
 * the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
