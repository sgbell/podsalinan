/**
 * 
 */
package podsalinan;

import javax.swing.JFrame;

/**
 * @author bugman
 *
 */
public class MainWindow extends JFrame {
	private CommandPass commandPasser;
	private DataStorage data;
	
	public MainWindow(){
		commandPasser = new CommandPass();
	}
	
	public MainWindow(DataStorage newData) {
		this();
		data = newData;
	}
}
