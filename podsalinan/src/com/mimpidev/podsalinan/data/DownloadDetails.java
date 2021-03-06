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

package com.mimpidev.podsalinan.data;

import com.mimpidev.dev.sql.DataRecord;
import com.mimpidev.dev.sql.field.StringType;

/**
 * 
 * @author bugman
 *
 */
public class DownloadDetails extends BaseURL{

	public DownloadDetails(){
		super();
		put("name", new StringType());
		put("datafile", new StringType());
		put("directory", new StringType());
	}
	
	public DownloadDetails(String name){
		this();
		setName(name);
	}
	
	public DownloadDetails(DataRecord dataRecord) {
		super(dataRecord);
	}

	public void setName(String name) {
		get("name").setValue(name);
	}

	public String getName() {
		return get("name").getValue();
	}
	
	public void setDatafile(String newDatafile){
		get("datafile").setValue(newDatafile);
	}
	
	public String getDatafile(){
		return get("datafile").getValue();
	}

	public void setDirectory(String directory) {
		get("directory").setValue(directory);
	}

	public String getDirectory() {
		return get("directory").getValue();
	}
}