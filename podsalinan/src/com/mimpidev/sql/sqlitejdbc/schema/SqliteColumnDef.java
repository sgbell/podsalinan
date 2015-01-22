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
package com.mimpidev.sql.sqlitejdbc.schema;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mimpidev.sql.sqlitejdbc.exceptions.SqliteException;

/**
 * @author bugman
 *
 */
public class SqliteColumnDef {

	private String type;
	private boolean primaryKey;
	private boolean notnull;
	private String name;

	public SqliteColumnDef(ResultSet columnDef) throws SqliteException{
		try {
			setType(columnDef.getString("type"));
			setPrimaryKey(columnDef.getBoolean("pk"));
			setNotnull(columnDef.getBoolean("notnull"));
			setName(columnDef.getString("name"));
		} catch (SQLException e) {
			throw new SqliteException("Failed to read column details.");
		}
		
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the primaryKey
	 */
	public boolean isPrimaryKey() {
		return primaryKey;
	}
	/**
	 * @param primaryKey the primaryKey to set
	 */
	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}
	/**
	 * @return the notnull
	 */
	public boolean isNotnull() {
		return notnull;
	}
	/**
	 * @param notnull the notnull to set
	 */
	public void setNotnull(boolean notnull) {
		this.notnull = notnull;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}
