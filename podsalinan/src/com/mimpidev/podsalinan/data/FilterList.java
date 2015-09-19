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
package com.mimpidev.podsalinan.data;

import java.util.ArrayList;

import com.mimpidev.podsalinan.exception.FilterNotFound;
import com.mimpidev.podsalinan.exception.MatchingFilterFound;


/* TODO: Need to code FilterLoader to do the normal database updates
 * for the filter objects.
 * Need to code the Filter parser. 
 */
/**
 * @author Sam Bell
 *
 */
public class FilterList {

	private ArrayList<Filter> filters;
	
	public FilterList(){
		filters = new ArrayList<Filter>();
	}

	/**
	 * @return the filters
	 */
	public ArrayList<Filter> getFilters() {
		return filters;
	}

	/**
	 * @param filters the filters to set
	 */
	public void setFilters(ArrayList<Filter> filters) {
		this.filters = filters;
	}
	
	public boolean addFilter(String matchString, String commands, String commandName) throws MatchingFilterFound{
		Filter newFilter=new Filter(matchString, commands, commandName);
		return addFilter(newFilter);
	}

	public boolean addFilter(Filter newFilter) throws MatchingFilterFound {
		if (findFilter(newFilter.getMatch())==null){
			filters.add(newFilter);
			return true;
		} else {
			throw new MatchingFilterFound("Found an existing filter.");
		}
	}

	public Filter findFilter(String match) {
		for (Filter currentFilter: filters){
			if (currentFilter.equals(match))
				return currentFilter;
		}
		return null;
	}
	
	public boolean deleteFilter(String match) throws FilterNotFound{
		for (Filter currentFilter: filters){
			if (currentFilter.equals(match)){
				filters.remove(currentFilter);
				return true;
			}
		}
		throw new FilterNotFound(match);
	}
}
