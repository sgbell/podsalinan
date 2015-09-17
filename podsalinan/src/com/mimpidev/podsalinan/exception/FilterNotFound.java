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
package com.mimpidev.podsalinan.exception;

/**
 * @author Sam Bell
 *
 */
public class FilterNotFound extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1901306139243424462L;
	private String filterString;
	
	public FilterNotFound(String filter){
		super("Filter not found.");
		setFilterString(filter);
	}

	/**
	 * @return the filterString
	 */
	public String getFilterString() {
		return filterString;
	}

	/**
	 * @param filterString the filterString to set
	 */
	public void setFilterString(String filterString) {
		this.filterString = filterString;
	}
	
	
}
