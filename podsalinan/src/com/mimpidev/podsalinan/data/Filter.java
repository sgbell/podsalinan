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
		fields.put("id", new IntegerType());
		fields.put("match", new StringType());
		fields.put("commandList", new StringType());
		fields.get("id").setPrimaryKey(true);
		
	}
	
	public Filter(String matchString, String commands) {
		setMatch(matchString);
		
	}

	public String getMatch(){
		return fields.get("match").getValue();
	}
	
	public void setMatch(String value){
		fields.get("match").setValue(value);
	}
	
	public String getCommandList(){
		return fields.get("commandList").getValue();
	}
	
	public void setCommandList(String commandList){
		fields.get("commandList").setValue(commandList);
	}
}
