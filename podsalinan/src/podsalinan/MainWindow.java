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
	
	public MainWindow(){
		
	}
	
	public MainWindow(CommandPass commands){
		commandPasser = commands;
	}
	
	public void addDownloadToQueue(String filename,
							  long fileSize,
							  long completedSize,
							  String destination){
		
	}
	
	public void removeDownloadFromQueue(String filename,
										String destination){
		
	}
	
	public void updateDownloadStatus(String filename,
									 String destination,
									 long completedSize){
		
	}
	
	public void increaseDownloadPriority(String filename,
										 String destination){
		
	}
	
	public void decreaseDownloadPriority(String filename,
										 String destination){
		
	}
	
	public void addPodcast (String name,
							String imageName){
		
	}
	
	public void removePodcase (String name){
		
	}
	
	public void addEpisodeToPodcast (String podcast,
									 Episode episodeToAdd){
		
	}
	
	public void markEpisode (String podcast,
							 String episode,
							 int status){
		
	}
	
	public void removeEpisodeFromPodcast (String podcast,
										  String episodeName){
		
	}
	
	public void addURLDownload (String newURL,
								String destination,
								String size){
		
	}
	
	public void removeURLDownload (String filename,
								   String destination){
		
	}
	
	public void setDownloadStatus (String filename,
								   String destination,
								   int Status){
		
	}
}
