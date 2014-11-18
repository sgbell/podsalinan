/**
 * 
 */
package com.mimpidev.dev.sql.SqlJet;

import java.util.Map;

import org.tmatesoft.sqljet.core.SqlJetErrorCode;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.internal.table.ISqlJetBtreeDataTable;
import org.tmatesoft.sqljet.core.internal.table.SqlJetTableDataCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.ISqlJetTransaction;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import com.mimpidev.dev.sql.field.condition.FieldCondition;

/**
 * @author sbell
 *
 */
public class SqlJetScopeConditional extends SqlJetTableDataCursor implements ISqlJetCursor {

	
	Map<String,FieldCondition> conditions;
	private long rowsCount;
    private long currentRowNum;
    private long currentRowId;
    private boolean internalMove;
    
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
	
	public long getRowCount() throws SqlJetException {
		if (rowsCount <0) {
			computeRows(false);
		}
		
		return rowsCount;
	}
	
    /**
     * @throws SqlJetException
     */
    protected void computeRows(final boolean current) throws SqlJetException {
        db.runReadTransaction(new ISqlJetTransaction() {
            public Object run(SqlJetDb db) throws SqlJetException {
                try {

                    internalMove = true;

                    currentRowId = getRowIdSafe();
                    rowsCount = 0;
                    currentRowNum = -1;

                    for (first(); !eof(); next()) {
                    	if (equalsConditions())
                           rowsCount++;
                        if (currentRowId == getRowIdSafe()) {
                            currentRowNum = rowsCount;
                            if (current) {
                                break;
                            }
                        }
                    }

                    if (currentRowNum < 0) {
                        currentRowNum = rowsCount;
                    }

                    if (currentRowId > 0) {
                        goTo(currentRowId);
                    }

                } finally {
                    internalMove = false;
                }
                return null;
            }
        });
    }
    
    private long getRowIdSafe() throws SqlJetException {
        return super.eof() ? 0 : getRowId();
    }
    
    /**
     * This function will test to see if the current record meets the requested conditions
     * @return True if the record matches the conditions
     * @throws SqlJetException
     */
    private boolean equalsConditions() throws SqlJetException{
    	if (conditions.size()>0){
        	return (Boolean) db.runReadTransaction(new ISqlJetTransaction() {
    			public Object run(SqlJetDb db) throws SqlJetException {
    				/*TODO: Working here, need to travel through the map of conditions to see if
    				 *      the current record matches the conditions (initially we will make all fields
    				 *      "LIKE") Need to tally a score, and if it meets conditions, return a true
    				 */
    	    		return getBtreeDataTable().getString(getFieldSafe("test")).equals("test");
    			}
        	});
    	} else
    	   return false;
    }
    
    private int getFieldSafe(String fieldName) throws SqlJetException {
        final ISqlJetBtreeDataTable table = getBtreeDataTable();
        if (eof()) {
            throw new SqlJetException(SqlJetErrorCode.MISUSE,
                    "Table is empty or the current record doesn't point to a data row");
        }
        if (fieldName == null) {
            throw new SqlJetException(SqlJetErrorCode.MISUSE, "Field name is null");
        }
        final int field = table.getDefinition().getColumnNumber(fieldName);
        if (field < 0) {
            throw new SqlJetException(SqlJetErrorCode.MISUSE, "Field not found: " + fieldName);
        }
        return field;
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
