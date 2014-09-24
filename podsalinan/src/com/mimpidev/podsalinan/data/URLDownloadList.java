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
 * 
 */
package com.mimpidev.podsalinan.data;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import com.mimpidev.dev.debug.Log;
import com.mimpidev.dev.sql.SqlException;
import com.mimpidev.dev.sql.TableView;
import com.mimpidev.dev.sql.data.definition.SqlDefinition;
import com.mimpidev.podsalinan.Podsalinan;

/**
 * @author bugman
 *
 */
public class URLDownloadList extends DownloadDetails {

	private Vector<URLDownload> downloads;
	private Vector<Podcast> podcasts;
	
	public URLDownloadList(){
		super("Downloads");
		downloads = new Vector<URLDownload>();
		tableName = "downloads";
		
		String[] columnNames = {"id","url","size","destination","priority","podcastSource","status"};
		String[] columnTypes = {"INTEGER PRIMARY KEY AUTOINCREMENT",
				                "TEXT","TEXT","TEXT","INTEGER","TEXT","INTEGER"};
		createColumnList(columnNames,columnTypes);
	}
	
	public URLDownloadList(Vector<Podcast> podcastList){
		this();
		podcasts = podcastList;
	}
	
	public URLDownloadList(PodcastList podcastList) {
		this();
		podcasts = podcastList.getList();
	}

	public Vector<URLDownload> getDownloads(){
		return downloads;
	}
	
	public void addDownload(String url, String destination) {
		URLDownload newFile= new URLDownload(url);
		newFile.setDestination(destination);
		if (newFile.getURL()!=null){
			downloads.add(newFile);
			checkDownloadSize(newFile);
		}
	}
	
	public void addDownload(String url, String destination, String size, boolean added) {
		URLDownload newFile= new URLDownload(url, added);
		if (newFile.getURL()!=null){
			newFile.setDestination(destination);
			if (size!="-1")
				newFile.setSize(size);
			downloads.add(newFile);
			checkDownloadSize(newFile);
		}
	}
	
	public void addDownload(URLDownload newDownload, int priority) {
		if (findDownload(newDownload.getURL())==-1){
			// If nothing in the download list, or if priority is equal to the size
			if ((downloads.size()==0)||
				(priority>=downloads.size()))
				downloads.add(newDownload);
			// if the priority is less then the download size add it to the queue at the position of priority
			else if (priority<downloads.size()){
				downloads.insertElementAt(newDownload, priority);
			}
		} else {
			System.out.println ("Download already queued in the system");
		}
	}
	
	public void addDownload(Episode episode, Podcast podcast){
		//System.out.println("Debug: URLDownloadList.addDownload(Episode,Podcast)");
		int position = findDownload(episode.getURL());
		if (position<0){
			URLDownload newFile = new URLDownload(episode.getURL(),false);
			newFile.setDestination(podcast.getDirectory());
			newFile.setPodcastId(podcast.getDatafile());
			newFile.setStatus(Details.DOWNLOAD_QUEUED);
			//System.out.println("Debug: URLDownloadList.addDownload(Episode, Podcast) - podcastID="+podcast.getDatafile());
			if (episode.getSize()!="-1")
				newFile.setSize(episode.getSize());
			downloads.add(newFile);
			//System.out.println("Debug: download List size="+downloads.size());
			checkDownloadSize(newFile);
		} else {
			URLDownload download = getDownloads().get(position);
			if ((download!=null)&&
				(download.getPodcastId()==null)){
				episode.setStatus(Details.DOWNLOAD_QUEUED);
				download.setPodcastId(podcast.getDatafile());
			}
		}
	}
	/**
	 * This will move the selected download up the queue
	 * @param downloadId
	 */
	public boolean increasePriority(int downloadId){
		if ((downloadId>0)&&
		    (!downloads.get(downloadId).isRemoved())){
			Collections.swap(downloads, downloadId, downloadId-1);
			downloads.get(downloadId).setUpdated(true);
			downloads.get(downloadId-1).setUpdated(true);
			return true;
		}
		return false;
	}
	
	/**
	 * This will move the selected download down the queue
	 * @param downloadId
	 */
	public boolean decreasePriority(int downloadId){
		if ((downloadId<downloads.size()-1)&&
			(!downloads.get(downloadId+1).isRemoved())){
			Collections.swap(downloads, downloadId, downloadId+1);
			downloads.get(downloadId).setUpdated(true);
			downloads.get(downloadId+1).setUpdated(true);
			return true;
		}
		return false;
	}

	public boolean decreasePriority(URLDownload download) {
		int downloadId = findDownload(download);
		
		return decreasePriority(downloadId);
	}

	public void checkDownloadSize(URLDownload newFile){
		if (Long.parseLong(newFile.getSize())==0){
			try {
				URLConnection stream = newFile.getURL().openConnection();
				int fileSize=stream.getContentLength();
				newFile.setSize(Long.toString(fileSize));
				newFile.setStatus(Details.DOWNLOAD_QUEUED);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String filename=newFile.getDestination();
		File localFile = new File(filename);
		if (localFile.exists())
			if (localFile.length() < Long.parseLong(newFile.getSize())){
				newFile.setStatus(Details.INCOMPLETE_DOWNLOAD);
			} else if (localFile.length() >= Long.parseLong(newFile.getSize())){
				newFile.setStatus(Details.FINISHED);					
			}
	}

	/** This is a wrapper for cancelDownload(URLDownload)
	 * @param download
	 */
	public void cancelDownload(int download) {
		//System.out.println("URLDownloadList.cancelDownload() - downloads.size()="+downloads.size());
		if ((download >=0)&&(download<downloads.size())){
			// if download is an episode from a podcast we need to set the status on the episode and remove it from the list
			//System.out.println("Debug: URLDownloadList.cancelDownload(int) - podcastID: "+downloads.get(download).getPodcastId());
			URLDownload selectedDownload= downloads.get(download);
			cancelDownload(selectedDownload);
		}
	}
	
	/** This is a wrapper for cancelDownload(int)
	 * @param url
	 */
	public void cancelDownload (URL url){
		cancelDownload(findDownload(url));
	}

	/** This is the parent method for all cancelDownload methods
	 * @param download
	 */
	public void cancelDownload (URLDownload download){
		if (download.getPodcastId()!=""){
			for (Podcast currentPodcast : podcasts)
				if (currentPodcast.getDatafile().equalsIgnoreCase(download.getPodcastId())){
					download.setRemoved(true);
					Episode selectedEpisode = currentPodcast.getEpisodeByURL(download.getURL().toString());
					if (selectedEpisode!=null)
						selectedEpisode.setStatus(Details.DOWNLOAD_CANCELLED);
				}
		}
		download.setStatus(Details.DOWNLOAD_CANCELLED);
	}
	
	public boolean deleteDownload(int download) {
		if ((download >=0)&&(download<downloads.size())){
			return deleteDownload(downloads.get(download));
		}
		return false;
	}

	public boolean deleteDownload(URL url) {
		return deleteDownload(findDownload(url));
	}
	
	public boolean deleteDownload(URLDownload download){
		download.setRemoved(true);
		downloads.remove(download);
		downloads.add(download);
		return true;
	}
	
	public boolean restartDownload(int download) {
		if ((download >=0)&&(download<downloads.size())){
			return restartDownload(downloads.get(download));
		}
		return false;
	}
	
	public boolean restartDownload(URL url){
		return restartDownload(findDownload(url));
	}
	
	public boolean restartDownload(URLDownload download){
		if (deleteFile(download)){
			download.setStatus(Details.DOWNLOAD_QUEUED);
			return true;
		}
		return false;
	}
	
	public boolean deleteFile(URLDownload download){
		File localFile = download.getDestinationFile();

		if ((localFile.list()==null)&&
			(localFile.exists())){
			// delete the file
			localFile.delete();
			return true;
		} else if (localFile.list()!=null)
			// If it's a directory, don't delete it, but say you have
			return true;
		else
			return false;
	}

	public int size() {
		return downloads.size();
	}
	
	public int visibleSize() {
		int count=0;
		
		for (URLDownload download : downloads){
			if (!download.isRemoved())
				count++;
		}
		
		return count;
	}

	public int findDownload(URL url) {
		//System.out.println("Debug: findDownload(URL)"+url.toString());
		int count=0;
		URL lastURL=null;
		
		for (URLDownload download : downloads){
			//System.out.println("Debug: findDownload(URL) download="+download.getURL().toString());
			if ((lastURL!=null)&&(download.getURL().toString().equalsIgnoreCase(lastURL.toString()))){
				download.setRemoved(true);
			} else {
				lastURL = download.getURL();
			}
		}

		for (URLDownload download : downloads){
			if (download.getURL().toString().equalsIgnoreCase(url.toString())){
			/*	System.out.println("Debug: URLDownloadList.findDownload(URL) - download(");
				System.out.println("Debug:     Destination="+download.getDestination());
				System.out.println("Debug:     PodcastId="+download.getPodcastId());
				System.out.println("Debug:     Size="+download.getSize());
				System.out.println("Debug:     URL="+download.getURL()+")");*/
				return count;
			}
			count++;
		}
		return -1;
	}

	private int findDownload(URLDownload download) {
		int count=0;
		boolean found=false;
		
		while ((count<downloads.size())){
			if (downloads.get(count).equals(download))
				found=true;
			count++;
		}
		return count;
	}
	
	public int getNumberOfQueuedDownloads(){
		int numberofQueuedItems=0;
		
		synchronized (downloads){
			for (URLDownload download : downloads)
				if (download.getStatus()==Details.DOWNLOAD_QUEUED)
					numberofQueuedItems++;
		}
		
		return numberofQueuedItems;
	}
	
	/** getHighestQueuedItem will go through the array and find the highest queued item in the
	 *  list
	 * @return URLDownload
	 */
	public URLDownload getHighestQueuedItem(){
		for (URLDownload download : downloads)
			if (download.getStatus()==Details.DOWNLOAD_QUEUED)
				return download;
		
		// if item is not found
		return null;
	}

	public void reQueueDownload(URL url) {
		reQueueDownload(downloads.get(findDownload(url)));
	}

	public void reQueueDownload(URLDownload download) {
		if (download!=null)
			download.setStatus(Details.DOWNLOAD_QUEUED);
	}
	
	public void setPodcasts(Vector<Podcast> newPodcasts){
		podcasts=newPodcasts;
	}
	
	public Vector<Podcast> getPodcastArray(){
		return podcasts;
	}

	/**
	 * 
	 */
	public void readTable() {
		ArrayList<Map<String,String>> recordSet = readFromTable();
		
		if ((recordSet!=null)&&(recordSet.size()>0))
		for (Map<String,String> record: recordSet){
			// Create a new URLDownload and add to the downloads Vector
			URLDownload newDownload = new URLDownload(record.get("url"),
													  record.get("size"),
													  record.get("destination"),
													  record.get("podcastSource"),
													  Integer.parseInt(record.get("status")));
			newDownload.setAdded(true);
			addDownload(newDownload, Integer.parseInt(record.get("priority")));
		}
	}

	/**
	 * 
	 */
	public void updateDatabase(){
		
		if (dbTable.isDbOpen()){
			// Need to figure out how to increment downloadCount
			final Integer downloadCount= new Integer(0);
			for (final URLDownload download : downloads){
				int sqlType=TableView.NOTHING_CHANGED;
				if (!download.isAdded()){
					// Used to set the correct flag
					try {
						dbTable.insert(new HashMap<String,Object>(){{
							put("url",download.getURL().toString());
							put("size",Long.parseLong(download.getSize()));
							put("destination",download.getDestination());
							put("podcastSource",download.getPodcastId());
							put("status",download.getStatus());
						}});
						sqlType=TableView.ITEM_ADDED_TO_DATABASE;
					} catch (SqlException e) {
						e.printStackTrace();
					}
				} else if (download.isRemoved()){
					try {
						dbTable.delete(new HashMap<String, Object>(){{
							put("url",download.getURL().toString());
						}});
					} catch (SqlException e) {
						e.printStackTrace();
					}
					sqlType=TableView.ITEM_REMOVED_FROM_DATABASE;
				} else if (download.isUpdated()){
					try {
						dbTable.update(new HashMap<String, Object>(){{
							put("size",Long.parseLong(download.getSize()));
							put("destination",download.getDestination());
							put("podcastSource",download.getPodcastId());
							put("status",download.getStatus());
							put("priority",downloadCount.intValue());
						}}, 
							new HashMap<String, Object>(){{
								put("url",download.getURL().toString());
						}});
					} catch (SqlException e) {
						e.printStackTrace();
					}
					sqlType=TableView.ITEM_UPDATED_IN_DATABASE;
				}
				switch (sqlType){
					case TableView.ITEM_ADDED_TO_DATABASE:
						downloads.get(downloads.indexOf(download)).setAdded(true);
						break;
					case TableView.ITEM_UPDATED_IN_DATABASE:
						downloads.get(downloads.indexOf(download)).setUpdated(true);
						break;
				}
			}
		} else {
			Podsalinan.debugLog.logError("Error db connection is closed");
		}
	}

	public void deleteActiveDownload(int parseInt) {
		//TODO:Flesh this out
	}
}
