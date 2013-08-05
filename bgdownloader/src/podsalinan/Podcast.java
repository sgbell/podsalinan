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
/**
 *  This is the class used to store details about the rss feed
 */
package podsalinan;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * @author bugman
 *
 */
public class Podcast extends DownloadDetails{

	private String  url,
	   				directory,  // Directory that files will be downloaded to.
					image;
	private boolean changed,
					remove,
					added;
	
	private Vector<Episode> episodeList = new Vector<Episode>(); // Used to store the the downloads, seperate from the DownloadList
	
	/**This is used to create a new Podcast with only a url.
	 * 
	 * @param newURL
	 */
	public Podcast(String newURL){
		super(null);
		url = newURL;
	}
	
	/** Used to create a Podcast from the information in the systems database. 
	 * 
	 * @param newName
	 * @param newURL
	 * @param newDirectory
	 * @param newDatastore
	 */
	public Podcast(String newName, String newURL, String newDirectory, String newDatastore){
		this(newURL);
		
		setName(newName);
		setDirectory(newDirectory);
		setDatafile(newDatastore);
	}
	
	public Vector<Episode> getEpisodes(){
		return episodeList;
	}
	
	public void setURL(String url){
		this.url=url;
	}
	
	public String getURL(){
		return url;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getDirectory() {
		return directory;
	}
	
	/**
	 * @return the changed
	 */
	public boolean isChanged() {
		return changed;
	}

	/**
	 * @param changed the changed to set
	 */
	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	/**
	 * @return the remove
	 */
	public boolean isRemoved() {
		return remove;
	}

	/**
	 * @param remove the remove to set
	 */
	public void setRemove(boolean remove) {
		this.remove = remove;
	}

	/**
	 * @return the added
	 */
	public boolean isAdded() {
		return added;
	}

	/**
	 * @param added the added to set
	 */
	public void setAdded(boolean added) {
		this.added = added;
	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}
}
