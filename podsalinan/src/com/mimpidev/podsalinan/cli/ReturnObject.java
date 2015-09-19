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
 *  ObjectCall is a way of handling methodCalls in the system, to loop through calls,
 *  even calls through different parts of the menu system
 */
package com.mimpidev.podsalinan.cli;

import java.util.HashMap;
import java.util.Map;

import com.mimpidev.dev.debug.Log;

/**
 * @author bugman
 *
 */
public class ReturnObject {

	/**
	 * The method to be called
	 */
	public String methodCall="";
	/**
	 * The parameters to be passed to the method
	 */
	public Map<String,String> parameterMap = new HashMap<String,String>();
	/**
	 * If the system needs to continue looping
	 */
	public boolean execute=false;

	public void debug(boolean debug) {
		if (debug) {
			Log.logInfo(this, "methodCall="+methodCall);
			Log.logInfo(this, "parameterMap contents");
			Log.logMap(parameterMap);
		}
	}

}
