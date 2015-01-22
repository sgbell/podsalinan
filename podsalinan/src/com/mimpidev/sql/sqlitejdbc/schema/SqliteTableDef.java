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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mimpidev.sql.sqlitejdbc.Database;

/**
 * @author bugman
 *
 */
public class SqliteTableDef {

	private String sql;
	private List<SqliteColumnDef> columns;
	
	public SqliteTableDef(Database database, String tableName,
			HashMap<String, Object> hashMap) {
		// TODO Auto-generated constructor stub
	}

	public SqliteTableDef() {
		setColumns(new ArrayList<SqliteColumnDef>());
	}

	public String getPrimaryKeyName() {
		String primaryKey="";
		for (SqliteColumnDef column: columns){
			if (column.isPrimaryKey())
				if (primaryKey.length()>0)
					primaryKey+=" ";
			    primaryKey+=column.getName();
		}
		return primaryKey;
	}

	/**
	 * @return the sql
	 */
	public String getSql() {
		return sql;
	}

	/**
	 * @param sql the sql to set
	 */
	public void setSql(String sql) {
		this.sql = sql;
	}

	/**
	 * @return the columns
	 */
	public List<SqliteColumnDef> getColumns() {
		return columns;
	}

	/**
	 * @param columns the columns to set
	 */
	public void setColumns(List<SqliteColumnDef> columns) {
		this.columns = columns;
	}

	public void addColumn(SqliteColumnDef columnDetails) {
		columns.add(columnDetails);
	}

}
