/**
 * 
 */
package com.mimpidev.podsalinan.db.tables;

import com.mimpidev.dev.sql.data.definition.SqlDefinition;

/**
 * @author sbell
 *
 */
public class Downloads extends TableDefinition {

	
	public Downloads(){
		tableName = "downloads";
		columnList.put(0, new SqlDefinition("id","INTEGER PRIMARY KEY AUTOINCREMENT"));
		columnList.put(1, new SqlDefinition("url","TEXT"));
		columnList.put(2,new SqlDefinition("size","TEXT"));
		columnList.put(3,new SqlDefinition("destination","TEXT"));
		columnList.put(4,new SqlDefinition("priority","INTGEGER"));
		columnList.put(5,new SqlDefinition("podcastSource","TEXT"));
		columnList.put(6,new SqlDefinition("status","INTEGER"));
	}
	
}
