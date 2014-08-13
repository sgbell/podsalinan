/**
 * 
 */
package com.mimpidev.podsalinan.db.tables;

import com.mimpidev.dev.sql.data.definition.SqlDefinition;

/**
 * @author sbell
 *
 */
public class Podcasts extends TableDefinition {

	public Podcasts() {
		tableName = "podcasts";
		columnList.put(0, new SqlDefinition("id","INTEGER PRIMARY KEY AUTOINCREMENT"));
		columnList.put(1, new SqlDefinition("name","TEXT"));
		columnList.put(2,new SqlDefinition("localfile","TEXT"));
		columnList.put(3,new SqlDefinition("url","TEXT"));
		columnList.put(4,new SqlDefinition("directory","TEXT"));
		columnList.put(5,new SqlDefinition("auto_queue","INTEGER"));
	}
}
