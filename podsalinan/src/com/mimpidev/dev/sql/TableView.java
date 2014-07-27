/*******************************************************************************
 * Copyright (c) Sam Bell.
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
 * 
 */
package com.mimpidev.dev.sql;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.tmatesoft.sqljet.core.table.SqlJetDb;

/**
 * @author bugman
 *
 */
public class TableView {

    /**
     * The database Connection
     */
	private SqlJetDb db;
	/**
	 * The list of columns in the database table
	 */
	private Map<Integer,TableColumn> columnList;
    /**
     * The name of the table this is related to.
     */
	private String name;
	
	public TableView(File databaseFile){
		db = new SqlJetDb(databaseFile,true);
		columnList = new HashMap<Integer,TableColumn>();
	}
	
	public TableView(File databaseFile, HashMap<Integer,TableColumn> newColumnList){
		this(databaseFile);
		columnList=newColumnList;
	}
	
	public TableView(SqlJetDb newDb, HashMap<Integer,TableColumn> newColumnList){
		db = newDb;
		columnList = newColumnList;
	}
	
	public boolean createTable(){
		if ((name!=null)&&(name.length()>1)){
			String sql = "CREATE TABLE IF NOT EXISTS "+name+" (";
			// Do a for loop to go through each of the columns in columnList, and add them to the sql string
			
			sql.concat(");");
			
			
		}
		
		return false;
	}
	
	public class TableColumn {
		public String name;
		public String type;
	}
}
