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
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import com.mimpidev.dev.debug.Log;

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
	private Map<Integer,TableColumn> columnList;
	/**
	 * The log.
	 */
	private Log log;
    /**
     * The name of the table this is related to.
     */
	private String name;
	
	public TableView(File databaseFile){
		db = new SqlJetDb(databaseFile,true);
		columnList = new HashMap<Integer,TableColumn>();
	}
	
	public TableView(File databaseFile, HashMap<Integer,TableColumn> newColumnList, Log debugLog){
		this(databaseFile);
		columnList=newColumnList;
		log=debugLog;
	}
	
	public TableView(SqlJetDb newDb, HashMap<Integer,TableColumn> newColumnList){
		db = newDb;
		columnList = newColumnList;
	}
	
	public boolean isDbOpen(){
		return ((db!=null)&&(db.isOpen()));
	}
	
	public boolean createTable(){
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
				}
			}
		}
		return false;
	}
	
	public boolean insert(){
		
		return false;
	}
	
	public boolean addNewColumn(String columnName, String columnType){
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
				return false;
			}
			
			if (!columnFound){
				try {
					db.alterTable("ALTER TABLE "+name+" ADD COLUMN "+columnName+" "+columnType.toUpperCase()+";");
					return true;
				} catch (SqlJetException e) {
					log.printStackTrace(e.getStackTrace());
				}
			}
		}
		return false;
	}

	public boolean selectAll(){
		
		if (isDbOpen()){
			try {
				db.beginTransaction(SqlJetTransactionMode.READ_ONLY);
				
			} catch (SqlJetException e) {
				log.printStackTrace(e.getStackTrace());
			}
			
		}
		
		return false;
	}
	
	public class TableColumn {
		public String name;
		public String type;
	}
	
}
