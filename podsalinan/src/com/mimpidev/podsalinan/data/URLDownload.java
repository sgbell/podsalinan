/**
 * 
 */
package com.mimpidev.podsalinan.data;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.mimpidev.podsalinan.Podsalinan;

/**
 * @author bugman
 *
 */
public class URLDownload extends URLDetails {
	
	private File destination;
	private String podcastId;
	private String uid;
	
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
		setUid(url+destination);
	}

	public URLDownload(String url, String length, boolean added, String destination){
		this(url, added, destination);
		setSize(length);
	}
	
	public URLDownload(String url, String length, String destination, String podcast, 
			           int status){
		this(url, length, destination);
		setStatus(status);
		this.podcastId=podcast;
	}
	
	public URLDownload(URL url, boolean added){
		super(url.toString(),added);
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

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(String newUid) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytesUid = (newUid).getBytes("UTF-8");
			md.update(bytesUid, 0, bytesUid.length);
			uid = new BigInteger(1, md.digest()).toString().substring(0,8);
		} catch (NoSuchAlgorithmException e) {
			Podsalinan.debugLog.printStackTrace(e.getStackTrace());
		} catch (UnsupportedEncodingException e) {
			Podsalinan.debugLog.printStackTrace(e.getStackTrace());
		}
	}

}
