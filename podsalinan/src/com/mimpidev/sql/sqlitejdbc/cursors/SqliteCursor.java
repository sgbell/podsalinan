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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mimpidev.sql.sqlitejdbc.Database;
import com.mimpidev.sql.sqlitejdbc.SqliteDataTable;
import com.mimpidev.sql.sqlitejdbc.cursors.internal.SqliteResult;
import com.mimpidev.sql.sqlitejdbc.exceptions.SqliteException;

/**
 * @author bugman
 *
 */
public class SqliteCursor {

	protected Database db;
	protected SqliteDataTable table;
	protected List<SqliteResult> tableData;


	public SqliteCursor(Database db, SqliteDataTable table) {
		setDb(db);
		setTable(table);
	}

	/**
	 * @return the db
	 */
	public Database getDb() {
		return db;
	}

	/**
	 * @param db the db to set
	 */
	public void setDb(Database db) {
		this.db = db;
	}

	/**
	 * @return the table
	 */
	public SqliteDataTable getTable() {
		return table;
	}

	/**
	 * @param table the table to set
	 */
	public void setTable(SqliteDataTable table) {
		this.table = table;
	}

	/**
	 * @return the results
	 */
	public List<SqliteResult> getResults() {
		return tableData;
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(List<SqliteResult> results) {
		tableData = results;
	}
	
	public List<SqliteResult> executeSql(String sqlString) throws SqliteException{
		tableData = new ArrayList<SqliteResult>();
		synchronized(db){
			try {
				Statement sql = db.getConnection().createStatement();
				ResultSet results = sql.executeQuery(sqlString);
				while (results.next()){
					SqliteResult newRow = new SqliteResult(results);
					tableData.add(newRow);
				}
				return tableData;
			} catch (SQLException e) {
				System.err.println(sqlString);
				throw new SqliteException("Problem executing sql statement on table: "+table.getName(),sqlString);
			}
		}
	}
}
