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
import java.util.Map;

import com.mimpidev.dev.sql.field.StringType;
import com.mimpidev.dev.debug.Log;

/**
 * @author bugman
 *
 */
public class URLDownload extends URLDetails {
	
	public URLDownload(){
		super();
		initializeFields();
	}
	
	public URLDownload(String url) {
		super(url);
		initializeFields();
	}

	public URLDownload(String url, String length){
		super(url, length);
		initializeFields();
	}
	
	public URLDownload(String url, boolean added){
		super(url, added);
		initializeFields();
	}
	
	public URLDownload(String url, String length, String destination){
		super(url, length);
		initializeFields();
		setDestination(destination);
		setUid(url+destination);
	}
	
	public URLDownload(String url, boolean added, String destination){
		super(url, added);
		initializeFields();
		setDestination(destination);
		setUid(url+destination);
	}
	
	public void initializeFields(){
		put("podcastSource", new StringType());
		put("uid", new StringType());
		put("directory", new StringType());
	}

	public URLDownload(String url, String length, boolean added, String destination){
		this(url, added, destination);

		setSize(length);
	}
	
	public URLDownload(String url, String length, String destination, String podcast, 
			           int status){
		this(url, length, destination);
		setStatus(status);
		setPodcastSource(podcast);
	}
	/**
	 * 
	 * @param url
	 * @param added
	 */
	public URLDownload(URL url, boolean added){
		super(url.toString(),added);
		initializeFields();
	}
	/**
	 * 
	 * @param recordSet
	 */
	public URLDownload(Map<String, String> record) {
		this();
		// If previously saved as downloading, This will load it into the
		// system as queued
		if (record.get("status").equals(""+CURRENTLY_DOWNLOADING)){
			record.put("status", ""+DOWNLOAD_QUEUED);
		}
		populateFromRecord(record);
	}
	
	/**
	 * @return the destination, including the filename
	 */
	public String getDestination() {
		String destination;

		// Need to test if directory is actually a file and not a directory
		File test = new File (get("directory").getValue());
		if (test.exists() && test.isDirectory()){
		   destination = get("directory").getValue()+"/"+getFilenameDownload();
		} else if (get("directory").getValue().endsWith(".xml")){
			destination = get("directory").getValue();
		} else {
			destination = get("directory").getValue()+"/"+getFilenameDownload();
		}
		
		return destination;
	}

	/**
	 * @param destination the destination to set
	 */
	public void setDestination(String destination) {
		get("directory").setValue(destination);
	}

	public void setDestination(File outputFile) {
		get("directory").setValue(outputFile.getAbsolutePath());
	}

	/**
	 * @return the podcastId
	 */
	public String getPodcastSource() {
		return get("podcastSource").getValue();
	}

	/**
	 * @param podcastId the podcastId to set
	 */
	public void setPodcastSource(String podcastSource) {
		get("podcastSource").setValue(podcastSource);
		setUpdated(true);
	}
	
	
	public String getFilenameDownload(){
		return getURL().toString().split("/")[getURL().toString().split("/").length-1];
	}
	
	public File getDestinationFile(){
		return new File(getDestination());
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		// Just setting a uid, incase the download doesn't have one set already.
		if (get("uid").getValue().length()==0){
			setUid(getURL()+getDestination());
		}
		return get("uid").getValue();
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(String newUid) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytesUid = (newUid).getBytes("UTF-8");
			md.update(bytesUid, 0, bytesUid.length);
			get("uid").setValue(new BigInteger(1, md.digest()).toString().substring(0,8));
		} catch (NoSuchAlgorithmException e) {
			if (Log.isDebug())Log.printStackTrace(e.getStackTrace());
		} catch (UnsupportedEncodingException e) {
			if (Log.isDebug())Log.printStackTrace(e.getStackTrace());
		}
	}
	
	public void setDirectory(String directory) {
		get("directory").setValue(directory);
	}

	public String getDirectory() {
		return get("directory").getValue();
	}
}
