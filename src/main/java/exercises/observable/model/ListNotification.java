package exercises.observable.model;

import java.util.List;

/**
 * Class representing the notifications passed to the listeners.
 *
 * Created by guisil on 09/08/2016.
 */
public class ListNotification<T> {

    private final ListNotificationType type;
    private final List<T> changedElements;
    private final List<T> resultingList;

    private ListNotification(
            ListNotificationType type, List<T> changedElements, List<T> resultingList) {
        this.type = type;
        this.changedElements = changedElements;
        this.resultingList = resultingList;
    }

    /**
     * Factory method for notifications of added elements to a list.
     * @param changedElements list of added elements
     * @param resultingList list after the addition of the elements
     * @param <T> type of the elements in the list
     * @return notification containing the given information
     */
    public static <T> ListNotification<T> newListAddNotification(List<T> changedElements, List<T> resultingList) {
        return new ListNotification<>(ListNotificationType.ADD, changedElements, resultingList);
    }

    /**
     * Factory method for notifications of removed elements from a list.
     * @param changedElements list of removed elements
     * @param resultingList list after the removal of the elements
     * @param <T> type of the elements in the list
     * @return notification containing the given information
     */
    public static <T> ListNotification<T> newListRemoveNotification(List<T> changedElements, List<T> resultingList) {
        return new ListNotification<>(ListNotificationType.REMOVE, changedElements, resultingList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ListNotification)) {
            return false;
        }
        ListNotification other = (ListNotification) obj;

        return (other.type == this.type
                && other.changedElements.equals(this.changedElements)
                && other.resultingList.equals(this.resultingList));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + type.hashCode();
        result = 31 * result + changedElements.hashCode();
        result = 31 * result + resultingList.hashCode();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Changed List Notification. ");
        if (ListNotificationType.ADD.equals(type)) {
            builder.append("Added ");
        } else {
            builder.append("Removed ");
        }
        builder.append("elements: ");
        builder.append(changedElements);
        builder.append(". Resulting list: ");
        builder.append(resultingList);
        return builder.toString();
    }
}
