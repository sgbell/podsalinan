/**
 * 
 */
package com.mimpidev.podsalinan.db.tables;

import com.mimpidev.dev.sql.data.definition.SqlDefinition;
import com.mimpidev.dev.sql.data.definition.TableDefinition;

/**
 * @author sbell
 *
 */
public class Shows extends TableDefinition {

	/**
	 * 
	 */
	public Shows() {
		tableName = "shows";
		columnList.put(0, new SqlDefinition("id","INTEGER PRIMARY KEY AUTOINCREMENT"));
		columnList.put(1, new SqlDefinition("published","TEXT"));
		columnList.put(2,new SqlDefinition("title","TEXT"));
		columnList.put(3,new SqlDefinition("url","TEXT"));
		columnList.put(4,new SqlDefinition("size","INTEGER"));
		columnList.put(5,new SqlDefinition("directory","TEXT"));
		columnList.put(6,new SqlDefinition("status","INTEGER"));
	}

}
