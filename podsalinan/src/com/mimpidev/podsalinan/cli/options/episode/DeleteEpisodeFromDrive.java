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
package com.mimpidev.podsalinan.cli.options.episode;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ObjectCall;

/**
 * @author bugman
 *
 */
public class DeleteEpisodeFromDrive extends CLIOption {

	/**
	 * @param newData
	 */
	public DeleteEpisodeFromDrive(DataStorage newData) {
		super(newData);
	}

	@Override
	public ObjectCall execute(String command) {
		debug=true;
		if (debug) Podsalinan.debugLog.logInfo(this.getClass().getName()+":"+command);
		// TODO: flesh out DeleteEpisodeFromDrive
		return returnObject;
	}

}
