/**
 * 
 */
package com.mimpidev.podsalinan.data;

import java.io.File;
import java.net.URL;

/**
 * @author bugman
 *
 */
public class URLDownload extends Details {
	
	private File destination;
	private String podcastId;
	
	public URLDownload(){
		super();
	}
	
	public URLDownload(String url) {
		super(url);
	}

	public URLDownload(String url, String length){
		super(url, length);
	}
	
	public URLDownload(String url, boolean added){
		super(url, added);
	}
	
	public URLDownload(String url, String length, String destination){
		super(url, length);
		setDestination(destination);
	}
	
	public URLDownload(String url, boolean added, String destination){
		super(url, added);
		setDestination(destination);
	}

	public URLDownload(String url, String length, boolean added, String destination){
		super(url, added);
		setSize(length);
		setDestination(destination);
	}
	
	public URLDownload(String url, String length, String destination, String podcast, 
			           int status){
		super(url, length);
		setDestination(destination);
		this.status=status;
		this.podcastId=podcast;
	}
	
	public URLDownload(URL url, boolean added){
		super(url,added);
	}
	
	/**
	 * @return the destination, including the filename
	 */
	public String getDestination() {
		return destination.toString();
	}

	/**
	 * @param destination the destination to set
	 */
	public void setDestination(String destination) {
		this.destination = new File(destination);
	}

	public void setDestination(File outputFile) {
		destination = outputFile;
	}

	/**
	 * @return the podcastId
	 */
	public String getPodcastId() {
		return podcastId;
	}

	/**
	 * @param podcastId the podcastId to set
	 */
	public void setPodcastId(String podcastId) {
		this.podcastId = podcastId;
		this.setUpdated(true);
	}
	
	
	public String getFilenameDownload(){
		return getURL().toString().split("/")[getURL().toString().split("/").length-1];
	}
	
	public File getDestinationFile(){
		return destination;
	}

}
