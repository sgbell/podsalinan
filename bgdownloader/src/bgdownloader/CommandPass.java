package bgdownloader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CommandPass implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		
		if (command.compareTo("quit")==0){
			System.exit(0);
		}
		
		if (command.compareTo("addURL")==0){
			System.out.println("command adding url");
		}
		if (command.compareTo("addRSS")==0){
			System.out.println("command adding rss feed");
		}
		if (command.compareTo("setDownloadFolder")==0){
			System.out.println("command changing download folder");
		}
		if (command.compareTo("updateInterval")==0){
			System.out.println("command update interval");
		}
		if (command.compareTo("removeFeed")==0){
			System.out.println("command remove feed");			
		}
	}

	
}
