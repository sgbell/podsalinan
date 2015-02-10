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

import com.mimpidev.dev.sql.SqlException;
import com.mimpidev.dev.sql.field.FieldDetails;
import com.mimpidev.dev.sql.field.StringType;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.data.ProgSettings;
import com.mimpidev.sql.sqlitejdbc.Database;

/**
 * @author bugman
 *
 */
public class SettingsLoader extends TableLoader {

	private ProgSettings settings;

	public SettingsLoader(ProgSettings newSettings, Database dbConnection) throws ClassNotFoundException {
		super("settings",dbConnection);
		String[] columnNames = {"id","name","value"};
		String[] columnTypes = {"INTEGER PRIMARY KEY AUTOINCREMENT",
				                "TEXT","TEXT"};
		createColumnList (columnNames, columnTypes);
		setdbTable(dbConnection);
		settings=newSettings;

	}

	@Override
	public void readTable() {
		ArrayList<Map<String,String>> recordSet = readFromTable();
		
		if ((recordSet!=null)&&(recordSet.size()>0))
		for (Map<String,String> record: recordSet){
			settings.getMap().put(record.get("name"),record.get("value"));
		}
	}

	@Override
	public void updateDatabase() {
		if (isDbOpen()){
			try {
				purgeTable();
			} catch (SqlException e) {
				e.printStackTrace();
			}
			
			
			for (final Map.Entry<String, String> entry : settings.getMap().entrySet()){
				try {
					insert(new HashMap<String, FieldDetails>(){{
						put("name",new StringType(entry.getKey()));
						put("value",new StringType(entry.getValue()));
					}});
				} catch (SqlException e) {
					Podsalinan.debugLog.logError(this, "Error inserting record into settings table");
					Podsalinan.debugLog.logError(this, e.getMessage());
					Podsalinan.debugLog.printStackTrace(e.getStackTrace());
				}
			}
		} else {
			Podsalinan.debugLog.logError("Error db connection is closed");
		}
	}

}
