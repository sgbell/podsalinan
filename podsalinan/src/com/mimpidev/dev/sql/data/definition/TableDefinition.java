/**
 * 
 */
package com.mimpidev.dev.sql.data.definition;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import com.mimpidev.dev.debug.Log;
import com.mimpidev.dev.sql.SqlException;
import com.mimpidev.dev.sql.TableView;
import com.mimpidev.dev.sql.data.definition.field.FieldDetails;

/**
 * @author sbell
 *
 */
public abstract class TableDefinition extends TableView{

	public TableDefinition(){
		
	}
	
	public TableDefinition(SqlJetDb newDb, Map<String, String> newColumnList,
			String tableName, Log debugLog) {
		super(newDb, newColumnList, tableName, debugLog);
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
		Iterator<Entry<String, FieldDetails>> it = columnDetails.entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<String, FieldDetails> pair = (Map.Entry<String, FieldDetails>)it.next();
			try {
				getColumnList().put(pair.getKey(), pair.getValue().getDbFieldType());
			} catch (DataDefinitionException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setdbTable(SqlJetDb dbConnection, Log log) {
	}
	
	public void setdbTable(SqlJetDb dbConnection){
	}
	
	/**
	 * 
	 * @return
	 */
	protected ArrayList<Map<String,String>> readFromTable(){
		ArrayList<Map<String,String>> recordSet = new ArrayList<Map<String,String>>();
		try {
			ISqlJetCursor currentRecord = selectAll();
			while (!currentRecord.eof()){
				Map<String, String> newRecord = new HashMap<String,String>();
				
				recordSet.add(newRecord);
				currentRecord.next();
			}
		} catch (SqlException e) {
			e.printStackTrace();
		} catch (SqlJetException e) {
			e.printStackTrace();
		} finally {
			try {
				dbCommit();
			} catch (SqlJetException e) {
				e.printStackTrace();
			}
		}
		return recordSet;
	}
	
	/**
	 * @return 
	 * 
	 */
	public boolean purgeTable(){
        try {
			super.purgeTable();
			return true;
		} catch (SqlException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public File getDbFile(){
		return getTable().getDataBase().getFile();
	}
}
