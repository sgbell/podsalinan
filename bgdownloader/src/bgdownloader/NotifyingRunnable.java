/**
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
