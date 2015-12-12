/*******************************************************************************
 * Copyright (c) 12 Dec 2015 Mimpi Development.
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
package com.mimpidev.podsalinan.cli.options.settings;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import com.mimpidev.dev.debug.Log;
import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.CLInput;
import com.mimpidev.podsalinan.cli.ReturnObject;

/**
 * @author Sam Bell
 *
 */
public class DownloadTimeDay extends CLIOption {
	private CLInput input = new CLInput();

	public DownloadTimeDay(DataStorage newData) {
		super(newData);
	}

	public ReturnObject execute(Map<String, String> functionParms) {
		if (Log.isDebug())Log.logMap(this, functionParms);

		if (functionParms.containsKey("startTime") && 
			functionParms.containsKey("endTime")){
			data.getSettings().updateSetting("startTime", functionParms.get("startTime"));
			data.getSettings().updateSetting("endTime", functionParms.get("endTime"));
		} else if (functionParms.containsKey("userInput") &&
				(functionParms.get("userInput").equals("0")||
				functionParms.get("userInput").equals("false"))) {
			data.getSettings().updateSetting("startTime", "");
			data.getSettings().updateSetting("endTime", "");
		} else {
			System.out.println("Change Scheduled time for downloading");
			System.out.println();
			System.out.println("Enter Start Time for Schedule: [00:00PM/AM or empty to disable]");
			String startTime = input.getStringInput();
			System.out.println("Enter End Time for Schedule: [00:00PM/AM or empty to disable]");
			String endTime = input.getStringInput();
			
			DateFormat timeFormat = new SimpleDateFormat("HH:mma");
			try {
				if (!timeFormat.parse(startTime).toString().equals("")&&
					!timeFormat.parse(endTime).toString().equals("")){
					data.getSettings().updateSetting("startTime", startTime);
					data.getSettings().updateSetting("endTime", endTime);
				}
			} catch (ParseException e) {
				// If either value is blank clear both
				data.getSettings().updateSetting("startTime", "");
				data.getSettings().updateSetting("endTime", "");
			}
		}
		
		return returnObject;
	}

}
