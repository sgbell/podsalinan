/*******************************************************************************
 * Copyright (c) 2014 Sam Bell.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or  any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See
 * the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Sam Bell - initial API and implementation
 ******************************************************************************/
/**
 * Going to create my own implementation of the SqlJetDb and SqlJetTable Classes, so I can get
 * searching happening in the system
 */
package com.mimpidev.dev.sql.SqlJet;

import java.io.File;
import java.io.IOException;

import org.tmatesoft.sqljet.core.SqlJetErrorCode;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.internal.ISqlJetFileSystem;
import org.tmatesoft.sqljet.core.internal.ISqlJetPager;
import org.tmatesoft.sqljet.core.internal.table.SqlJetPragmasHandler;
import org.tmatesoft.sqljet.core.schema.ISqlJetIndexDef;
import org.tmatesoft.sqljet.core.schema.ISqlJetSchema;
import org.tmatesoft.sqljet.core.schema.ISqlJetTableDef;
import org.tmatesoft.sqljet.core.schema.ISqlJetTriggerDef;
import org.tmatesoft.sqljet.core.schema.ISqlJetViewDef;
import org.tmatesoft.sqljet.core.schema.ISqlJetVirtualTableDef;
import org.tmatesoft.sqljet.core.table.ISqlJetRunnableWithLock;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.ISqlJetTransaction;
import org.tmatesoft.sqljet.core.table.SqlJetDb;
import org.tmatesoft.sqljet.core.table.engine.ISqlJetEngineSynchronized;
import org.tmatesoft.sqljet.core.table.engine.ISqlJetEngineTransaction;
import org.tmatesoft.sqljet.core.table.engine.SqlJetEngine;

/**
 *  <p>
 * Connection to database. This class currently is main entry point in SQLJet
 * API.
 * </p>
 * 
 * <p>
 * It allows to perform next tasks:
 * 
 * <ul>
 * <li>Open existed and create new SQLite database.</li>
 * <li>Get and modify database's schema.</li>
 * <li>Control transactions.</li>
 * <li>Read, search and modify data in database.</li>
 * <li>Get and set database's options.</li>
 * </ul>
 * 
 * </p>
 * 
 * @author TMate Software Ltd.
 * @author Sergey Scherbina (sergey.scherbina@gmail.com)
 * @author Dmitry Stadnik (dtrace@seznam.cz)
 * @author Mimpi Development
 * @author Sam Bell (bugmanaus@gmail.com)
 */
public class SqlJetDbWrapper extends SqlJetDb {

    /**
     * File name for in memory database.
     */
    public static final File IN_MEMORY = new File(ISqlJetPager.MEMORY_DB);
    
    private SqlJetDb temporaryDb;

    /**
     * <p>
     * Creates connection to database but not open it. Doesn't open database
     * file until not called method {@link #open()}.
     * </p>
     * 
     * <p>
     * File could be null or have special value {@link #IN_MEMORY}. If file is
     * null then will be created temporary file which will be deleted at close.
     * If file is {@link #IN_MEMORY} then file doesn't created and instead
     * database will placed in memory. If regular file is specified but doesn't
     * exist then it will be tried to created.
     * </p>
     * 
     * @param file
     *            path to data base. Could be null or {@link #IN_MEMORY}.
     * @param writable
     *            if true then will allow data modification.
     */
    public SqlJetDbWrapper(final File file, final boolean writable) {
        super(file, writable);
    }

    public SqlJetDbWrapper(final File file, final boolean writable, final ISqlJetFileSystem fs) {
        super(file, writable, fs);
    }

    public SqlJetDbWrapper(final File file, final boolean writable, final String fsName) throws SqlJetException {
        super(file, writable, fsName);
    }

    /**
     * <p>
     * Opens connection to data base. It does not create any locking on
     * database. First lock will be created when be called any method which
     * requires real access to options or schema.
     * <p>
     * 
     * <p>
     * File could be null or have special value {@link #IN_MEMORY}. If file is
     * null then will be created temporary file which will be deleted at close.
     * If file is {@link #IN_MEMORY} then file doesn't created and instead
     * database will placed in memory. If regular file is specified but doesn't
     * exist then it will be tried to created.
     * </p>
     * 
     * @param file
     *            path to data base. Could be null or {@link #IN_MEMORY}.
     * @param write
     *            open for writing if true.
     * @throws SqlJetException
     *             if any trouble with access to file or database format.
     */
    public static SqlJetDbWrapper open(File file, boolean write) throws SqlJetException {
        final SqlJetDbWrapper db = new SqlJetDbWrapper(file, write);
        db.open();
        return db;
    }

    /**
     * @param file
     * @param write
     * @param fs
     * @return SqlJetDb object for opened database
     * @throws SqlJetException
     */
    public static SqlJetDbWrapper open(File file, boolean write, final ISqlJetFileSystem fs) throws SqlJetException {
        final SqlJetDbWrapper db = new SqlJetDbWrapper(file, write, fs);
        db.open();
        return db;
    }

    /**
     * @param file
     * @param write
     * @param fsName
     * @return SqlJetDb object for opened database
     * @throws SqlJetException
     */
    public static SqlJetDbWrapper open(File file, boolean write, final String fsName) throws SqlJetException {
        final SqlJetDbWrapper db = new SqlJetDbWrapper(file, write, fsName);
        db.open();
        return db;
    }

    /**
     * Open table.
     * 
     * @param tableName name of the table to open.
     * @return opened table
     */
    public ISqlJetTable getTable(final String tableName) throws SqlJetException {
        checkOpen();
        refreshSchema();
        return (SqlJetTable) runWithLock(new ISqlJetRunnableWithLock() {
            public Object runWithLock(SqlJetDb db) throws SqlJetException {
                return new SqlJetTable(db, btree, tableName, writable);
            }
        });
    }

    /**
     * Run modifications in write transaction.
     * 
     * @param op transaction to run.
     * @return result of the {@link ISqlJetTransaction#run(SqlJetDb)} call.
     */
    public Object runWriteTransaction(ISqlJetTransaction op) throws SqlJetException {
        checkOpen();
        if (writable) {
            return runTransaction(op, SqlJetTransactionMode.WRITE);
        } else {
            throw new SqlJetException(SqlJetErrorCode.MISUSE, "Can't start write transaction on read-only database");
        }
    }

    /**
     * Run read-only transaction.
     * 
     * @param op transaction to run.
     * @return result of the {@link ISqlJetTransaction#run(SqlJetDb)} call.
     */
    public Object runReadTransaction(ISqlJetTransaction op) throws SqlJetException {
        checkOpen();
        return runTransaction(op, SqlJetTransactionMode.READ_ONLY);
    }

    /**
     * Run transaction.
     * 
     * @param op
     *            transaction's body (closure).
     * @param mode
     *            transaction's mode.
     * @return result of the {@link ISqlJetTransaction#run(SqlJetDb)} call.
     */
    public Object runTransaction(final ISqlJetTransaction op, final SqlJetTransactionMode mode) throws SqlJetException {
        return runEngineTransaction(new ISqlJetEngineTransaction() {
            public Object run(SqlJetEngine engine) throws SqlJetException {
                return op.run((SqlJetDb)SqlJetDbWrapper.this);
            }
        }, mode);
    }

    /**
     * Executes pragma statement. If statement queries pragma value then pragma
     * value will be returned.
     */
    public Object pragma(final String sql) throws SqlJetException {
        checkOpen();
        refreshSchema();
        return runWithLock(new ISqlJetRunnableWithLock() {
            public Object runWithLock(SqlJetDb db) throws SqlJetException {
                return new SqlJetPragmasHandler(getOptions()).pragma(sql);
            }
        });
    }

    /**
     * @see #getTemporaryDatabase(boolean)
     * 
     * @return SqlJetDb object for temporary database
     * @throws SqlJetException
     */
    public SqlJetDbWrapper getTemporaryDatabase() throws SqlJetException {
        return getTemporaryDatabase(false);
    }
    
    /**
     * Opens temporary on-disk database which life span is less or equal to that
     * of this object. Temporary database is closed and deleted as soon as 
     * this database connection is closed.
     * 
     * Temporary file is used to store temporary database.
     * 
     * Subsequent calls to this method will return the same temporary database
     * In case previously create temporary database is closed by the user, 
     * then another one is created by this method. 
     * 
     * @param inMemory when true open temporary database in memory. 
     * 
     * @return temporary database being created or existing temporary database.
     * @throws SqlJetException
     */
    public SqlJetDbWrapper getTemporaryDatabase(final boolean inMemory) throws SqlJetException {
        checkOpen();        
        return (SqlJetDbWrapper) runWithLock(new ISqlJetRunnableWithLock() {
            public Object runWithLock(SqlJetDb db) throws SqlJetException {
                if (temporaryDb == null || !temporaryDb.isOpen()) {
                    closeTemporaryDatabase();
                    final File tmpDbFile = getTemporaryDatabaseFile(inMemory);
                    if (tmpDbFile != null) {
                        temporaryDb = SqlJetDbWrapper.open(tmpDbFile, true);
                    }
                }
                return temporaryDb;
            }

        });
    }
    
    @Override
    protected void closeResources() throws SqlJetException {
        closeTemporaryDatabase();
    }

    private void closeTemporaryDatabase() throws SqlJetException {
        if (temporaryDb != null) {
            temporaryDb.close();
            File tmpDbFile = temporaryDb.getFile();
            if (tmpDbFile != null && !IN_MEMORY.equals(tmpDbFile)) {
                getFileSystem().delete(tmpDbFile, false);
            }
        }
        temporaryDb = null;
    }

    private File getTemporaryDatabaseFile(boolean inMemory) throws SqlJetException {
        if (inMemory) {
            return IN_MEMORY;
        }
        File tmpDbFile = null;
        try {
            tmpDbFile = getFileSystem().getTempFile();
        } catch (IOException e) {
            throw new SqlJetException(SqlJetErrorCode.CANTOPEN, e);
        }
        if (tmpDbFile == null) {
            throw new SqlJetException(SqlJetErrorCode.CANTOPEN);
        }
        return tmpDbFile;
    }
}
