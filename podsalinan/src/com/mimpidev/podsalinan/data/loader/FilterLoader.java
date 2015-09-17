/*******************************************************************************
 * Copyright (c) 17 Sep 2015 Mimpi Development.
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
 *     Sam Bell - sam@mimpidev.com
 ******************************************************************************/
package com.mimpidev.podsalinan.data.loader;

import com.mimpidev.podsalinan.data.Filter;
import com.mimpidev.podsalinan.data.FilterList;
import com.mimpidev.sql.sqlitejdbc.Database;

/**
 * @author Sam Bell
 *
 */
public class FilterLoader extends TableLoader {

	private FilterList filters;
	/**
	 * @param tableName
	 * @param newDb
	 * @throws ClassNotFoundException
	 */
	public FilterLoader(FilterList filterList, Database dbConnection)
			throws ClassNotFoundException {
		super("filters",dbConnection);
		setFilters(filterList);
		createColumnList(new Filter().getDatabaseRecord());
		setdbTable(dbConnection);
	}

	@Override
	public void readTable() throws ClassNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDatabase() {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the filters
	 */
	public FilterList getFilters() {
		return filters;
	}

	/**
	 * @param filters the filters to set
	 */
	public void setFilters(FilterList filters) {
		this.filters = filters;
	}

}
