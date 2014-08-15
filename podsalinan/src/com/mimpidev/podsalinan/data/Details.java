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

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author bugman
 *
 */
public class Details {

	private URL     url;
	private String 	size;
	private boolean added=false,		  // Has this podcast been added to the database
	   			    remove=false, // Does this podcast need to be deleted from the system
					updated=false;
	protected int	    status;  	  // This is used to track if the system has already downloaded the file.
	
	public static final int NOT_QUEUED=0,
							DOWNLOAD_QUEUED=1,
							CURRENTLY_DOWNLOADING=2,
							FINISHED=3,
							DOWNLOAD_CANCELLED=4,
							DO_NOT_DOWNLOAD=5,
							INCOMPLETE_DOWNLOAD=6,
							DOWNLOAD_FAULT=7,
							DESTINATION_INVALID=8;
	
	public Details (){
		
	}
	
	public Details (String url){
		try {
			this.url= new URL(url);
		} catch (MalformedURLException e) {
		}
		size="0";
	}
	
	public Details (URL url){
		this.url = url;
	}
	
	public Details(String url, String length){
		this(url);
		this.size=length;
	}
	
	public Details(URL url, String length){
		this(url);
		this.size=length;
	}
	
	public Details(String url, boolean added){
		this(url);
		this.added=added;
	}
	
	public Details(URL url, boolean added){
		this(url);
		this.added=added;
	}
	
	public URL getURL(){
		return url;
	}
	
	public void setURL(String url){
		try {
			this.url= new URL(url);
		} catch (MalformedURLException e) {
		}
	}
	
	public void setURL(URL url){
		this.url = url;
	}
	
	public String getSize(){
		return size;
	}
	
	public void setSize(String size){
		this.size=size;
	}
	
	public boolean isAdded(){
		return added;
	}
	
	public void setAdded(boolean added){
		this.added = added;
	}
	
	public boolean isRemoved(){
		return remove;
	}
	
	public void setRemoved(boolean removed){
		remove=removed;
	}
	
	public int getStatus(){
		return status;
	}
	
	public void setStatus(int newStatus){
		status=newStatus;
		updated=true;
	}

	/**
	 * @return the updated
	 */
	public boolean isUpdated() {
		return updated;
	}

	/**
	 * @param updated the updated to set
	 */
	public void setUpdated(boolean updated) {
		this.updated = updated;
	}
	
	public String getCurrentStatus(){
		return getStatusString(getStatus());
	}
	
	public String getStatusString(int statusValue){
		String status=null;
		
		switch (statusValue){
			case NOT_QUEUED:
				status = "Not Downloaded";
				break;
			case DOWNLOAD_QUEUED:
				status = "Download Queued";
				break;
			case CURRENTLY_DOWNLOADING:
				status = "Downloading Currently";
				break;
			case FINISHED:
				status = "Completed Download";
				break;
			case DOWNLOAD_CANCELLED:
				status = "Download Cancelled";
				break;
			case DO_NOT_DOWNLOAD:
				status = "Marked do not Download";
				break;
			case INCOMPLETE_DOWNLOAD:
				status = "Download Incomplete";
				break;
			case DOWNLOAD_FAULT:
				status = "Download Fault";
				break;
			case DESTINATION_INVALID:
				status = "Destination Invalid";
				break;
		}
		
		return status;
	}
	
	public char getCharStatus() {
		char status='\0';
		
		switch (this.status){
		case NOT_QUEUED:
			status = '\0';
			break;
		case DOWNLOAD_QUEUED:
			status = 'Q';
			break;
		case CURRENTLY_DOWNLOADING:
			status = 'D';
			break;
		case FINISHED:
			status = 'F';
			break;
		case DOWNLOAD_CANCELLED:
			status = 'C';
			break;
		case DO_NOT_DOWNLOAD:
			status = '*';
			break;
		case INCOMPLETE_DOWNLOAD:
			status = 'I';
			break;
		case DOWNLOAD_FAULT:
			status = 'B';
			break;
		case DESTINATION_INVALID:
			status = '-';
			break;
	}
		
		return status;
	}
}