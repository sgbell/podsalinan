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
/** This is from code found at: http://stackoverflow.com/questions/702415/how-to-know-if-other-threads-have-finished
 *  For proper handling of threads on end of execution.
 * 
 */
package com.mimpidev.podsalinan;

/**
 * @author bugman
 *
 */
public interface RunnableCompleteListener {

	void notifyOfThreadComplete(final Runnable runnable);
}
