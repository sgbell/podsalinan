/**
 * 
 */
package com.mimpidev.sql.sqlitejdbc;

import java.util.Map;

import com.mimpidev.dev.debug.Log;
import com.mimpidev.sql.sqlitejdbc.exceptions.SqliteException;
import com.mimpidev.sql.sqlitejdbc.schema.SqliteTable;

/**
 * @author sbell
 *
 */
public class Table {

	private String tableName = "";
	private final Database database;
	private SqliteTable tableDefinition;
	protected boolean debug = false;

    /**
     * 
     * @param newTableName
     * @param newDatabase
     * @throws ClassNotFoundException
     */
	public Table(String newTableName, Database newDatabase) throws ClassNotFoundException{
		database = newDatabase;
		setTableName(newTableName);
	}
	public Table(Map<String, String> newColumnList, String tableName,
			Log debugLog, Database newDatabase) {
		database = newDatabase;
		setTableName(tableName);
	}
	/**
	 * @return the databaseConnection
	 */
	public Database getDatabase() {
		return database;
	}
	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}
	/**
	 * 
	 * @param newTableName
	 */
	public void setTableName(String newTableName) {
		tableName=newTableName;
		try {
			tableDefinition = new SqliteTable(database,tableName);
		} catch (SqliteException e) {
			if (Log.isDebug())Log.logError(this, "Error opening database");
			if (Log.isDebug())Log.logError(this, e.getMessage());
			if (Log.isDebug())Log.printStackTrace(e.getStackTrace());
		}
	}

	/**
	 * @return the tableDefinition
	 */
	public SqliteTable getTableDefinition() {
		return tableDefinition;
	}
	/**
	 * @param tableDefinition the tableDefinition to set
	 */
	public void setTableDefinition(SqliteTable tableDefinition) {
		this.tableDefinition = tableDefinition;
	}
	/**
	 * @return the debug
	 */
	public boolean isDebug() {
		return debug;
	}
	/**
	 * @param debug the debug to set
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
}
