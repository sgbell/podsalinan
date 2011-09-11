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
/** This is from code found at: http://stackoverflow.com/questions/702415/how-to-know-if-other-threads-have-finished
 *  For proper handling of threads on end of execution.
 * 
 */
package podsalinan;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author bugman
 *
 */
public abstract class NotifyingRunnable implements Runnable{

	private final Set<RunnableCompleteListener> listeners =
			new CopyOnWriteArraySet<RunnableCompleteListener>();
	
	public final void addListener(final RunnableCompleteListener listener) {
		listeners.add(listener);
	}
	
	public final void notifyListeners(){
		for (RunnableCompleteListener listener : listeners) {
			listener.notifyOfThreadComplete(this);
		}
	}
	
	@Override
	public final void run(){
		try {
			doRun();
		} finally {
			notifyListeners();
		}
	}
	
	public abstract void doRun();
}
