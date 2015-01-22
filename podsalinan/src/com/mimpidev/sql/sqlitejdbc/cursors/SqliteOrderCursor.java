/**
 * 
 */
package com.mimpidev.sql.sqlitejdbc.cursors;

import com.mimpidev.sql.sqlitejdbc.Database;
import com.mimpidev.sql.sqlitejdbc.SqliteDataTable;
import com.mimpidev.sql.sqlitejdbc.exceptions.SqliteException;

/**
 * @author sbell
 *
 */
public class SqliteOrderCursor extends SqliteCursor {

	public SqliteOrderCursor(Database db, SqliteDataTable table, String orderby) throws SqliteException {
		super(db, table);
		String sqlStatement = "SELECT * FROM "+table.getName();
		if (orderby != null){
		   sqlStatement+=" ORDER BY "+orderby;
		}
		executeSql(sqlStatement);
	}

}
