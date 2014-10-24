/**
 * 
 */
package com.mimpidev.dev.sql.data.definition;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import com.mimpidev.dev.debug.Log;
import com.mimpidev.dev.sql.SqlException;
import com.mimpidev.dev.sql.TableView;
import com.mimpidev.dev.sql.data.definition.field.FieldDetails;
import com.mimpidev.podsalinan.Podsalinan;

/**
 * @author sbell
 *
 */
public abstract class TableDefinition {

	/**
	 * 
	 */
	protected String tableName = "";
	/**
	 * 
	 */
	protected TableView dbTable=null;
	
	public TableDefinition() {
	}
	
	public Map<String,String> getColumnList() throws DataDefinitionException{
		if (dbTable!=null)
			return dbTable.getColumnList();
		else
			throw new DataDefinitionException("TableView not set");
	}
	
	public String getName(){
		return tableName;
	}
	
	public void createColumnList(String[] columnNames, String[] columnTypes){
		for (int count=0; count<columnNames.length; count++){
			try {
				getColumnList().put(columnNames[count], columnTypes[count]);
			} catch (DataDefinitionException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void createColumnList(Map<String,FieldDetails> columnDetails){
		//TODO: traverse columnDetails map and set columnList 
	}
	
	public void setdbTable(SqlJetDb dbConnection, Log log) {
		// Need to re-arrange the startup so that we dont have to store the columns more than once.
		dbTable = new TableView(dbConnection,tableName,log);
	}
	
	public void setdbTable(SqlJetDb dbConnection){
		dbTable = new TableView(dbConnection,tableName,Podsalinan.debugLog);
	}
	
	/**
	 * 
	 * @return
	 */
	protected ArrayList<Map<String,String>> readFromTable(){
		ArrayList<Map<String,String>> recordSet = new ArrayList<Map<String,String>>();
		try {
			ISqlJetCursor currentRecord = dbTable.selectAll();
			while (!currentRecord.eof()){
				Map<String, String> newRecord = new HashMap<String,String>();
				for (SqlDefinition currentColumn: columnList){
					if (currentRecord.getString(currentColumn.name)!=null)
						newRecord.put(currentColumn.name, currentRecord.getString(currentColumn.name));
					else
						newRecord.put(currentColumn.name, "");
				}
				recordSet.add(newRecord);
				currentRecord.next();
			}
		} catch (SqlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SqlJetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				dbTable.dbCommit();
			} catch (SqlJetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return recordSet;
	}
	
	/**
	 * 
	 */
	public void purgeTable(){
        try {
			dbTable.purgeTable();
		} catch (SqlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public File getDbFile(){
		return dbTable.getTable().getDataBase().getFile();
	}
}
