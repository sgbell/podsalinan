/**
 * 
 */
package bgdownloader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

/**
 * @author bugman
 *
 */
public class URLDownloadList extends DownloadDetails {

	private Vector<Details> downloads;
	
	public URLDownloadList(Object syncObject){
		super("URLS", syncObject);
		setDownloadList(new DownloadList(false));
		downloads = new Vector<Details>();
	}
	
	public Vector<Details> getDownloads(){
		return downloads;
	}
	
	public void addDownload(String url) {
		Details newFile= new Details(url);
		downloads.add(newFile);
		getDownloadList().addDownload(url);
		checkDownloadSize(newFile);
		synchronized (syncObject){
			syncObject.notify();
		}
	}

	public void removeDownload(){
		String url = this.getDownloadList().removeDownload();
		for (int dlc=0; dlc < downloads.size(); dlc++){
			if (downloads.get(dlc).url.substring(downloads.get(dlc).url.lastIndexOf('/')+1).equals(url)){
				downloads.get(dlc).remove=true;
			}
		}
	}
	
	public void addDownload(String url, String size, boolean added) {
		Details newFile= new Details(url,added);
		//System.out.println("URLDownloadList fileSize: "+size);
		newFile.setSize(size);
		downloads.add(newFile);
		getDownloadList().addDownload(url);
		checkDownloadSize(newFile);
		synchronized (syncObject){
			syncObject.notify();
		}
	}
	
	public void checkDownloadSize(Details newFile){
		//System.out.println("URLDownloadList.checkDownloadSize: "+newFile.getSize());
		if (Long.parseLong(newFile.getSize())==0){
			try {
				URLConnection stream = new URL(newFile.getURL()).openConnection();
				int fileSize=stream.getContentLength();
				newFile.setSize(Long.toString(fileSize));
				newFile.downloaded=newFile.NOT_STARTED;
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String filename=getDirectory()+"/"+newFile.getURL().substring(newFile.getURL().lastIndexOf('/')+1);
		File localFile = new File(filename);
		if (localFile.exists())
			if (localFile.length() < Long.parseLong(newFile.getSize())){
				newFile.downloaded=newFile.PREVIOUSLY_STARTED;
			} else if (localFile.length() == Long.parseLong(newFile.getSize())){
				newFile.downloaded=newFile.FINISHED;					
			}
	}
}
