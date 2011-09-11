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

import java.awt.Component;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * @author bugman
 *
 */
@SuppressWarnings("serial")
public class ProgressCellRenderer extends JProgressBar implements TableCellRenderer {

	/**
	 * 
	 */
	public ProgressCellRenderer() {
		super(0, 100);
	    setValue(0);
	    setString("0%");
	    setStringPainted(true);
	}

	public Component getTableCellRendererComponent(
        JTable table,
        Object value,
        boolean isSelected,
        boolean hasFocus,
        int row,
        int column) {

		//value is a percentage e.g. 95%
		final String sValue = value.toString();
		int index = sValue.indexOf('%');
		if (index != -1) {
			int p = 0;
			try{
				p = Integer.parseInt(sValue.substring(0, index));
			}
			catch(NumberFormatException e){
			}
			setValue(p);
			setString(sValue);
		}
		return this;
	}
}
