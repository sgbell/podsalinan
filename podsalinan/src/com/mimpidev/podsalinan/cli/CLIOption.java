/**
 * 
 */
package com.mimpidev.podsalinan.cli;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.mimpidev.podsalinan.DataStorage;

/**
 * @author sbell
 *
 */
public abstract class CLIOption {

	/**
	 * 
	 */
	protected DataStorage data;
	
	protected boolean debug=false;
	/**
	 * 
	 */
	protected Map<String, CLIOption> options;
	protected ReturnObject returnObject;
	
	/**
	 *  selection is a way for me to keep hold of what is currently selected in the system, which will be
	 *  passed through all of the CLIOption classes to alter when appropriate.
	 *   
	 */
	protected Map<String,String> globalSelection; 

	/**
	 * 
	 */
	public CLIOption(DataStorage newData) {
		data=newData;
		// Declaring here so it doesn't have to be initialized in the children
		options = new HashMap<String, CLIOption>();
		returnObject = new ReturnObject();
	}
	
	public CLIOption(DataStorage newData, ReturnObject parentObject) {
		this(newData);
		returnObject=parentObject;
	}

	public abstract ReturnObject execute(String command);
	
	public String getCharForNumber(int i){
		return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
	}
	
	public String getEncodingFromNumber(int number){
		String charOutput="";
		if (number<27)
			charOutput = getCharForNumber(number);
		else {
			if (number%26!=0){
				charOutput+=getCharForNumber(number/26);
				charOutput+=getCharForNumber(number%26);
			} else {
				charOutput=getCharForNumber((number/26)-1)+"Z";
			}
		}
		
		return charOutput;
	}
	
	public int convertCharToNumber(String input) {
		int number=1;
		if (input.length()>1){
			for (int charCount=0; charCount < input.length()-1; charCount++)
				number=number*26*(int)(input.toUpperCase().charAt(charCount)-64);
			number+=(int)(input.toUpperCase().charAt(input.length()-1)-64);
		} else if (input.length()==1)
			number=(int)(input.toUpperCase().charAt(0)-64);
		number--;
		
		return number;
	}

	protected String humanReadableSize(long fileSize) {
		String fileSizeModifier="";
		double newOutputSize;
		
		if (fileSize>1073741824){
			fileSizeModifier=" Gb";
			newOutputSize = (double)fileSize/1073741824;
		} else if (fileSize>1048576){
			fileSizeModifier=" Mb";
			newOutputSize = (double)fileSize/1048576;
		} else if (fileSize>1024){
			fileSizeModifier=" Kb";
			newOutputSize = (double)fileSize/1024;
		} else
			newOutputSize = (double)fileSize;

		newOutputSize=new Double(new DecimalFormat("#.##").format(newOutputSize)).doubleValue();
		
        if (newOutputSize==0)
        	return "0";
        else
        	return Double.toString(newOutputSize)+fileSizeModifier;
	}

	/**
	 * @return the globalSelection
	 */
	public Map<String,String> getGlobalSelection() {
		return globalSelection;
	}

	/**
	 * @param globalSelection the globalSelection to set
	 */
	public void setGlobalSelection(Map<String,String> globalSelection) {
		Iterator<CLIOption> it = options.values().iterator();
		while (it.hasNext()){
			CLIOption currentOption = it.next();
			currentOption.setGlobalSelection(globalSelection);
		}
		this.globalSelection = globalSelection;
	}
	
	public String globalSelectionToString(){
		String returnString="";
		String[] firstLevel = {"download","episode"};
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
}
