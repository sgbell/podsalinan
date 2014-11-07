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
		setPodcastSource(podcast);
	}
	/**
	 * 
	 * @param url
	 * @param added
	 */
	public URLDownload(URL url, boolean added){
		super(url.toString(),added);
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
		
		destination = fields.get("directory").getValue()+"/"+getFilenameDownload();
		
		return destination;
	}

	/**
	 * @param destination the destination to set
	 */
	public void setDestination(String destination) {
		fields.get("directory").setValue(destination);
	}

	public void setDestination(File outputFile) {
		fields.get("direcotory").setValue(outputFile.getAbsolutePath());
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

}
