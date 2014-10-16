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
package com.mimpidev.podsalinan.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bugman
 *
 */
public class ProgSettings{

	private Map<String, String> settings;
	private boolean finished;
	private Object waitObject = new Object();
	
	public ProgSettings (){
		settings = new HashMap<String, String>();
		finished=false;
		/*tableName = "settings";
		
		String[] columnNames = {"id","name","value"};
		String[] columnTypes = {"INTEGER PRIMARY KEY AUTOINCREMENT",
				                "TEXT","TEXT"};
		createColumnList (columnNames, columnTypes);*/
	}
	/**
	 * Loop through the array of settings and return the value for the requested setting
	 * @param name
	 * @return
	 */
	public String getSettingValue(String name){
		if (settings.size()>0)
			return settings.get(name);
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
		if (settings.containsKey(name)){
			settings.put(name, value);
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
			settings.put(name,value);
			return true;
		}
		return false;
	}
	
	public void upgradeSettings(){
		renameSetting("urlDirectory","defaultDirectory");
		renameSetting("maxPodcastDownloaders","maxDownloaders");
	}
	
	public void renameSetting(String originalName, String newName){
		if (settings.containsKey(originalName)){
			settings.put(newName, settings.get(originalName));
			settings.remove(originalName);
		}
	}
	
	public boolean removeSetting(String name){
		if (settings.containsKey(name)){
			settings.remove(name);
			return true;
		}
		return false;
	}
	
	public boolean isValidSetting(String name){
		if (settings.containsKey(name))
			return true;
		else
			return false;
	}
	
	public Map<String,String> getMap(){
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
	
	/**
	 * @return the waitObject
	 */
	public Object getWaitObject() {
		return waitObject;
	}

	/**
	 * @param waitObject the waitObject to set
	 */
	public void setWaitObject(Object waitObject) {
		this.waitObject = waitObject;
	}
	public String findSetting(String key) {
		return settings.get(key);
	}
}
