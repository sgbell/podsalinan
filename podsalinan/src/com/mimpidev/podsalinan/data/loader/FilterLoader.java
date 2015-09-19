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
import com.mimpidev.podsalinan.exception.MatchingFilterFound;
import com.mimpidev.sql.sqlitejdbc.Database;

/**
 * @author Sam Bell
 *
 */
public class FilterLoader extends TableLoader {

	/**
	 * 
	 */
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

	@Override
	public void readTable() {
		//TODO: Read filters from database
		
		Filter newFilter = new Filter();
		newFilter.setMatch("download <filename>.matches('^hak5--[0-9]{4}')");
		newFilter.setCommandList("<destination_dir>=<destination_dir>'/'"
				+ "<filename>.split('--')[1].split(0,1)+'/'");
		newFilter.setCommandName("Hak 5 Episodes");
		try {
			filters.addFilter(newFilter);
		} catch (MatchingFilterFound e) {
			System.out.println("Error: Filter Already exists.");
		}
		newFilter = new Filter();
		newFilter.setMatch("download <filename>.matches('^hak5--haktip')");
		newFilter.setCommandList("<destination_dir>=<destination_dir>'/'"
				+ "'haktip/'");
		newFilter.setCommandName("Haktips");
		try {
			filters.addFilter(newFilter);
		} catch (MatchingFilterFound e) {
			System.out.println("Error: Filter already exists.");
		}
	}

	@Override
	public void updateDatabase() {
		// TODO Auto-generated method stub

	}
}
