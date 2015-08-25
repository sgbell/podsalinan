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
	
	private Map<String,String> gsToParameterMap = new HashMap<String,String>(){/**
		 * 
		 */
		private static final long serialVersionUID = -8904676551176138163L;

	{ 
		   put("podcastid","uid");
		   put("downloads","userInput");
		   put("episode","userInput");
	}};
	
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
				returnString=key+" "+globalSelection.get(key);
			}
		}
		if (globalSelection.containsKey("podcastid")){
			returnString="podcast "+globalSelection.get("podcastid")+(returnString.length()>0?" "+returnString:"");
		}
		
		return returnString;
	}
	
	/**
	 *  This function shouldn't have to return anything, as it's being set in the parameterMap passed in
	 * @param parameterMap
	 */
	public void createReturnParameters(Map<String, String> parameterMap) {
		parameterMap.clear();
		for (String key: gsToParameterMap.keySet()){
			if (globalSelection.containsKey(key)){
				parameterMap.put(gsToParameterMap.get(key), globalSelection.get(key));
			}
		}
	}
}
