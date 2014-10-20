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

/**
 * @author bugman
 *
 */
public class Log {
	
    /**
     * This is used to define if the program should be outputting debug information,
     * either to the output file, or to the screen
     */
	private boolean showMeTheDebug=true;
	private boolean outputToScreen=true;

	private RandomAccessFile fileOutput=null;
	
	public Log(){
		
	}
	
	public Log(File file, String mode){
		setNewLog(file,mode);
	}

	public Log(String filename, String mode){
		setNewLog(filename, mode);
	}
	
	public void setNewLog(String filename, String mode){
		setNewLog(new File(filename), mode);
	}
	
	public void setNewLog (File filename, String mode){
		try {
			fileOutput = new RandomAccessFile(filename, mode);
			fileOutput.seek(fileOutput.length());
		} catch (IOException e) {
			System.err.println("[Error] Cannot open file");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param stackTrace
	 */
	public synchronized void printStackTrace(StackTraceElement[] stackTrace){
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
	public synchronized void logInfo(String debugLine){
		println ("[Info]"+debugLine);
	}
	
	/**
	 * 
	 * @param debugLine
	 */
	public synchronized void logError(String debugLine){
		println ("[Error]"+debugLine);
	}
	
	/**
	 * 
	 * @param debugLine
	 */
	private synchronized void println(String debugLine){
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
	public boolean showDebug() {
		return showMeTheDebug;
	}

	public void close() {
		try {
			fileOutput.close();
		} catch (IOException e) {
			System.err.println("[Error] Problem closing debug log");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return
	 */
    public boolean initialise(){
    	return initialise("debug.log");
    }
	
	/**
     * 
     * @param filename
     * @return
     */
	public boolean initialise(String filename){
		if (fileOutput==null){
			String settingsDir="";
			String fileSystemSlash="";
			if (System.getProperty("os.name").equalsIgnoreCase("linux")){
				settingsDir = System.getProperty("user.home").concat("/.podsalinan");
				fileSystemSlash = "/";
			}else if (System.getProperty("os.name").startsWith("Windows")){
				settingsDir = System.getProperty("user.home").concat("\\appdata\\local\\podsalinan");
				fileSystemSlash = "\\";
			}
			setNewLog(settingsDir+fileSystemSlash+filename,"rw");
			return true;
		}
		return false;
	}
}
