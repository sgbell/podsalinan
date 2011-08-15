/**
 * 
 */
package bgdownloader;

import java.awt.BorderLayout;
import java.util.Vector;

//import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
//import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * @author bugman
 *
 */
@SuppressWarnings("serial")
public class DownloadList extends JPanel {
	
	private DefaultTableModel downloadList;
	private Vector<String> filenames;
	//private JEditorPane previewPane;
	private boolean isRssFeed;
	
	public DownloadList(boolean rssFeed){
		super();

		filenames = new Vector<String>();
		isRssFeed = rssFeed;
		setLayout(new BorderLayout());
		if (rssFeed){
			Object headers[] = {"Title",
					"Date",
					"Progress"};
			downloadList = new DefaultTableModel(headers,1);
		} else {
			Object headers[] = {"Filename",
					"Progress"};
			downloadList = new DefaultTableModel(headers,1);
		}
		JTable downloads = new JTable(downloadList);
		
		// removing grid from table
		downloads.setShowGrid(false);
		
		downloads.setRowSelectionAllowed(true);

		
		if (rssFeed) {
			Object newRow[] = new Object [] {"","","0%"};
			downloadList.addRow(newRow);
			downloadList.removeRow(0);
			TableColumn myCol = downloads.getColumnModel().getColumn(2);
			myCol.setCellRenderer(new ProgressCellRenderer());
		} else {
			Object newRow[] = new Object [] {"","0%"};
			downloadList.addRow(newRow);
			downloadList.removeRow(0);
			TableColumn myCol = downloads.getColumnModel().getColumn(1);
			myCol.setCellRenderer(new ProgressCellRenderer());
		}
		
		//previewPane = new JEditorPane();
		//previewPane.setEditable(false);
		//previewPane.setContentType("text/html");
		
		/*
		if (rssFeed){
			JSplitPane downloadSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,new JScrollPane(downloads),previewPane);
			downloadSplit.setDividerLocation(300);
			add(downloadSplit);
		} else {*/
			add(new JScrollPane(downloads));
		//}
	}
	
	public DefaultTableModel getDownloads(){
		return downloadList;
	}
	
	public void setPreviewPane(String url){
		/*	if (url != null)
			previewPane.setText(url);
		try {
				
		} catch (IOException e) {
			e.printStackTrace();
		}*/ 
	}
	
	public void addDownload(String title, String date, String filename,String progress){
		Object newRow[];
		
		newRow = new Object[] {title,date,"0%"};
		downloadList.addRow(newRow);
		filenames.add(filename);
		if (downloadList.getValueAt(0, 0).toString().length()==0){
			downloadList.removeRow(0);
		}
	}
	
	public void downloadProgress(String filename, int progress){
		String newProgress = Integer.toString(progress)+"%";
		for (int dlc=0; dlc<downloadList.getRowCount(); dlc++){
			if (filenames.get(dlc).equals(filename))
				downloadList.setValueAt(newProgress, dlc, 2);
		}
	}
	
	public void addDownload(String newDownload){
		Object newRow[];
	
		if (newDownload != null){
			if (!isRssFeed){
				newRow = new Object[] {newDownload,"0%"};
				downloadList.addRow(newRow);
				if (downloadList.getValueAt(0, 0).toString().length()==0){
					downloadList.removeRow(0);
				}
			}
		}
	}
}
