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
public class Downloads {

	private final String tableName = "downloads";
	private final Map<Integer, SqlDefinition> columnList = new HashMap<Integer, SqlDefinition>();

	
	
	public Downloads(){
		columnList.put(0, new SqlDefinition("id","INTEGER PRIMARY KEY AUTOINCREMENT"));
		columnList.put(1, new SqlDefinition("url","TEXT"));
		columnList.put(2,new SqlDefinition("size","TEXT"));
		columnList.put(3,new SqlDefinition("destination","TEXT"));
		columnList.put(4,new SqlDefinition("priority","INTGEGER"));
		columnList.put(5,new SqlDefinition("podcastSource","TEXT"));
		columnList.put(6,new SqlDefinition("status","INTEGER"));
	}
	
}
