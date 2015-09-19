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

import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.dev.debug.Log;
import com.mimpidev.podsalinan.cli.CLInput;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.data.Episode;

/**
 * @author bugman
 *
 */
public class DeleteEpisodeFromDrive extends BaseEpisodeOption {

	/**
	 * @param newData
	 */
	public DeleteEpisodeFromDrive(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		if (debug) if (Log.isDebug())Log.logMap(this, functionParms);
		Episode episode=null;
		
		if (functionParms.containsKey("uid") && functionParms.containsKey("userInput")){
			episode = this.getEpisode(functionParms.get("uid"), functionParms.get("userInput"));
		}		
		
		if (episode!=null){
			System.out.println("Episode: "+episode.getTitle());
			CLInput input = new CLInput();
			if (input.confirmRemoval()){
				getPodcast().deleteEpisodeFromDrive(episode);
			}
		}
		
		returnObject.methodCall="podcast "+getPodcast().getDatafile()+" episode "+functionParms.get("userInput");
		returnObject.parameterMap.clear();
		returnObject.execute=true;
		return returnObject;
	}

}
