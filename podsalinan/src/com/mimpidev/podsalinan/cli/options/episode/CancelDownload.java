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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnObject;
import com.mimpidev.podsalinan.data.Episode;
import com.mimpidev.podsalinan.data.Podcast;

/**
 * @author bugman
 *
 */
public class CancelDownload extends BaseEpisodeOption {

	/**
	 * @param newData
	 */
	public CancelDownload(DataStorage newData) {
		super(newData);
	}

	@Override
	public ReturnObject execute(Map<String, String> functionParms) {
		debug=true;
		if (debug) Podsalinan.debugLog.logMap(this, functionParms);
		Episode episode=null;
		
		if (functionParms.containsKey("uid") && functionParms.containsKey("episode")){
			episode = getEpisode(functionParms.get("uid"), functionParms.get("episode"));
		}
		if (episode!=null){
			try {
				data.getUrlDownloads().cancelDownload(new URL(episode.getURL()));
				System.out.println("Successfully Cancelled Download of Episode: "+episode.getTitle());
			} catch (MalformedURLException e) {
				System.out.println("Error: Invalid URL");
			}
		}
		returnObject.methodCall="podcast "+getPodcast().getDatafile()+" episode ";
		returnObject.methodCall+=functionParms.get("episode");
		returnObject.parameterMap.clear();
		returnObject.execute=true;
		
		return returnObject;
	}

}
