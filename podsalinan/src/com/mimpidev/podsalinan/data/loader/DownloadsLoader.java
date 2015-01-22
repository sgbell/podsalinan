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

import org.tmatesoft.sqljet.core.table.SqlJetDb;

import com.mimpidev.dev.sql.SqlException;
import com.mimpidev.dev.sql.TableView;
import com.mimpidev.dev.sql.field.FieldDetails;
import com.mimpidev.dev.sql.field.StringType;
import com.mimpidev.podsalinan.Podsalinan;
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
	 * 
	 */
	public DownloadsLoader(URLDownloadList downloadList, Database dbConnection) {
		super(dbConnection);
		setDownloads(downloadList);
		setTableName("downloads");
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
			downloads.addDownload(newDownload, Integer.parseInt(record.get("priority")));
		}
	}

	@Override
	public void updateDatabase() {
		if (isDbOpen()){
			// Need to figure out how to increment downloadCount
			final Integer downloadCount= new Integer(0);
			for (final URLDownload download : downloads.getDownloads()){
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
						delete(new HashMap<String, FieldDetails>(){{
							put("url",new StringType(download.getURL().toString()));
						}});
					} catch (SqlException e) {
						e.printStackTrace();
					}
					sqlType=TableView.ITEM_REMOVED_FROM_DATABASE;
				} else if (download.isUpdated()){
					try {
						update(download.getDatabaseRecord(), 
							new HashMap<String, FieldDetails>(){{
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
			Podsalinan.debugLog.logError("Error db connection is closed");
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
