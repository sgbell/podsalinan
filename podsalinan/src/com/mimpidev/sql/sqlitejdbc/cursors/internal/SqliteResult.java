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
package com.mimpidev.sql.sqlitejdbc.cursors.internal;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.mimpidev.sql.sqlitejdbc.exceptions.SqliteException;

/**
 * @author bugman
 *
 */
public class SqliteResult {

	private Map<String,String> values;

	public SqliteResult(ResultSet results) throws SqliteException {
		values = new HashMap<String,String>();
		try {
			ResultSetMetaData metaData = results.getMetaData();
			for (int columnNum=1; columnNum<metaData.getColumnCount()+1; columnNum++){
				values.put(metaData.getColumnLabel(columnNum), results.getString(metaData.getColumnLabel(columnNum)));
			}
		} catch (SQLException e) {
			throw new SqliteException("Error reading data from table.");
		}
		
	}
	
	public Map<String, String> getValues() {
		return values;
	}

	public void setValues(Map<String, String> values) {
		this.values = values;
	}
}
