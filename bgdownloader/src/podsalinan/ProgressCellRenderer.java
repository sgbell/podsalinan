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
