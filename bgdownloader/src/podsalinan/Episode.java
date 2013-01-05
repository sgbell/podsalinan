/*******************************************************************************
 * Copyright (c) 2011 Sam Bell.
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
package podsalinan;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author bugman
 *
 */
public class Episode extends Details {
	public String date,
				  title,
				  description;

	public Episode(String published, String title, String url, String length, String desc) {
		super(url,length);
		date = published;
		this.title=title;
		description=desc;
	}
	
	public String getDate(){
		DateFormat df = new SimpleDateFormat("EEE, dd MMM yyy HH:mm:ss zzz");
		try {
			Date newDate = df.parse(date);
			DateFormat newFormat = new SimpleDateFormat("EEE, dd-MMM-yyy HH:mm:ss");
			System.out.println ("Date: "+newFormat.format(newDate));
			return newFormat.format(newDate);
		} catch (ParseException e) {
		}
		return "";
	}
};
