/**
 * 
 */
package com.mimpidev.podsalinan.db.tables;

import java.util.HashMap;
import java.util.Map;

import com.mimpidev.dev.sql.data.definition.SqlDefinition;

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
}
