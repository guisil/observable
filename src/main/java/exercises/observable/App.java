package exercises.observable;

import exercises.observable.implementation.ObservableList;
import exercises.observable.implementation.TestListListener;
import exercises.observable.model.ListListener;

import java.util.Arrays;
import java.util.List;

/**
 * Class to be used as a sample for the features of ObservableList.
 *
 * Created by guisil on 09/08/2016.
 */
public class App {

    private static final String firstString = "One";
    private static final String secondString = "Two";
    private static final String thirdString = "Three";
    private static final String fourthString = "Four";
    private static final String fifthString = "Five";
    private static final String sixthString = "Six";
    private static final String seventhString = "Seven";
    private static final List<String> someStrings = Arrays.asList(firstString, secondString, thirdString, fourthString);
    private static final List<String> extraStrings = Arrays.asList(fifthString, sixthString, seventhString);

    public static void main(String[] args) {

        // creating lists
        ObservableList<String> firstObservableList = new ObservableList<>("First List");
        ObservableList<String> secondObservableList = new ObservableList<>("Second List");
        ObservableList<String> thirdObservableList = new ObservableList<>("Third List");

        // creating listeners
        ListListener<String> firstListener = new TestListListener<>("First Listener");
        ListListener<String> secondListener = new TestListListener<>("Second Listener");
        ListListener<String> thirdListener = new TestListListener<>("Third Listener");
        ListListener<String> fourthListener = new TestListListener<>("Fourth Listener");

        List<ListListener<String>> allListeners =
                Arrays.asList(firstListener, secondListener, thirdListener, fourthListener);


        // registering some listeners in the lists
        firstObservableList.register(firstListener);
        firstObservableList.register(secondListener);
        secondObservableList.register(secondListener);
        secondObservableList.register(thirdListener);

        // adding some elements to the lists
        firstObservableList.add(firstString);
        firstObservableList.add(secondString);
        secondObservableList.add(thirdString);
        secondObservableList.add(fourthString);

        // unregistering a listener
        secondObservableList.unregister(secondListener);

        // removing some elements
        firstObservableList.remove(firstString);
        secondObservableList.remove(fourthString);

        // registering another listener
        secondObservableList.register(fourthListener);

        // clearing a list
        firstObservableList.removeAll(someStrings);

        // adding a collection to a list
        secondObservableList.addAll(extraStrings);

        // unregistering another listener
        secondObservableList.unregister(fourthListener);

        // adding an element retrieved from another list
        firstObservableList.add(secondObservableList.get(0));

        // register another listener in another list
        thirdObservableList.register(fourthListener);

        // adding elements to that list
        thirdObservableList.add(0, fifthString);
        thirdObservableList.addAll(1, someStrings);

        // retaining all elements from a collection
        thirdObservableList.retainAll(extraStrings);

        // removing all the elements that match the given criterion
        thirdObservableList.removeIf(element -> element.startsWith("F"));

        // clearing all lists
        firstObservableList.clear();
        secondObservableList.clear();
        thirdObservableList.clear();

        // unregistering all listeners
        for (ListListener<String> listener : allListeners) {
            firstObservableList.unregister(listener);
            secondObservableList.unregister(listener);
            thirdObservableList.unregister(listener);
        }
    }
}
