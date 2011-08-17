/** This is from code found at: http://stackoverflow.com/questions/702415/how-to-know-if-other-threads-have-finished
 *  For proper handling of threads on end of execution.
 * 
 */
package bgdownloader;

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
