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

import java.util.Vector;

/**
 * @author bugman
 *
 */
public class ProgSettings {

	private Vector<Setting> settings;
	private boolean finished;
	
	public ProgSettings (){
		settings = new Vector<Setting>();
		finished=false;
	}
	/**
	 * Loop through the array of settings and return the value for the requested setting
	 * @param name
	 * @return
	 */
	public String getSettingValue(String name){
		if (settings.size()>0)
			return findSetting(name).value;
		else
			return null;
	}
	
	/**
	 * Loop through the array and find the specified setting, and update it's value
	 * @param name
	 * @param value
	 * @return
	 */
	public boolean updateSetting(String name, String value){
		Setting currentSetting = findSetting(name);
		if (currentSetting!=null){
			currentSetting.value=value;
			return true;
		}
		return false;
	}
	
	public int size(){
		return settings.size();
	}
	
	public void clear(){
		settings.clear();
	}
	
	public boolean addSetting(String name, String value){
		if (!isValidSetting(name)){
			Setting newSetting = new Setting (name,value);
			// Updating old program settings to new ones.
			if (newSetting.name.equalsIgnoreCase("urlDirectory"))
				newSetting.name="defaultDirectory";
			
			if (newSetting.name.equalsIgnoreCase("maxPodcastDownloaders"))
				newSetting.name="maxDownloaders";

			settings.add(newSetting);
			return true;
		}
		return false;
	}
	
	public boolean removeSetting(String name){
		Setting currentSetting=findSetting(name);
		if (currentSetting!=null){
			//System.out.println("Debug: Found setting");
			settings.remove(currentSetting);
			//System.out.println("Size: "+settings.size());
			return true;
		}
		return false;
	}
	
	public boolean isValidSetting(String name){
		if (findSetting(name)!=null)
			return true;
		else
			return false;
	}
	
	public Setting findSetting(String name){
		for (Setting currentSetting : settings){
			if (currentSetting.name.equalsIgnoreCase(name))
				return currentSetting;
		}
		return null;
	}
	
	public Vector<Setting> getArray(){
		return settings;
	}
	/**
	 * @return the finished
	 */
	public boolean isFinished() {
		return finished;
	}
	/**
	 * @param finished the finished to set
	 */
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
}
