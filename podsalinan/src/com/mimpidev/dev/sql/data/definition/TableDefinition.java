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
	protected final ArrayList<SqlDefinition> columnList = new ArrayList<SqlDefinition>();
	/**
	 * 
	 */
	protected TableView dbTable=null;
	
	public TableDefinition() {
	}
	
	public ArrayList<SqlDefinition> getColumnList(){
		return columnList;
	}
	
	public String getName(){
		return tableName;
	}
	
	public void createColumnList(String[] columnNames, String[] columnTypes){
		for (int count=0; count<columnNames.length; count++){
			columnList.add(new SqlDefinition(columnNames[count],columnTypes[count]));
		}
	}
	
	public void setdbTable(SqlJetDb dbConnection, Log log) {
		dbTable = new TableView(dbConnection,getColumnList(),tableName,log);
	}
	
	public void setdbTable(SqlJetDb dbConnection){
		dbTable = new TableView(dbConnection,getColumnList(),tableName,Podsalinan.debugLog);
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
					newRecord.put(currentColumn.name, currentRecord.getString(currentColumn.name));
				}
				recordSet.add(newRecord);
				currentRecord.next();
			}
			return recordSet;
		} catch (SqlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SqlJetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
