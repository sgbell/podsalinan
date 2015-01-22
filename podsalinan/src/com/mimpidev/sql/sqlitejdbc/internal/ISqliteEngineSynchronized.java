/**
 * 
 */
package com.mimpidev.sql.sqlitejdbc.internal;

import com.mimpidev.sql.sqlitejdbc.Database;
import com.mimpidev.sql.sqlitejdbc.exceptions.SqliteException;

/**
 * @author sbell
 *
 */
public interface ISqliteEngineSynchronized {

	public Object runSynchronized(Database db) throws SqliteException;
}
