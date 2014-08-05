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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.schema.ISqlJetColumnDef;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import com.mimpidev.dev.debug.Log;
import com.mimpidev.dev.sql.data.definition.BaseColumn;
import com.mimpidev.dev.sql.data.definition.SqlDefinition;

/**
 * @author bugman
 *
 */
public class TableView {

    /**
     * The database Connection
     */
	private SqlJetDb db;
	/**
	 * The list of columns in the database table
	 */
	private Map<Integer,SqlDefinition> columnList;
	/**
	 * The log.
	 */
	private Log log;
    /**
     * The name of the table this is related to.
     */
	private String name;
	/**
	 * 
	 */
	private ISqlJetTable table;
	/**
	 * Status for when columns are added to the table 
	 */
	public static final int NEW_COLUMNS_ADDED = 1;
	public static final int NOTHING_CHANGED = 0;
	public static final int ERROR = -1;
	
	public TableView(File databaseFile, String tableName, Log debugLog){
		this (new HashMap<Integer,SqlDefinition>(), tableName, debugLog);
		db = new SqlJetDb(databaseFile,true);
		if (!setTable()){
			try {
				createTable();
			} catch (SqlException e) {
				e.getErrorCode();
			}
		} else {
			checkColumns();
		}
	}
	
	private TableView(HashMap<Integer,SqlDefinition> newColumnList, String tableName, Log debugLog){
		columnList = newColumnList;
		log = debugLog;
		name=tableName;
	}
	
	public TableView(File databaseFile, HashMap<Integer,SqlDefinition> newColumnList, String tableName, Log debugLog){
		this(newColumnList, tableName, debugLog);
		db = new SqlJetDb(databaseFile,true);
		if (!setTable()){
			try {
				createTable();
			} catch (SqlException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			checkColumns();
		}
	}
	
	public TableView(SqlJetDb newDb, HashMap<Integer, SqlDefinition> newColumnList, String tableName, Log debugLog){
		this(newColumnList, tableName, debugLog);
		db = newDb;
		if (!setTable()){
			try {
				createTable();
			} catch (SqlException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			checkColumns();
		}
	}
	
    /**
     * 
     * @return Error status
     */
	public int checkColumns(){
		int result=0;
		for (int cc=0; cc<columnList.size(); cc++){
			int newResult = addNewColumn(columnList.get(cc).name,columnList.get(cc).type);
			if (result!=ERROR){
				result=newResult;
			}
	        if (newResult==ERROR){
	        	log.println("[Table:"+name+"] Error Adding Column:"+columnList.get(cc).name);
	        }
		}
		
		return result;
	}
	
	public boolean isDbOpen(){
		return ((db!=null)&&(db.isOpen()));
	}
	
	public boolean createTable() throws SqlException{
		if ((name!=null)&&
			(name.length()>1)&&
			(columnList.size()>0)){
			String sql = "CREATE TABLE IF NOT EXISTS "+name+" (";
			// Do a for loop to go through each of the columns in columnList, and add them to the sql string
			for (int cc=0; cc<columnList.size(); cc++){
				sql.concat(columnList.get(cc).name+" "+columnList.get(cc).type);
			}
			
			sql.concat(");");
			if (isDbOpen()){
				try {
					db.beginTransaction(SqlJetTransactionMode.WRITE);
					db.createTable(sql);
					db.commit();
					return true;
				} catch (SqlJetException e) {
					log.printStackTrace(e.getStackTrace());
					throw new SqlException(SqlException.CREATE_TABLE_FAILED);
				}
			}
			setTable();
		}
		return false;
	}
	
	public boolean insert(Map<String,BaseColumn> data){
		Map<String, Object> values = new HashMap<String,Object>();
		try {
			db.beginTransaction(SqlJetTransactionMode.WRITE);
		} catch (SqlJetException e) {
			log.printStackTrace(e.getStackTrace());
		}
		if (table==null){
			setTable();
		}
		for (int cc=0; cc<columnList.size(); cc++){
			if (data.containsKey(columnList.get(cc))){
				values.put(columnList.get(cc).name, data.get(columnList.get(cc)));
			}
		}
		if (values.size()>0)
			try {
				table.insertByFieldNames(values);
				db.close();
				return true;
			} catch (SqlJetException e) {
				log.printStackTrace(e.getStackTrace());
			}
		return false;
	}
	
	public boolean update(){
		
		
		return false;
	}
	
	public int addNewColumn(String columnName, String columnType){
		boolean columnFound=false;
		List<ISqlJetColumnDef> columns = null;

		if (isDbOpen()){
			try {
				db.beginTransaction(SqlJetTransactionMode.WRITE);
			} catch (SqlJetException e) {
				log.printStackTrace(e.getStackTrace());
			}
			
			try {
				columns = db.getSchema().getTable(name).getColumns();
			} catch (SqlJetException e) {
				log.printStackTrace(e.getStackTrace());
			}
			if (columns!=null){
				for (ISqlJetColumnDef column : columns){
					if (column.getName().contentEquals(columnName))
						columnFound=true;
				}
			} else {
				return ERROR;
			}
			
			if (!columnFound){
				try {
					db.alterTable("ALTER TABLE "+name+" ADD COLUMN "+columnName+" "+columnType.toUpperCase()+";");
					log.println("[Table:"+name+"] Added Column:"+columnName);
					return NEW_COLUMNS_ADDED;
				} catch (SqlJetException e) {
					log.printStackTrace(e.getStackTrace());
				}
			}
		}
		return NOTHING_CHANGED;
	}

	public ISqlJetCursor selectAll(){
		ISqlJetTable table = null;
		
		if (isDbOpen()){
			try {
				db.beginTransaction(SqlJetTransactionMode.READ_ONLY);
				ISqlJetCursor currentLine = table.order(table.getPrimaryKeyIndexName());
				return currentLine;
			} catch (SqlJetException e) {
				log.printStackTrace(e.getStackTrace());
			}
		}
		return null;
	}
	
	public String getTableName (){
		return name;
	}
	
	/**
	 * @return the table
	 */
	public ISqlJetTable getTable() {
		return table;
	}

	/**
	 * @param table the table to set
	 */
	private boolean setTable() {
		try {
			table = db.getTable(name);
		} catch (SqlJetException e) {
			log.printStackTrace(e.getStackTrace());
			return false;
		}
		return true;
	}
}
