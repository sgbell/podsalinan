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
package com.mimpidev.dev.debug;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author bugman
 *
 */
public class Log {
	
    /**
     * This is used to define if the program should be outputting debug information,
     * either to the output file, or to the screen
     */
	private static boolean logDebug=true;
	private static boolean outputToScreen=false;
	private static boolean showShortName=false;

	private static RandomAccessFile fileOutput=null;
	private static String dataDirectory="mimpidev-debug";
	private static boolean isOpen=false;
	
	public Log(){
		
	}
	
	public Log(String programName){
		dataDirectory=programName;
	}
	
	public Log(File file, String mode){
		setNewLog(file,mode);
	}

	public Log(String filename, String mode){
		setNewLog(filename, mode);
	}
	
	public static void setNewLog(String filename, String mode){
		setNewLog(new File(filename), mode);
	}
	
	public static void setNewLog (File filename, String mode){
		try {
			if (!filename.exists()){
				filename.createNewFile();
			}
			fileOutput = new RandomAccessFile(filename, mode);
			fileOutput.seek(fileOutput.length());
			isOpen=true;
		} catch (IOException e) {
			System.err.println("[Error] Cannot open file");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param stackTrace
	 */
	public static synchronized void printStackTrace(StackTraceElement[] stackTrace){
		println("[Error] Oh no! It's another error.");
		for (StackTraceElement traceElement : stackTrace){
			println("[Debug] "+traceElement.toString());
		}
		println("[Error] End of error report");
	}

	/**
	 * 
	 * @param debugLine
	 */
	public synchronized static void logInfo(String debugLine){
		println ("[Info] "+debugLine);
	}
	
	public synchronized static void logInfo(Object object, String debugLine){
		println ("[Info]["+getShortClassName(object.getClass().getName())+"] "+debugLine);
	}

	public synchronized static void logInfo(Object object, int lineNum, String debugLine) {
		logInfo(object,"Line:"+lineNum+" - "+debugLine);
	}
	
	/**
	 * 
	 * @param debugLine
	 */
	public synchronized void logError(String debugLine){
		println ("[Error] "+debugLine);
	}
	
	public synchronized static void logError (Object object, String debugLine){
		println ("[Error]["+getShortClassName(object.getClass().getName())+"] "+debugLine);
	}
	/**
	 * 
	 * @param debugLine
	 */
	private synchronized static void println(String debugLine){
		try {
			if (fileOutput==null)
				initialise();
			fileOutput.writeBytes(debugLine);
			fileOutput.writeBytes(System.getProperty("line.separator"));
		} catch (IOException e) {
			System.err.println("[Error] Problem writing to debug log");
			e.printStackTrace();
		}
		if (outputToScreen){
			System.out.println(debugLine);
		}
	}

	/**
	 * @return showMeTheDebug Value (used to tell program if we want the debug information outputted). 
	 */
	public static boolean isDebug() {
		return logDebug;
	}
	
	public static void setLogDebug(boolean newLogDebug){
		logDebug=newLogDebug;
	}

	public static void close() {
		try {
			if (fileOutput!=null && isOpen)
				fileOutput.close();
			isOpen=false;
		} catch (IOException e) {
			System.err.println("[Error] Problem closing debug log");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return
	 */
    public static boolean initialise(){
    	return initialise("debug.log");
    }
	
	/**
     * 
     * @param filename
     * @return
     */
	public static boolean initialise(String filename){
		if (fileOutput==null){
			String settingsDir="";
			String fileSystemSlash="";
			if (System.getProperty("os.name").startsWith("Windows")){
				settingsDir = System.getProperty("user.home")+"\\appdata\\local\\"+dataDirectory;
				fileSystemSlash = "\\";
			} else {
				settingsDir = System.getProperty("user.home")+"/."+dataDirectory;
				fileSystemSlash = "/";
			}
			// Check if the settings directory exists, and if not, create it.
			File settingsLocation = new File(settingsDir);
			if (!settingsLocation.exists()){
				settingsLocation.mkdir();
			}
			
			setNewLog(settingsDir+fileSystemSlash+filename,"rw");
			return true;
		}
		return false;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void logMap(Map mapObject){
		Iterator<Entry> it = mapObject.entrySet().iterator();
		
		println("Map Object Debug");
		println("----------------");
		while (it.hasNext()){
			Map.Entry pair = (Map.Entry)it.next();
			if ((String)pair.getValue().toString()!=null){
			   println((String)pair.getKey()+" = "+(String)pair.getValue().toString());
			} else {
			   println((String)pair.getKey()+" = null");
			}
		}
		println("----------------");
		println("End Map Key List");
	}
	
	protected static String getShortClassName(String className){
		if (showShortName)
			return className.substring(className.lastIndexOf(".")+1).trim();
		else
			return className;
	}

	public String getDataDirectory() {
		return dataDirectory;
	}

	public void setDataDirectory(String newDataDirectory) {
		dataDirectory = newDataDirectory;
	}

	@SuppressWarnings("rawtypes")
	public static void logMap(Object object,
			 Map mapObject) {
		logInfo(object,"Map used:");
		logMap(mapObject);
	}

	public static void logInfo(Object object, int lineNum, String[] stringArray) {
		logInfo(object,lineNum,"Array of Strings found:");
		for (String item : stringArray){
			logInfo(object,lineNum,item);
		}
	}

	public static boolean isOpen() {
		return isOpen;
	}
}
