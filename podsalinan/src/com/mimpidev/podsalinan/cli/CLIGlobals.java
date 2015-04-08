/**
 * 
 */
package com.mimpidev.podsalinan.cli;

import java.util.HashMap;
import java.util.Map;

import com.mimpidev.podsalinan.Podsalinan;

/**
 * @author sbell
 *
 */
public class CLIGlobals {

	/**
	 *  selection is a way for me to keep hold of what is currently selected in the system, which will be
	 *  passed through all of the CLIOption classes to alter when appropriate.
	 *   
	 */
	private Map<String,String> globalSelection; 
	
	private boolean debug=false;
	/**
	 * 
	 */
	public CLIGlobals() {
		globalSelection=new HashMap<String,String>();
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, String> getGlobalSelection(){
		if (debug){
			Podsalinan.debugLog.logInfo(this, "getGlobalSelection() called");
			Podsalinan.debugLog.logInfo(this, "Size: "+globalSelection.size());
		}
		
		return globalSelection;
	}
	
	/**
	 * 
	 * @return
	 */
	public String globalSelectionToString(){
		String returnString="";
		String[] firstLevel = {"downloads","episode"};
		for (String key : firstLevel){
			if (globalSelection.containsKey(key)){
				returnString=key+" "+globalSelection.get(key)+" ";
			}
		}
		if (globalSelection.containsKey("podcast")){
			returnString="podcast "+globalSelection.get("podcast")+" "+returnString;
		}
		
		return returnString;
	}
	
	public ReturnObject createReturnObject(){
		ReturnObject returnObject = new ReturnObject();
		String[] firstLevel = {"downloads","podcast"};
		for (String key : firstLevel){
			if (globalSelection.containsKey(key)){
				returnObject.methodCall=key;
				returnObject.methodParameters=globalSelection.get(key);
			}
		}
		if (globalSelection.containsKey("episode")){
			returnObject.methodParameters+=" "+globalSelection.get("episode");
		}
		
		return returnObject;
	}
}
