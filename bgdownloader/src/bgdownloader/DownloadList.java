/**
 * 
 */
package bgdownloader;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
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
	private JEditorPane previewPane;
	
	public DownloadList(){
		super();
		setLayout(new BorderLayout());
		
		Object headers[] = {"Title",
							"Date",
							"Filename",
							"Progress"};
		
		downloadList = new DefaultTableModel(headers,1);
		JTable downloads = new JTable(downloadList);
		downloads.setRowSelectionAllowed(true);
		downloads.setShowGrid(false);

		
		Object newRow[] = new Object [] {"","","","0%"};
		
		downloadList.addRow(newRow);
		downloadList.removeRow(0);
		
		TableColumn myCol = downloads.getColumnModel().getColumn(3);
		myCol.setCellRenderer(new ProgressCellRenderer());
		
		previewPane = new JEditorPane();
		previewPane.setEditable(false);
		previewPane.setContentType("text/html");
		
		JSplitPane downloadSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,new JScrollPane(downloads),previewPane);
		downloadSplit.setDividerLocation(300);
		add(downloadSplit);
	}
	
	public DefaultTableModel getDownloads(){
		return downloadList;
	}
	
	public void setPreviewPane(String url){
		if (url != null)
			previewPane.setText(url);
	/*	try {
				
		} catch (IOException e) {
			e.printStackTrace();
		}*/ 
	}
}
