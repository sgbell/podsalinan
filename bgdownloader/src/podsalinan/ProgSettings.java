/*******************************************************************************
 * Copyright (c) 2011 Sam Bell.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Sam Bell - initial API and implementation
 ******************************************************************************/
/**
 * 
 */
package podsalinan;

/**
 * @author bugman
 *
 */
public class ProgSettings {

	public String setting,
				  value;
	public ProgSettings(String name, String value) {
		setting = name;
		this.value = value;
	}
}
