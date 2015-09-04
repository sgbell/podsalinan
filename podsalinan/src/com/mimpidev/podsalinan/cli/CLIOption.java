/**
 * 
 */
package com.mimpidev.podsalinan.cli;

import java.text.DecimalFormat;
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
	
	protected boolean debug=true;
	/**
	 * 
	 */
	//protected Map<String, CLIOption> options;
	protected ReturnObject returnObject;

	/**
	 * 
	 */
	public CLIOption(DataStorage newData) {
		data=newData;
		// Declaring here so it doesn't have to be initialized in the children
		//options = new HashMap<String, CLIOption>();
		returnObject = new ReturnObject();
	}
	
	public CLIOption(DataStorage newData, ReturnObject parentObject) {
		this(newData);
		returnObject=parentObject;
	}

	public abstract ReturnObject execute(Map<String,String> functionParms);
	
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
}
