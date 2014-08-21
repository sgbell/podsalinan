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
import java.io.FileNotFoundException;
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

	private RandomAccessFile fileOutput;
	
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
	public synchronized void println(String debugLine){
		try {
			fileOutput.writeBytes(debugLine);
			fileOutput.writeBytes(System.getProperty("line.separator"));
		} catch (IOException e) {
			System.err.println("[Error] Problem writing to debug log");
			e.printStackTrace();
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
}
