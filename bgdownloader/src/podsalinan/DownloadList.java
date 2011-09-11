/*******************************************************************************
 * Copyright (c) 2011 Sam Bell.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Sam Bell - initial API and implementation
 ******************************************************************************/
/**
 * 
 */
package podsalinan;

import java.awt.BorderLayout;
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
	//private Vector<String> filenames;
	//private JEditorPane previewPane;
	private boolean isRssFeed;
	private JTable table;
	
	public DownloadList(boolean rssFeed){
		super();

		//filenames = new Vector<String>();
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
		table = new JTable(downloadList){
		    public boolean isCellEditable(int rowIndex, int vColIndex) {
		        return false;
		    }
		};
		
		// removing grid from table
		table.setShowGrid(false);
		
		table.setRowSelectionAllowed(true);
		
		if (rssFeed) {
			Object newRow[] = new Object [] {"","","0%"};
			downloadList.addRow(newRow);
			downloadList.removeRow(0);
			TableColumn myCol = table.getColumnModel().getColumn(2);
			myCol.setCellRenderer(new ProgressCellRenderer());
		} else {
			Object newRow[] = new Object [] {"","0%"};
			downloadList.addRow(newRow);
			downloadList.removeRow(0);
			TableColumn myCol = table.getColumnModel().getColumn(1);
			myCol.setCellRenderer(new ProgressCellRenderer());
		}
		
		add(new JScrollPane(table));
	}
	
	public String removeDownload(){
		int row = table.getSelectedRow();
		String url = (String) downloadList.getValueAt(row, 0);
		downloadList.removeRow(row);
		
		return url;
	}
	
	public void setPreviewPane(String url){
		/*	if (url != null)
			previewPane.setText(url);
		try {
				
		} catch (IOException e) {
			e.printStackTrace();
		}*/ 
	}
	
	/** Used for Adding a download in a podcast
	 * 
	 * @param title
	 * @param date
	 * @param filename
	 * @param progress
	 */
	public void addDownload(String title, String date, String filename,String progress){
		Object newRow[];
		
		if (filename!=null){
			if (isRssFeed){
				newRow = new Object[] {title,date,"0%"};
				downloadList.addRow(newRow);
				//filenames.add(filename);
				if (downloadList.getValueAt(0, 0).toString().length()==0){
					downloadList.removeRow(0);
				}			
			}
		}
	}
	
	/** Used for Adding a stand-alone download
	 * 
	 * @param newDownload
	 */
	public void addDownload(String newDownload){
		Object newRow[];
	
		if (newDownload != null){
			// Adding just the filename to the window
			newDownload=newDownload.substring(newDownload.lastIndexOf('/')+1);
			if (!isRssFeed){
				newRow = new Object[] {newDownload,"0%"};
				downloadList.addRow(newRow);
				if (downloadList.getValueAt(0, 0).toString().length()==0){
					downloadList.removeRow(0);
				}
			}
		}
	}
	
	public void downloadProgress(int dlc, int progress){
		String newProgress = Integer.toString(progress)+"%";
		//for (int dlc=0; dlc<downloadList.getRowCount(); dlc++){
			//if (filenames.get(dlc).equals(filename))
			// Need another way to find the right one
		if (isRssFeed)
			downloadList.setValueAt(newProgress, dlc, 2);
		else
			downloadList.setValueAt(newProgress, dlc, 1);
		//}
	}
	
}
