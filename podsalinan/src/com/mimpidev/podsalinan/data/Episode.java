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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.mimpidev.dev.sql.DataRecord;
import com.mimpidev.dev.sql.field.StringType;

/**
 * @author bugman
 *
 */
public class Episode extends URLDetails {
	
	private static final String dateFormat="EEE, dd-MMM-yyy HH:mm:ss";
	private static final String originalDateFormat="EEE, dd MMM yyy HH:mm:ss zzz";

	public Episode(String published, String title, String url, String length,
			       String desc, int status) {
		super(url,length);
		setDate(published);
		setTitle(title);
		setDescription(desc);
		setStatus(status);
	}
	
	public Episode() {
		super();
		put("date", new StringType());
		put("title", new StringType());
		put("description", new StringType());
	}
	
	public Episode(Map<String,String> record){
		this();
		populateFromRecord(record);
	}

	public Episode(DataRecord dataRecord) {
		super(dataRecord);
	}

	public String getDate(){
		// Original Timestamp in xml file
		DateFormat df = new SimpleDateFormat(originalDateFormat);
		try {
			Date newDate = df.parse(get("date").getValue());
			DateFormat newFormat = new SimpleDateFormat(dateFormat);
			return newFormat.format(newDate);
		} catch (ParseException e) {
			// If date in data file is now set to the user readable format
			setUpdated(true);
			return get("date").getValue();
		}
	}
	
	/**
	 * This method is used to ensure that all the dates are stored in their original format
	 * @return
	 */
	public String getOriginalDate(){
		DateFormat df = new SimpleDateFormat(dateFormat);
		try {
			Date newDate = df.parse(get("date").getValue());
			DateFormat newFormat = new SimpleDateFormat(originalDateFormat);
			// If date is in the user readable format, reformat it.
			return newFormat.format(newDate);
		} catch (ParseException e) {
			// If date is in original format return it
			return get("date").getValue();
		}
	}
	
	public void setDate(String newDate){
		get("date").setValue(newDate);
	}
	
	public static String getDateFormat(){
		return dateFormat;
	}
	
	public String getDescription(){
		return get("description").getValue();
	}
	
	public void setDescription(String newDescription){
		get("description").setValue(newDescription);
	}
	
	public String getTitle(){
		return get("title").getValue();
	}
	
	public void setTitle(String newTitle){
		get("title").setValue(newTitle);
	}

	public String getFilename() throws MalformedURLException {
		return (new URL(get("url").getValue())).getFile();
	}
	public boolean dateEquals(Date searchDate) {
		DateFormat df = new SimpleDateFormat(originalDateFormat);
		try {
			Date episodeDate = df.parse(get("date").getValue());
			Calendar episodeCalendar = Calendar.getInstance();
			Calendar searchCalendar = Calendar.getInstance();
			episodeCalendar.setTime(episodeDate);
			searchCalendar.setTime(searchDate);
			
			if ((episodeCalendar.get(Calendar.DAY_OF_MONTH)==searchCalendar.get(Calendar.DAY_OF_MONTH))&&
				(episodeCalendar.get(Calendar.MONTH)==searchCalendar.get(Calendar.MONTH))&&
				(episodeCalendar.get(Calendar.YEAR)==searchCalendar.get(Calendar.YEAR)))
			   return true;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public String toString(){
		return "Title - "+getTitle()+" - Date - "+getDate()+" -- Description "+getDescription();
	}
	
	
	public int getStatus(){
		if (super.getStatus()==UNKNOWN_STATUS){
			setStatus(NOT_QUEUED);
		}
		
		return super.getStatus(); 
	}
}
