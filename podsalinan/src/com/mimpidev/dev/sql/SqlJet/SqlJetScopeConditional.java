/**
 * 
 */
package com.mimpidev.dev.sql.SqlJet;

import java.util.Map;

import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.internal.table.ISqlJetBtreeDataTable;
import org.tmatesoft.sqljet.core.internal.table.SqlJetTableDataCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.ISqlJetTransaction;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import com.mimpidev.dev.sql.field.FieldCondition;

/**
 * @author sbell
 *
 */
public class SqlJetScopeConditional extends SqlJetTableDataCursor implements ISqlJetCursor {

	
	Map<String,FieldCondition> conditions;
	/**
	 * @param table
	 * @param db
	 * @throws SqlJetException
	 */
	public SqlJetScopeConditional(ISqlJetBtreeDataTable table, SqlJetDb db, Map<String,FieldCondition> conditions)
			throws SqlJetException {
		super(table, db);
		this.conditions=conditions;
		first();
	}

	/**
	 * 
	 * @param table
	 * @param db
	 * @param values
	 */
	public SqlJetScopeConditional(ISqlJetTable table, SqlJetDb db,
			Map<String, FieldCondition> conditions) {
		//TODO:need to do the correct creation here
		this(new ISqlJetBtreeDataTable(), db, conditions);
	}

	/**
	 * 
	 */
	public boolean first() throws SqlJetException {
		return (Boolean) db.runReadTransaction(new ISqlJetTransaction(){

			@Override
			public Object run(SqlJetDb db) throws SqlJetException {
				btreeTable.first();
				//TODO: need to search the db cursor for the first value that matches conditions, so it is returned
				
				return null;
			}
		});
	}

	/**
	 * 
	 * @return
	 */
	public ISqlJetCursor getCursor() {
		// TODO Auto-generated method stub
		return null;
	}
}

/**
 * Current map of path SqlJet uses to traverse the db (in summary anyway)
 * 
 * org.tmatesoft.sqljet.core.internal.table.SqlJetTable = SqlJetDb->getTable(String tableName)
 *
 * ISqlJetCursor SqlJetTable.scope(indexName,firstKey,LastKey){
 *     SqlJetBtreeDataTable table = new SqlJetBtreeDataTable(btree, tableName, write);
 *     org.tmatesoft.sqljet.core.internal.table.SqlJetIndexScopeCursor(table, db, indexName, scope){
 *        org.tmatesoft.sqljet.core.internal.table.SqlJetIndexOrderCursor(table, db, index){
 *            org.tmatesoft.sqljet.core.internal.table.SqlJetTableDataCursor(table, db){
 *            
 *            }
 *            if (indexName is null) indexName=table.getPrimaryKeyIndex();
 *            
 *        }
 *     }
 *   }
 */
