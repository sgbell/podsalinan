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
package com.mimpidev.sql.sqlitejdbc.cursors;

import com.mimpidev.sql.sqlitejdbc.Database;
import com.mimpidev.sql.sqlitejdbc.SqliteDataTable;
import com.mimpidev.sql.sqlitejdbc.exceptions.SqliteException;

/**
 * @author bugman
 *
 */
public class SqliteScopeConditional extends SqliteCursor{

	private String conditions;
	
	public SqliteScopeConditional(SqliteDataTable table, Database db,
			String conditions) throws SqliteException {
		super (db,table);
		setConditions(conditions);

		String sqlStatement = "SELECT * FROM "+table.getName()+" WHERE "+conditions;
		executeSql(sqlStatement);
	}
	
	/**
	 * @return the conditions
	 */
	public String getConditions() {
		return conditions;
	}

	/**
	 * @param conditions the conditions to set
	 */
	public void setConditions(String conditions) {
		this.conditions = conditions;
	}

	/**
	 * @return the rowCount
	 */
	public long getRowCount() {
		return tableData.size();
	}
}
