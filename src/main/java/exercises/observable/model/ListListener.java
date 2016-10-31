package exercises.observable.model;

/**
 * Interface for the list listeners.
 *
 * Created by guisil on 09/08/2016.
 */
public interface ListListener<T> {

    /**
     * Method through which the listener is notified of a change in the list.
     * @param notification notification object
     */
    void onListChange(ListNotification<T> notification);
}
