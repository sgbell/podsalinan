/** This is from code found at: http://stackoverflow.com/questions/702415/how-to-know-if-other-threads-have-finished
 *  For proper handling of threads on end of execution.
 * 
 */
package podsalinan;

/**
 * @author bugman
 *
 */
public interface RunnableCompleteListener {

	void notifyOfThreadComplete(final Runnable runnable);
}
