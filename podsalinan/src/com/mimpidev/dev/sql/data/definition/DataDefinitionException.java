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
package com.mimpidev.dev.sql.data.definition;

/**
 * @author bugman
 *
 */
public class DataDefinitionException extends Exception {

	/**
	 * 
	 */
	public DataDefinitionException() {
	}

	/**
	 * @param message
	 */
	public DataDefinitionException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DataDefinitionException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DataDefinitionException(String message, Throwable cause) {
		super(message, cause);
	}

}
