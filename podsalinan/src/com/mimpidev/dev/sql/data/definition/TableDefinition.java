/**
 * 
 */
package com.mimpidev.dev.sql.data.definition;

import java.util.HashMap;
import java.util.Map;

import org.tmatesoft.sqljet.core.table.SqlJetDb;

import com.mimpidev.dev.debug.Log;
import com.mimpidev.dev.sql.TableView;

/**
 * @author sbell
 *
 */
public abstract class TableDefinition {

	protected String tableName = "";
	protected final Map<Integer, SqlDefinition> columnList = new HashMap<Integer, SqlDefinition>();
	protected TableView dbTable=null;

	
	public TableDefinition() {
	}
	
	public Map<Integer, SqlDefinition> getColumnList(){
		return columnList;
	}
	
	public String getName(){
		return tableName;
	}
	
	public void createColumnList(String[] columnNames, String[] columnTypes){
		for (int count=0; count<columnNames.length; count++){
			columnList.put(count, new SqlDefinition(columnNames[count],columnTypes[count]));
		}
	}
	
	public void setdbTable(SqlJetDb dbConnection, Log log) {
		dbTable = new TableView(dbConnection,getColumnList(),tableName,log);
	}
	
	
}
