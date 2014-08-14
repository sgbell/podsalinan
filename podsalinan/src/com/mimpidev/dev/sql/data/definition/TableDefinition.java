/**
 * 
 */
package com.mimpidev.dev.sql.data.definition;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sbell
 *
 */
public abstract class TableDefinition {

	protected String tableName = "";
	protected final Map<Integer, SqlDefinition> columnList = new HashMap<Integer, SqlDefinition>();

	
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
}
