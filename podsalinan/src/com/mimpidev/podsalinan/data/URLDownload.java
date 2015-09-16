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
import com.mimpidev.podsalinan.Podsalinan;

/**
 * @author bugman
 *
 */
public class URLDownload extends URLDetails {
	
	public URLDownload(){
		super();
		fields.put("podcastSource", new StringType());
		fields.put("uid", new StringType());
		fields.put("directory", new StringType());
	}
	
	public URLDownload(String url) {
		super(url);
		fields.put("podcastSource", new StringType());
		fields.put("uid", new StringType());
		fields.put("directory", new StringType());
	}

	public URLDownload(String url, String length){
		super(url, length);
		fields.put("podcastSource", new StringType());
		fields.put("uid", new StringType());
		fields.put("directory", new StringType());
	}
	
	public URLDownload(String url, boolean added){
		super(url, added);
		fields.put("podcastSource", new StringType());
		fields.put("uid", new StringType());
		fields.put("directory", new StringType());
	}
	
	public URLDownload(String url, String length, String destination){
		super(url, length);
		fields.put("podcastSource", new StringType());
		fields.put("uid", new StringType());
		fields.put("directory", new StringType());
		setDestination(destination);
		setUid(url+destination);
	}
	
	public URLDownload(String url, boolean added, String destination){
		super(url, added);
		fields.put("podcastSource", new StringType());
		fields.put("uid", new StringType());
		fields.put("directory", new StringType());
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
		setPodcastSource(podcast);
	}
	/**
	 * 
	 * @param url
	 * @param added
	 */
	public URLDownload(URL url, boolean added){
		super(url.toString(),added);
		fields.put("podcastSource", new StringType());
		fields.put("uid", new StringType());
		fields.put("directory", new StringType());
	}
	/**
	 * 
	 * @param recordSet
	 */
	public URLDownload(Map<String, String> record) {
		this();
		populateFromRecord(record);
	}
	
	/**
	 * @return the destination, including the filename
	 */
	public String getDestination() {
		String destination;

		// Need to test if directory is actually a file and not a directory
		File test = new File (fields.get("directory").getValue());
		if (test.exists() && test.isDirectory()){
		   destination = fields.get("directory").getValue()+"/"+getFilenameDownload();
		} else if (fields.get("directory").getValue().endsWith(".xml")){
			destination = fields.get("directory").getValue();
		} else {
			destination = fields.get("directory").getValue()+"/"+getFilenameDownload();
		}
		
		return destination;
	}

	/**
	 * @param destination the destination to set
	 */
	public void setDestination(String destination) {
		fields.get("directory").setValue(destination);
	}

	public void setDestination(File outputFile) {
		fields.get("directory").setValue(outputFile.getAbsolutePath());
	}

	/**
	 * @return the podcastId
	 */
	public String getPodcastSource() {
		return fields.get("podcastSource").getValue();
	}

	/**
	 * @param podcastId the podcastId to set
	 */
	public void setPodcastSource(String podcastSource) {
		fields.get("podcastSource").setValue(podcastSource);
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
		if (fields.get("uid").getValue().length()==0){
			setUid(getURL()+getDestination());
		}
		return fields.get("uid").getValue();
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(String newUid) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytesUid = (newUid).getBytes("UTF-8");
			md.update(bytesUid, 0, bytesUid.length);
			fields.get("uid").setValue(new BigInteger(1, md.digest()).toString().substring(0,8));
		} catch (NoSuchAlgorithmException e) {
			Podsalinan.debugLog.printStackTrace(e.getStackTrace());
		} catch (UnsupportedEncodingException e) {
			Podsalinan.debugLog.printStackTrace(e.getStackTrace());
		}
	}
	
	public void setDirectory(String directory) {
		fields.get("directory").setValue(directory);
	}

	public String getDirectory() {
		return fields.get("directory").getValue();
	}
}
