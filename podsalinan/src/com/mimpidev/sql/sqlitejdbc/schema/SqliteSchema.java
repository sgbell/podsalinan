/**
 * 
 */
package com.mimpidev.sql.sqlitejdbc.schema;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.mimpidev.sql.sqlitejdbc.Database;
import com.mimpidev.sql.sqlitejdbc.exceptions.SqliteException;

/**
 * @author sbell
 *
 */
public class SqliteSchema {

	private Map<String,SqliteTableDef> tableDefs;
	private Database database;

	public SqliteSchema(Database newDatabase) throws SqliteException{
		setDatabase(newDatabase);
		tableDefs=new HashMap<String,SqliteTableDef>();
		readSchema();
	}

	public void readSchema() throws SqliteException{
		Statement sql;
		try {
			synchronized(database){
				sql = database.getConnection().createStatement();
				ResultSet results = sql.executeQuery("SELECT * FROM sqlite_master WHERE type='table'");
				while (results.next()){
					SqliteTableDef tableDef = new SqliteTableDef();
					tableDef.setSql(results.getString("sql"));
					tableDefs.put(results.getString("name"), tableDef);
				}
			}

			Set<String> keys = tableDefs.keySet();
			for (String key : keys){
				synchronized(database){
					sql = database.getConnection().createStatement();
					ResultSet tableInfo = sql.executeQuery("PRAGMA table_info("+key+")");
					while (tableInfo.next()){
						SqliteColumnDef columnDetails = new SqliteColumnDef(tableInfo);
						tableDefs.get(key).addColumn(columnDetails);
					}
				}
			}
		} catch (SQLException e) {
			throw new SqliteException("Failed read table details.");
		}
	}

	/**
	 * @return the database
	 */
	public Database getDatabase() {
		return database;
	}

	/**
	 * @param database the database to set
	 */
	public void setDatabase(Database database) {
		this.database = database;
	}

	/**
	 * @return the tables
	 */
	public Map<String,Object> getTables() {
		return null;
	}

	/**
	 * @param tables the tables to set
	 */
	public void setTables(Map<String,Object> tables) {
		
	}

	public boolean dropTable(String tableName) throws SqliteException {
		if (tableDefs.containsKey(tableName)){
			Statement sql;
			try {
				sql = database.getConnection().createStatement();
                sql.executeQuery("DROP TABLE "+tableName);
                return true;
			} catch (SQLException e) {
				throw new SqliteException("Problem with DROP TABLE command.");
			}
		} else {
			throw new SqliteException("Table name does not exist.");
		}
	}

	public Object createTable(String createStatement) throws SqliteException {
		Statement sql;
		try {
			sql = database.getConnection().createStatement();
			System.out.println(createStatement);
			sql.execute(createStatement);
			// Need to create a new SqliteTable object so we can play with the table data, and return it.
			// TODO: sqliteJDBC 2. Make this return a newly created SqliteTable Object
			return null;
		} catch (SQLException e) {
			throw new SqliteException("Problem with CREATE TABLE command.");
		}
	}

	public Object alterTable(String alterTableSql) throws SqliteException {
		Statement sql;
		try {
			sql = database.getConnection().createStatement();
			sql.execute(alterTableSql);
			System.out.println("SqliteSchema.alterTable: "+alterTableSql);
			// Need to create a new SqliteTable object se we can pass the table back to the calling function
			//TODO: sqliteJDBC 3. Make a SqliteTable object after execution, and return SqliteTable object
			return null;
		} catch (SQLException e) {
			throw new SqliteException("Problem with Alter Table command.");
		}
	}

	public SqliteTableDef getTable(String tableName) throws SqliteException {
		return tableDefs.get(tableName);
	}
}
