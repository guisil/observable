package exercises.observable.implementation;

import exercises.observable.model.ListListener;
import exercises.observable.model.ListNotification;

/**
 * Implementation of the ListListener.
 *
 * Created by guisil on 09/08/2016.
 */
public class TestListListener<T> implements ListListener<T> {

    private final String name;

    public TestListListener(String name) {
        this.name = name;
    }

    /**
     * When a notification is received, the data that it carries is printed.
     * @param notification notification object
     */
    @Override
    public void onListChange(ListNotification<T> notification) {
        System.out.println(name + " - " + notification);
    }
}
