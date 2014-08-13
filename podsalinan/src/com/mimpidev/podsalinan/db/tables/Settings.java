/**
 * 
 */
package com.mimpidev.podsalinan.db.tables;

import com.mimpidev.dev.sql.data.definition.SqlDefinition;

/**
 * @author sbell
 *
 */
public class Settings extends TableDefinition {

	/**
	 * 
	 */
	public Settings() {
		tableName = "settings";
		columnList.put(0, new SqlDefinition("id","INTEGER PRIMARY KEY AUTOINCREMENT"));
		columnList.put(1, new SqlDefinition("name","TEXT"));
		columnList.put(2,new SqlDefinition("value","TEXT"));
	}
}
