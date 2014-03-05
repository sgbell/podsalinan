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
		MenuBar menubar = new MenuBar(commandPasser);
		menubar.createMenu("menu.xml");
		
		setSize(800,600);
		setTitle("Podsalinan");
		setJMenuBar(menubar);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public MainWindow(DataStorage newData) {
		this();
		data = newData;
		
		commandPasser.setDataStorage(data);
		setVisible(true);
	}
}
