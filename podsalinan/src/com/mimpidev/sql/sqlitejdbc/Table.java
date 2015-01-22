/**
 * 
 */
package com.mimpidev.sql.sqlitejdbc;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
}
