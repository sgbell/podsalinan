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
package podsalinan;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * @author bugman
 *
 */
public class CommandPass implements ActionListener {
	private Vector<ProgSettings> progSettings;
	private boolean programExiting;

	public CommandPass(Vector<ProgSettings> progSettings) {
		this.setProgramExiting(programExiting);
		this.setProgSettings(progSettings);
	}
	
	public CommandPass(){
		
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		
		if (command.compareTo("quit")==0){
			setProgramExiting(true);
			System.exit(0);
		}
		if (command.compareTo("addURL")==0){
		}
		if (command.compareTo("addRSS")==0){
		}
		if (command.compareTo("setDownloadFolder")==0){
		}
		if (command.compareTo("removeFeed")==0){

		}
		
		if (command.compareTo("updateInterval")==0){

		}
		if (command.compareTo("setURLFolder")==0){

		}
		if (command.compareTo("deleteURL")==0){
		}
		if (command.compareTo("numPodcasts")==0){
		}
		if (command.compareTo("numDownloads")==0){
		}
	}

	/**
	 * This function creates and shows a directory dialog window for the user to
	 * select a directory.
	 * @return String - returns the new directory.
	 */
	public String changeDirectory(){
    	// Create and Show a Directory Dialog for the user to choose the feed
    	// directory.
    	JFileChooser browseWindow= new JFileChooser();
    	// Sets what is able to be selected. in this case, a folder.
    	browseWindow.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    	return null;
	}
	
	/**
	 * @return the programExiting
	 */
	public boolean isProgramExiting() {
		return programExiting;
	}

	/**
	 * @param programExiting the programExiting to set
	 */
	public void setProgramExiting(boolean programExiting) {
		this.programExiting = programExiting;
	}

	/**
	 * @return the progSettings
	 */
	public Vector<ProgSettings> getProgSettings() {
		return progSettings;
	}

	/**
	 * @param progSettings the progSettings to set
	 */
	public void setProgSettings(Vector<ProgSettings> progSettings) {
		this.progSettings = progSettings;
	}
}