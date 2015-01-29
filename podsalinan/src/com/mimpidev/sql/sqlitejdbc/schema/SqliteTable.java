/**
 * 
 */
package com.mimpidev.sql.sqlitejdbc.schema;

import java.util.HashMap;
import java.util.Map;
import com.mimpidev.sql.sqlitejdbc.Database;
import com.mimpidev.sql.sqlitejdbc.SqliteDataTable;
import com.mimpidev.sql.sqlitejdbc.cursors.SqliteCursor;
import com.mimpidev.sql.sqlitejdbc.cursors.SqliteIndexScopeCursor;
import com.mimpidev.sql.sqlitejdbc.cursors.SqliteOrderCursor;
import com.mimpidev.sql.sqlitejdbc.cursors.SqliteScopeConditional;
import com.mimpidev.sql.sqlitejdbc.cursors.SqliteTableDataCursor;
import com.mimpidev.sql.sqlitejdbc.exceptions.SqliteException;
import com.mimpidev.sql.sqlitejdbc.internal.SqliteRunnableWithLock;

/**
 * @author sbell
 *
 */
public class SqliteTable {

	/**
	 * 
	 */
	private String name;
	/**
	 * 
	 */
	private final Database db;
    
	/**
	 * 
	 * @param database
	 * @param tableName
	 * @throws SqliteException
	 */
	public SqliteTable(Database database, String tableName) throws SqliteException {
		db = database;
		setName(tableName);
		if (tableName.length()==0)
			throw new SqliteException("No table name defined");
		if (getDefinition() == null)
			throw new SqliteException("Table not found: " + tableName);
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
	private void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the db
	 */
	public Database getDatabase() {
		return db;
	}
	
	/**
	 * 
	 * @return
	 * @throws SqliteException
	 */
	public String getPrimaryKeyName() throws SqliteException {
		final SqliteTableDef definition = getDefinition();
		return definition.getPrimaryKeyName();
	}

	/**
	 * 
	 * @return
	 * @throws SqliteException
	 */
	public SqliteTableDef getDefinition() throws SqliteException {
		return db.getSchema().getTable(getName());
	}
	
	/**
	 * 
	 * @return
	 * @throws SqliteException
	 */
	public SqliteCursor open() throws SqliteException {
		return (SqliteCursor) db.runWithLock(new SqliteRunnableWithLock() {

			@Override
			public Object runWithLock(Database db) throws SqliteException {
				return new SqliteTableDataCursor(new SqliteDataTable(db,getName()), db);
			}
			
		});
	}
	
	/**
	 * 
	 * @param orderby
	 * @return
	 * @throws SqliteException
	 */
	public SqliteCursor order(final String orderby) throws SqliteException{
		return (SqliteCursor) db.runWithLock(new SqliteRunnableWithLock(){
			@Override
			public Object runWithLock(Database db) throws SqliteException {
				final SqliteDataTable table = new SqliteDataTable(db,getName());
				return new SqliteOrderCursor(db,table,orderby);
			}
		});
	}
	
	/**
	 * 
	 * @param fieldName
	 * @param value
	 * @return
	 * @throws SqliteException
	 */
	public SqliteCursor lookup(final String fieldName, final Object value) throws SqliteException {
		return (SqliteCursor) db.runWithLock(new SqliteRunnableWithLock(){

			@Override
			public Object runWithLock(Database db) throws SqliteException {
				final SqliteDataTable table = new SqliteDataTable(db,getName());
				return new SqliteIndexScopeCursor(table,db,fieldName,value);
			}
		});
	}
	
	/**
	 * 
	 * @param conditions
	 * @return
	 * @throws SqliteException
	 */
	public SqliteCursor lookupByWhere(final String conditions) throws SqliteException {
		return (SqliteCursor) db.runWithLock(new SqliteRunnableWithLock() {
			@Override
			public Object runWithLock(Database db) throws SqliteException {
				final SqliteDataTable table = new SqliteDataTable(db,getName());
				return new SqliteScopeConditional(table,db,conditions);
			}
		});
	}
	
	/**
	 * 
	 * @param whereClause
	 * @return
	 * @throws SqliteException
	 */
	public SqliteCursor lookupByWhere(final Map whereClause) throws SqliteException{
		String conditions="";
		for (Object key : whereClause.keySet()){
			if (conditions.length()>0)
				conditions+=" AND ";
			conditions+=(String)key+" LIKE '%"+(String)whereClause.get(key)+"%'";
		}
		return lookupByWhere(conditions);
	}
	
	/**
	 * 
	 * @param values
	 * @return
	 * @throws SqliteException
	 */
	public long insert(final Map<String, Object> values) throws SqliteException {
		return insertOr(null, values);
	}

	/**
	 * 
	 * @param onConflict
	 * @param values
	 * @return
	 * @throws SqliteException
	 */
	public long insertOr(final SqliteConflictAction onConflict, final Map<String, Object> values) throws SqliteException {
		return (Long) db.runWithLock(new SqliteRunnableWithLock(){

			@Override
			public Object runWithLock(Database db) throws SqliteException {
				final SqliteDataTable dataTable = new SqliteDataTable(db,getName());
				return dataTable.insert(null, values);
			}
		});
	}
	
	/**
	 * 
	 * @return
	 * @throws SqliteException
	 */
	public boolean clear() throws SqliteException {
		return (Boolean) db.runWithLock(new SqliteRunnableWithLock(){

			@Override
			public Object runWithLock(Database db) throws SqliteException {
				final SqliteDataTable dataTable = new SqliteDataTable(db,getName());
				return dataTable.clear();
			}
			
		});
	}
	
	/**
	 * 
	 * @param datafields
	 * @param where
	 * @return
	 * @throws SqliteException
	 */
	public Object update(final Map datafields, final String where) throws SqliteException{
		return (Long) db.runWithLock(new SqliteRunnableWithLock(){
			public Object runWithLock(Database db) throws SqliteException {
				final SqliteDataTable dataTable = new SqliteDataTable(db,getName());
				return dataTable.update(datafields,where);
			}
		});
	}
	
	/**
	 * 
	 * @param conditions
	 * @return
	 * @throws SqliteException
	 */
	public Object delete(final String where) throws SqliteException {
		return (Long) db.runWithLock(new SqliteRunnableWithLock(){
			public Object runWithLock(Database db) throws SqliteException {
				final SqliteDataTable dataTable = new SqliteDataTable(db,getName());
				return dataTable.delete(where);
			}
		});
	}
}
