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
public interface SqliteRunnableWithLock {

	public Object runWithLock(Database db) throws SqliteException;
}
