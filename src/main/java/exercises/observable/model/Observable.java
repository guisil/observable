package exercises.observable.model;

/**
 * Interface for observable classes.
 *
 * Created by guisil on 09/08/2016.
 */
public interface Observable<T, L> {

    /**
     * Registers the given listener in this Observable.
     * @param listener listener to register
     */
    void register(L listener);

    /**
     * Unregisters the given listener from this Observable.
     * @param listener listener to unregister
     */
    void unregister(L listener);

    /**
     * Sends the given notification to all the registered listeners.
     * @param notification notification to send
     */
    void notifyAllListeners(ListNotification<T> notification);
}
