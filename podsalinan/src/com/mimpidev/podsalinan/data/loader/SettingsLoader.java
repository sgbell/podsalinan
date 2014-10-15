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

import org.tmatesoft.sqljet.core.table.SqlJetDb;

import com.mimpidev.podsalinan.data.ProgSettings;

/**
 * @author bugman
 *
 */
public class SettingsLoader extends TableLoader {

	/**
	 * @param podsalinanDB 
	 * 
	 */
	public SettingsLoader(ProgSettings settings, SqlJetDb dbConnection) {
		// TODO Auto-generated constructor stub
		setdbTable(dbConnection);
	}

	@Override
	public void readTable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateDatabase() {
		// TODO Auto-generated method stub
		
	}

}
