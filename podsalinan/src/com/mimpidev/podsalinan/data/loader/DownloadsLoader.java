/*******************************************************************************
 * Copyright (c) Sam Bell.
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
package com.mimpidev.podsalinan.data.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.mimpidev.dev.sql.DataRecord;
import com.mimpidev.dev.sql.SqlException;
import com.mimpidev.dev.sql.TableView;
import com.mimpidev.dev.sql.field.FieldDetails;
import com.mimpidev.dev.sql.field.StringType;
import com.mimpidev.dev.debug.Log;
import com.mimpidev.podsalinan.data.URLDownload;
import com.mimpidev.podsalinan.data.URLDownloadList;
import com.mimpidev.sql.sqlitejdbc.Database;

/**
 * @author bugman
 *
 */
public class DownloadsLoader extends TableLoader {

	private URLDownloadList downloads;
	/**
	 * @param podsalinanDB 
	 * @throws ClassNotFoundException 
	 * 
	 */
	public DownloadsLoader(URLDownloadList downloadList, Database dbConnection) throws ClassNotFoundException {
		super("downloads",dbConnection);
		setDownloads(downloadList);
		createColumnList(new URLDownload().getDatabaseRecord());
		setdbTable(dbConnection);
	}

	@Override
	public void readTable() {
		ArrayList<Map<String,String>> recordSet = readFromTable();
		
		if ((recordSet!=null)&&(recordSet.size()>0))
		for (Map<String,String> record: recordSet){
			// Create a new URLDownload and add to the downloads Vector
			URLDownload newDownload = new URLDownload(record);
			newDownload.setAdded(true);
			int priority;
			try {
				priority=Integer.parseInt(record.get("priority"));
			} catch (NumberFormatException e){
				priority=downloads.visibleSize()+1;
			}
				
			downloads.addDownload(newDownload, priority);
		}
	}

	@Override
	public void updateDatabase() {
		if (isDbOpen()){
			Vector<URLDownload> downloadList= new Vector<URLDownload>();
			synchronized(downloads){
				for (final URLDownload download : downloads.getDownloads()){
					downloadList.add(new URLDownload((DataRecord)download));
				}
			}
			for (final URLDownload download : downloadList){
				int sqlType=TableView.NOTHING_CHANGED;
				if (!download.isAdded()){
					// Used to set the correct flag
					try {
						insert(download.getDatabaseRecord());
						sqlType=TableView.ITEM_ADDED_TO_DATABASE;
					} catch (SqlException e) {
						e.printStackTrace();
					}
				} else if (download.isRemoved()){
					try {
						delete(new HashMap<String, FieldDetails>(){/**
							 * 
							 */
							private static final long serialVersionUID = 5750706774356825968L;
							{
							put("url",new StringType(download.getURL().toString()));
						}});
					} catch (SqlException e) {
						e.printStackTrace();
					}
					sqlType=TableView.ITEM_REMOVED_FROM_DATABASE;
				} else if (download.isUpdated()){
					try {
						update(download.getDatabaseRecord(), 
							new HashMap<String, FieldDetails>(){/**
								 * 
								 */
								private static final long serialVersionUID = 6980779916796068770L;
								{
								put("url",new StringType(download.getURL().toString()));
						}});
					} catch (SqlException e) {
						e.printStackTrace();
					}
					sqlType=TableView.ITEM_UPDATED_IN_DATABASE;
				}
				switch (sqlType){
					case TableView.ITEM_ADDED_TO_DATABASE:
						downloads.getDownloads().get(downloads.getDownloads().indexOf(download)).setAdded(true);
						break;
					case TableView.ITEM_UPDATED_IN_DATABASE:
						downloads.getDownloads().get(downloads.getDownloads().indexOf(download)).setUpdated(true);
						break;
				}
			}
		} else {
			if (Log.isDebug())Log.logError(this, "Error db connection is closed");
		}
	}

	/**
	 * @return the downloads
	 */
	public URLDownloadList getDownloads() {
		return downloads;
	}

	/**
	 * @param downloadList the downloads to set
	 */
	public void setDownloads(URLDownloadList downloadList) {
		this.downloads = downloadList;
	}

}
