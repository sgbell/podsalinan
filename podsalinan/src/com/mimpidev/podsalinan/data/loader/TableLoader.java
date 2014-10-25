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
package com.mimpidev.podsalinan.data.loader;

import java.util.Map;

import org.tmatesoft.sqljet.core.table.SqlJetDb;

import com.mimpidev.dev.debug.Log;
import com.mimpidev.dev.sql.TableView;

/**
 * @author bugman
 *
 */
public abstract class TableLoader extends TableView {

	public TableLoader(){
		
	}
	
	public TableLoader(SqlJetDb newDb, Map<String, String> newColumnList,
			String tableName, Log debugLog) {
		super(newDb, newColumnList, tableName, debugLog);
	}
	/**
	 * 
	 */
	public abstract void readTable();
	/**
	 * 
	 */
	public abstract void updateDatabase();
}
