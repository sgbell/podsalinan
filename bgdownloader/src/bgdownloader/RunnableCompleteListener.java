/**
 * 
 */
package bgdownloader;

/**
 * @author bugman
 *
 */
public interface RunnableCompleteListener {

	void notifyOfThreadComplete(final Runnable runnable);
}
