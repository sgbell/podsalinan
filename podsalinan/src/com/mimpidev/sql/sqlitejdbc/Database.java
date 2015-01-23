/**
 *  This class is the first class for creating a connection to the sqlite database via
 *  a jdbc connection.
 */
package com.mimpidev.sql.sqlitejdbc;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mimpidev.sql.sqlitejdbc.exceptions.SqliteException;
import com.mimpidev.sql.sqlitejdbc.internal.ISqliteEngineSynchronized;
import com.mimpidev.sql.sqlitejdbc.internal.ISqliteTransaction;
import com.mimpidev.sql.sqlitejdbc.internal.SqliteRunnableWithLock;
import com.mimpidev.sql.sqlitejdbc.schema.SqliteSchema;
import com.mimpidev.sql.sqlitejdbc.schema.SqliteTable;

/**
 * @author sbell
 *
 */
public class Database {

	private Connection connection = null;
	private SqliteSchema schema = null;
	/**
	 * 
	 */
	public Database() throws ClassNotFoundException{
        Class.forName("org.sqlite.JDBC");
        
	}

	public Database(String filename) throws ClassNotFoundException, SqliteException{
		this();
		try {
			setConnection(DriverManager.getConnection("jdbc:sqlite:"+filename));
		} catch (SQLException e) {
			throw new SqliteException("Failed to open database.");
		}
	}
	
	public Object runTransaction(ISqliteTransaction operation) throws SqliteException {
		return null;
	}
	
	public boolean checkOpen() throws SqliteException {
		try {
			return (!connection.isClosed());
		} catch (SQLException e) {
			throw new SqliteException("Error with database connection");
		}
	}
	
	public void refreshSchema() throws SqliteException {
	    if (schema==null){
	    	// If the current schema is not set, create it
	    	schema = new SqliteSchema(this);
	    } else
	    	schema.readSchema();
	}
	
	public SqliteTable alterTable(final String sql) throws SqliteException {
		checkOpen();
		return (SqliteTable) runTransaction(new ISqliteTransaction(){

			@Override
			public Object run(Database db) throws SqliteException {
				return getSchema().alterTable(sql);
			}
			
		});
	}
	
	public SqliteTable createTable(final String sql) throws SqliteException {
		checkOpen();
		return (SqliteTable) runTransaction(new ISqliteTransaction(){
			@Override
			public Object run(Database db) throws SqliteException {
				return getSchema().createTable(sql);
			}
		});
	}
	
	public boolean dropTable(final String tableName) throws SqliteException {
		checkOpen();
		return (Boolean) runTransaction(new ISqliteTransaction(){
			@Override
			public Object run(Database db) throws SqliteException {
				getSchema().dropTable(tableName);
				refreshSchema();
				return Boolean.TRUE;
			}
		});
	}
   
    /**
     * 
     * @param tableName
     * @return
     * @throws SqliteException
     */
	public SqliteTable getTable(final String tableName) throws SqliteException {
		checkOpen();
		refreshSchema();
		return (SqliteTable) runWithLock(new SqliteRunnableWithLock(){
			@Override
			public Object runWithLock(Database db) throws SqliteException {
				return new SqliteTable(db, tableName);
			}
		});
	}
	
	public Object runWithLock(final SqliteRunnableWithLock op) throws SqliteException{
		return runSynchronized(new ISqliteEngineSynchronized(){
			public Object runSynchronized(Database db) throws SqliteException {
				return op.runWithLock(Database.this);
			}
		});
	}
	
	public Object runSynchronized(
			ISqliteEngineSynchronized op) throws SqliteException{
		checkOpen();
		try {
			return op.runSynchronized(this);
		} finally {
			
		}
	}

	public SqliteSchema getSchema() throws SqliteException {
		refreshSchema();
		return schema;
	}

	/**
	 * @return the connection
	 */
	public Connection getConnection() {
		//TODO: check if connection object can be used for a lock
		return connection;
	}

	/**
	 * @param connection the connection to set
	 */
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public boolean isOpen() {
		// TODO Auto-generated method stub
		return false;
	}

	public File getFile() {
		// TODO Auto-generated method stub
		return null;
	}
}
