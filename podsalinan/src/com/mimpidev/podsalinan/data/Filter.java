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

import com.mimpidev.dev.sql.DataRecord;
import com.mimpidev.dev.sql.field.IntegerType;
import com.mimpidev.dev.sql.field.StringType;

/**
 * @author Sam Bell
 *
 */
public class Filter extends DataRecord {

	public Filter () {
		put("id", new IntegerType());
		put("match", new StringType());
		put("commandList", new StringType());
		put("commandName", new StringType());
		get("id").setPrimaryKey(true);
		get("id").setAutoIncrement(true);
	}
	
	public Filter(String matchString, String commands, String commandName) {
		setMatch(matchString);
		setCommandList(commands);
		setCommandName(commandName);
	}

	public String getMatch(){
		return get("match").getValue();
	}
	
	public void setMatch(String value){
		get("match").setValue(value);
	}
	
	public String getCommandList(){
		return get("commandList").getValue();
	}
	
	public void setCommandList(String commandList){
		get("commandList").setValue(commandList);
	}

	public void setCommandName(String commandName) {
		get("commandName").setValue(commandName);
	}

	public String getCommandName() {
		return get("commandName").getValue();
	}
}
