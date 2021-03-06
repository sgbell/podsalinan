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
package com.mimpidev.dev.sql;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.mimpidev.dev.debug.Log;
import com.mimpidev.dev.sql.field.FieldDetails;
import com.mimpidev.sql.sqlitejdbc.Database;
import com.mimpidev.sql.sqlitejdbc.cursors.SqliteCursor;
import com.mimpidev.sql.sqlitejdbc.cursors.internal.SqliteResult;
import com.mimpidev.sql.sqlitejdbc.exceptions.SqliteException;
import com.mimpidev.sql.sqlitejdbc.schema.SqliteColumnDef;
import com.mimpidev.sql.sqlitejdbc.schema.SqliteTable;

/**
 * @author bugman
 *
 */
public class TableView {

    protected boolean debug = false;
	/**
     * The database Connection
     */
	private Database db;
	/**
	 * The list of columns in the database table
	 */
	private Map<String,String> columnList;
	private Map<String,String> constraintList;
    /**
     * The name of the table this is related to.
     */
	private String name;
	/**
	 * 
	 */
	private SqliteTable table;
	/**
	 * Status for when columns are added to the table 
	 */
	public static final int NEW_COLUMNS_ADDED = 1;
	public static final int NOTHING_CHANGED = 0;
	public static final int ERROR = -1;
	
	/**
	 * 
	 */
	public static final int ITEM_ADDED_TO_DATABASE = 1;
	/**
	 * 
	 */
	public static final int ITEM_REMOVED_FROM_DATABASE = 2;
	/**
	 * 
	 */
	public static final int ITEM_UPDATED_IN_DATABASE = 3;
	
	public TableView(){
		constraintList = new HashMap<String,String>();
	}
	
	public TableView(String databaseFile, String tableName, Log debugLog) throws ClassNotFoundException, SqliteException{
		this (new HashMap<String,String>(), tableName, debugLog);
		db = new Database(databaseFile);
		initializeTable();
	}
	
	private TableView(Map<String,String> newColumnList, String tableName, Log debugLog){
		columnList = newColumnList;
		name=tableName;
		constraintList = new HashMap<String,String>();
	}
	
	public TableView(String databaseFile, Map<String,String> newColumnList, String tableName, Log debugLog) throws ClassNotFoundException, SqliteException{
		this(newColumnList, tableName, debugLog);
		db = new Database(databaseFile);
		initializeTable();
	}
	
	public TableView(Database newDb, Map<String,String> newColumnList, String tableName, Log debugLog){
		this(newColumnList, tableName, debugLog);
		db = newDb;
		initializeTable();
	}

	public TableView(String tableName, Database newDb) {
		db = newDb;
		name = tableName;
		columnList = new HashMap<String,String>();
		constraintList = new HashMap<String,String>();
	}

	/**
     * 
     */
	public void initializeTable(){
		try {
			if (!setTable()){
				createTable();
			} else {
				checkColumns();
			}
		} catch (SqlException e) {
			e.getErrorCode();
		}
	}
	
    /**
     * 
     * @return Error status
     */
	public int checkColumns(){
		int result=0;
		Iterator<Entry<String, String>> it = columnList.entrySet().iterator();
		while (it.hasNext()){
			int newResult=0;
			Map.Entry<String,String> pairs = (Map.Entry<String,String>)it.next();
			try {
				newResult = addNewColumn((String)pairs.getKey(),(String)pairs.getValue());
			} catch (SqlException e) {
	        	if (Log.isDebug()) Log.logInfo("[Table:"+name+"] Error Adding Column:"+(String)pairs.getKey());
	        	newResult = -1;
			}
			if (result>=0)
				result=newResult;
		}
		
		return result;
	}

	public void createColumnList(String[] columnNames, String[] columnTypes){
		for (int count=0; count<columnNames.length; count++){
			try {
				if (columnList==null)
					columnList=new HashMap<String,String>();
				getColumnList().put(columnNames[count], columnTypes[count]);
			} catch (DataDefinitionException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void createColumnList(Map<String,FieldDetails> columnDetails){
		Iterator<Entry<String, FieldDetails>> it = columnDetails.entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<String, FieldDetails> pair = (Map.Entry<String, FieldDetails>)it.next();
			try {
				if (columnList==null)
					columnList=new HashMap<String,String>();
				getColumnList().put(pair.getKey(), pair.getValue().getDbFieldType());
				if (pair.getValue().isPrimaryKey()){
					constraintList.put(pair.getKey(), "PRIMARY KEY");
				}
				if (pair.getValue().isAutoIncrement()){
					constraintList.put(pair.getKey(), 
							(constraintList.get(pair.getKey())!=null?(String)constraintList.get(pair.getKey())+" AUTOINCREMENT":"AUTOINCREMENT"));
				}
			} catch (DataDefinitionException e) {
				e.printStackTrace();
			}
		}
		initializeTable();
	}
	
	/**
	 * 
	 * @param newDb
	 */
	public void setdbTable(Database newDb){
		db = newDb;
		try {
			if ((getColumnList().size()>0)&&(name!=null)&&(name.length()>0)){
				initializeTable();
			}
		} catch (DataDefinitionException e) {
			if (Log.isDebug())Log.logError(this, "Error connecting to table");
			if (Log.isDebug())Log.logError(this, e.getMessage());
			if (Log.isDebug())Log.printStackTrace(e.getStackTrace());
		}
	}
	
	/**
	 * 
	 * @param newDbFile
	 * @throws SqliteException 
	 * @throws ClassNotFoundException 
	 */
	public void setdbTable(String newDbFile) throws ClassNotFoundException, SqliteException{
		setdbTable(new Database(newDbFile));
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isDbOpen(){
		return ((db!=null)&&(db.isOpen()));
	}
	
	/**
	 * 
	 * @return
	 * @throws SqlException
	 */
	public boolean createTable() throws SqlException{
		if ((name!=null)&&
			(name.length()>1)&&
			(columnList.size()>0)){
			String sql = "CREATE TABLE IF NOT EXISTS "+name+" (";
			// Do a for loop to go through each of the columns in columnList, and add them to the sql string
			Iterator<Entry<String, String>> it = columnList.entrySet().iterator();
			while (it.hasNext()){
				Map.Entry<String,String> pairs = (Map.Entry<String,String>)it.next();
				sql+=(String)pairs.getKey()+" "+(String)pairs.getValue();
				sql+=(constraintList.get(pairs.getKey())!=null?" "+constraintList.get(pairs.getKey()):"")+",";
			}
			sql=sql.substring(0, sql.length()-1);
			sql+=");";
			if (isDbOpen()){
				try {
					db.createTable(sql);
					setTable();
					return true;
				} catch (SqliteException e) {
					if (Log.isDebug())Log.printStackTrace(e.getStackTrace());
					throw new SqlException(SqlException.CREATE_TABLE_FAILED);
				}
			}
		}
		throw new SqlException(SqlException.ERROR_DB_FAILURE);
	}
	
	/**
	 * This function is used to output a Map object of only the columns found in the currently loaded table 
	 * @param map
	 * @return
	 */
	public Map<String, Object> confirmColumns(Map<String, FieldDetails> map) {
		Map<String, Object> values = new HashMap<String,Object>();
		// The following loop checks the data passed in, and copies into the values Map, only columns
		// that exist in this table.
		Iterator<Entry<String, String>> it = columnList.entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<String,String> pairs = (Map.Entry<String,String>)it.next();
			if (map.containsKey((String)pairs.getKey())){
				values.put((String)pairs.getKey(), map.get((String)pairs.getKey()).getValue());
				
			}
		}

		return values;
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 * @throws SqlException
	 */
	public boolean insert(Map<String, FieldDetails> map) throws SqlException{
		Map<String, Object> values = confirmColumns(map);
		// always call setTable
		setTable();
		if ((isDbOpen())&&(values.size()>0)){
			try{
				table.insert(values);
				return true;
			} catch (SqliteException e) {
				if (Log.isDebug())Log.printStackTrace(e.getStackTrace());
				throw new SqlException(SqlException.FAILED_INSERT_RECORD);
			}
		} else if (!isDbOpen()){
			throw new SqlException(SqlException.ERROR_DB_FAILURE);
		} else if (values.size()==0) {
			throw new SqlException(SqlException.ERROR_INVALID_TABLE);
		}
		return false;	
	}

	/**
	 * 
	 * @param map
	 * @param condition
	 * @return
	 * @throws SqlException
	 */
	public boolean update(Map<String, FieldDetails> map, String where) throws SqlException{
		final Map<String, Object> values = confirmColumns(map);
		setTable();
		/* Update the record with the values stored in data
		 */
		if ((isDbOpen())&&(values.size()>0)){
			try{
				table.update(values, where);
			} catch (SqliteException e) {
				if (Log.isDebug())Log.printStackTrace(e.getStackTrace());
				throw new SqlException(SqlException.ERROR_UPDATING_RECORD);
			}
		} else if (!isDbOpen()){
			throw new SqlException(SqlException.ERROR_DB_FAILURE);
		} else if (values.size()==0) {
			throw new SqlException(SqlException.ERROR_INVALID_TABLE);
		}
		
		return false;
	}
	
    /**
     * 
     * @param map
     * @param condition
     * @return true if the update command has succeeded
     * @throws SqlException
     */
	public boolean update(Map<String, FieldDetails> map, Map<String, FieldDetails> condition) throws SqlException{
		final String where = createWhereClause(condition);
		return update(map,where);
	}

	/**
	 * 
	 * @param condition
	 * @return
	 */
	public boolean delete(Map<String, FieldDetails> condition) throws SqlException{
		String where = createWhereClause(condition);
		setTable();
		if ((isDbOpen())&&(where.length()>0)){
			try {
				table.delete(where);
			} catch (SqliteException e) {
				throw new SqlException(SqlException.ERROR_DELETING_RECORD);
			}
		} else if (!isDbOpen()){
			throw new SqlException(SqlException.ERROR_DB_FAILURE);
		} else if (where.length()==0) {
			throw new SqlException(SqlException.ERROR_INVALID_TABLE);
		}
		return false;
	}

	/**
	 * 
	 * @param columnName
	 * @param columnType
	 * @return
	 * @throws SqlException
	 */
	public int addNewColumn(String columnName, String columnType) throws SqlException{
		boolean columnFound=false;
		List<SqliteColumnDef> columns = null;

		if (isDbOpen()){
			try {
				columns = db.getSchema().getTable(name).getColumns();
			} catch (SqliteException e) {
				e.printStackTrace();
				if (Log.isDebug())Log.printStackTrace(e.getStackTrace());
				throw new SqlException(SqlException.ERROR_READING_TABLE_SCHEMA);
			}
			if (columns!=null){
				for (SqliteColumnDef column : columns){
					if (column.getName().equalsIgnoreCase(columnName))
						columnFound=true;
				}
			} else {
				return ERROR;
			}
			
			if (!columnFound){
				try {
					db.alterTable("ALTER TABLE "+name+" ADD COLUMN "+columnName+" "+columnType.toUpperCase()+";");
					if (Log.isDebug())Log.logInfo("[Table:"+name+"] Added Column:"+columnName);
					return NEW_COLUMNS_ADDED;
				} catch (SqliteException e) {
					if (Log.isDebug())Log.printStackTrace(e.getStackTrace());
					throw new SqlException(SqlException.FAILED_ADDING_NEW_COLUMN);
				}
			}
		} else if (!isDbOpen()){
			throw new SqlException(SqlException.ERROR_DB_FAILURE);
		}
		return NOTHING_CHANGED;
	}

	/**
	 * 
	 * @return
	 * @throws SqlException
	 */
	public SqliteCursor selectAll() throws SqlException{
		if (isDbOpen()){
			SqliteCursor currentLine=null;
			try {
				currentLine = table.order(table.getDefinition().getPrimaryKeyName());
			} catch (SqliteException e) {
				if (Log.isDebug())Log.printStackTrace(e.getStackTrace());
				throw new SqlException(SqlException.FAILED_READING_RECORDS);
			}
			return currentLine;
		}
		throw new SqlException(SqlException.ERROR_DB_FAILURE);
	}
	
	public SqliteCursor selectByField(Map<String, FieldDetails> conditions) throws SqlException{
		if (isDbOpen()){
			try {
				return findItemsWithCondition(conditions);
			} catch (SqlException e) {
				if (Log.isDebug())Log.printStackTrace(e.getStackTrace());
				throw new SqlException(SqlException.FAILED_READING_RECORDS);
			}
		} else if (!isDbOpen()){
			throw new SqlException(SqlException.ERROR_DB_FAILURE);
		}
		
		return null;
	}
	
	private SqliteCursor findItemsWithCondition(Map<String, FieldDetails> conditions) throws SqlException{
		debug=true;
		String where = createWhereClause(conditions);
		if ((isDbOpen())&&(where.length()>0)){
			try {
				if (debug){
					if (Log.isDebug())Log.logMap(conditions);
				}
				SqliteCursor recordResults = table.lookupByWhere(where);
				return recordResults;
			} catch (SqliteException e) {
				throw new SqlException(SqlException.FAILED_READING_RECORDS);
			}
		} else if (!isDbOpen()){
			throw new SqlException(SqlException.ERROR_DB_FAILURE);
		} else if (where.length()==0) {
			if (Log.isDebug())Log.logMap(conditions);
			if (Log.isDebug())Log.logInfo(this, "validated values: 0");
			throw new SqlException(SqlException.ERROR_INVALID_TABLE);
		}
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String createWhereClause(
			Map conditions) {
		String where="";
		conditions=confirmColumns(conditions);
		Set<String> fieldNames = conditions.keySet();
		for (String field : fieldNames){
			if (where.length()>0)
				where+=" AND ";
			where+=field+"=";
			try {
				Double.parseDouble((String)conditions.get(field));
				where+=conditions.get(field);
			}catch (NumberFormatException e){
				where+="'"+conditions.get(field).toString().replaceAll("'", "&apos;")+"'";
			}
		}
		return where;
	}

	/**
	 * @return the name
	 */
	public String getTableName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setTableName(String name) {
		this.name = name;
	}

	/**
	 * @return the table
	 */
	public SqliteTable getTable() {
		return table;
	}

	/**
	 * @param table the table to set
	 */
	private boolean setTable() throws SqlException{
		if (table==null){
			try {
				table = (SqliteTable)db.getTable(name);
				return true;
			} catch (SqliteException e) {
				return false;
			}
		} else if (table!=null){
			return true;
		} else {
		    return false;
		}
	}
	
	/*TODO: I need to change the layout of the Table. remove the id field, and make sure we just have the text fields
	 * that way we don't need to reset the table index. 
	 */
	public boolean purgeTable() throws SqlException{
		if (table!=null){
			try {
				table.clear();
			} catch (SqliteException e) {
				e.printStackTrace();
				//log.printStackTrace(e.getStackTrace());
				//throw new SqlException(SqlException.ERROR_PURGE_TABLE);
			}
		}
		
		return true;
	}

	public Map<String, String> getColumnList() throws DataDefinitionException {
		if (columnList!=null)
			return columnList;
		else
			throw new DataDefinitionException("Column List is not set");
	}
	
	/**
	 * 
	 * @return
	 */
	protected ArrayList<Map<String,String>> readFromTable(){
		ArrayList<Map<String,String>> recordSet = new ArrayList<Map<String,String>>();
		try {
			SqliteCursor records = selectAll();
			for (SqliteResult row : records.getResults()){
				Map<String, String> newRecord = new HashMap<String,String>();
				for (SqliteColumnDef column: table.getDefinition().getColumns()){
					newRecord.put(column.getName(), row.getValues().get(column.getName()));
				}				
				recordSet.add(newRecord);
			}
		} catch (SqlException e) {
			e.printStackTrace();
		} catch (SqliteException e) {
			e.printStackTrace();
		}
		return recordSet;
	}

	public File getDbFile(){
		return db.getFile();
	}
	
	public boolean isDebug(){
		return debug;
	}
}
