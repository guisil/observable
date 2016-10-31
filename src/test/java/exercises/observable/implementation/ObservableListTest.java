package exercises.observable.implementation;

import exercises.observable.model.ListListener;
import exercises.observable.model.ListNotification;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.util.Lists.*;
import static org.mockito.Mockito.*;

/**
 * Test class for ObservableList.
 *
 * Created by guisil on 09/08/2016.
 */
public class ObservableListTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Mock
    private ListListener<String> firstListener;
    @Mock
    private ListListener<String> secondListener;

    private static final String firstString = "First String";
    private static final String secondString = "Second String";
    private static final String thirdString = "Third String";
    private static final String fourthString = "Fourth String";

    private ObservableList<String> stringList;


    @Before
    public void setUp() throws Exception {
         stringList = new ObservableList<>("First List");
    }


    @Test
    public void shouldAddElementAndSendNotification() throws Exception {

        // initialization
        final int expectedSize = 1;
        final ListNotification<String> expectedNotification =
                ListNotification.newListAddNotification(newArrayList(firstString), stringList);
        stringList.register(firstListener);
        stringList.register(secondListener);

        // call
        boolean result = stringList.add(firstString);

        // verifications
        verify(firstListener).onListChange(expectedNotification);
        verify(secondListener).onListChange(expectedNotification);

        // assertions
        assertThat(result)
                .as("Checking return value of the 'add' method")
                .isTrue();
        assertThat(stringList)
                .as("Checking the size of the list")
                .hasSize(expectedSize);
        assertThat(stringList)
                .as("Making sure the list only contains the added element")
                .containsOnly(firstString);
    }

    @Test
    public void shouldAddElementAndNotifyOnlyRegisteredListeners() throws Exception {

        // initialization
        final int expectedSize = 1;
        final ListNotification<String> expectedNotification =
                ListNotification.newListAddNotification(newArrayList(firstString), stringList);

        stringList.register(firstListener);
        stringList.register(secondListener);
        stringList.unregister(firstListener);

        // call
        boolean result = stringList.add(firstString);

        // verifications
        verifyZeroInteractions(firstListener);
        verify(secondListener).onListChange(expectedNotification);

        // assertions
        assertThat(result)
                .as("Checking return value of the 'add' method")
                .isTrue();
        assertThat(stringList)
                .as("Checking the size of the list")
                .hasSize(expectedSize);
        assertThat(stringList)
                .as("Making sure the list only contains the added element")
                .containsOnly(firstString);
    }

    @Test
    public void shouldAddElementOnIndexAndSendNotification() throws Exception {

        // initialization
        final int expectedSize = 2;
        final ListNotification<String> expectedNotification =
                ListNotification.newListAddNotification(newArrayList(secondString), stringList);
        stringList.add(firstString);
        final int expectedIndex = stringList.size();
        stringList.register(firstListener);
        stringList.register(secondListener);

        // call
        stringList.add(expectedIndex, secondString);

        // verifications
        verify(firstListener).onListChange(expectedNotification);
        verify(secondListener).onListChange(expectedNotification);

        // assertions
        assertThat(stringList)
                .as("Checking the size of the list")
                .hasSize(expectedSize);
        assertThat(stringList)
                .as("Making sure the list contains the expected elements")
                .containsOnly(firstString, secondString);
        assertThat(stringList.get(expectedIndex))
                .as("Making sure the added element is at the correct index")
                .isEqualTo(secondString);
    }

    @Test
    public void shouldAddCollectionAndSendNotification() throws Exception {

        // initialization
        final int expectedSize = 3;
        final List<String> listToAdd = newArrayList(secondString, thirdString);
        final ListNotification<String> expectedNotification =
                ListNotification.newListAddNotification(listToAdd, stringList);
        stringList.add(firstString);
        stringList.register(firstListener);
        stringList.register(secondListener);

        // call
        boolean result = stringList.addAll(listToAdd);

        // verifications
        verify(firstListener).onListChange(expectedNotification);
        verify(secondListener).onListChange(expectedNotification);

        // assertions
        assertThat(result)
                .as("Checking the return value of the 'addAll' method")
                .isTrue();
        assertThat(stringList)
                .as("Checking the size of the list")
                .hasSize(expectedSize);
        assertThat(stringList)
                .as("Making sure the list contains the expected elements")
                .containsOnly(firstString, secondString, thirdString);
    }

    @Test
    public void shouldNotSendNotificationWhenAddingEmptyCollection() throws Exception {

        // initialization
        final int expectedSize = 1;
        final List<String> listToAdd = newArrayList();
        stringList.add(firstString);
        stringList.register(firstListener);
        stringList.register(secondListener);

        // call
        boolean result = stringList.addAll(listToAdd);

        // verifications
        verifyZeroInteractions(firstListener);
        verifyZeroInteractions(secondListener);

        // assertions
        assertThat(result)
                .as("Checking the return value of the 'addAll' method")
                .isFalse();
        assertThat(stringList)
                .as("Checking the size of the list")
                .hasSize(expectedSize);
        assertThat(stringList)
                .as("Making sure the list contains the expected elements")
                .containsOnly(firstString);
    }

    @Test
    public void shouldAddCollectionOnIndexAndSendNotification() throws Exception {

        // initialization
        final int expectedSize = 3;
        final List<String> listToAdd = newArrayList(secondString, thirdString);
        final ListNotification<String> expectedNotification =
                ListNotification.newListAddNotification(listToAdd, stringList);
        stringList.add(firstString);
        final int expectedIndex = stringList.size();
        stringList.register(firstListener);
        stringList.register(secondListener);

        // call
        boolean result = stringList.addAll(expectedIndex, listToAdd);

        // verifications
        verify(firstListener).onListChange(expectedNotification);
        verify(secondListener).onListChange(expectedNotification);

        // assertions
        assertThat(result)
                .as("Checking the return value of the 'addAll' (with index) method")
                .isTrue();
        assertThat(stringList)
                .as("Checking the size of the list")
                .hasSize(expectedSize);
        assertThat(stringList)
                .as("Making sure the list contains the expected elements")
                .containsOnly(firstString, secondString, thirdString);
    }

    @Test
    public void shouldClearListAndSendNotification() throws Exception {

        // initialization
        final List<String> listToClear = newArrayList(firstString, secondString);
        final ListNotification<String> expectedNotification =
                ListNotification.newListRemoveNotification(listToClear, stringList);
        stringList.add(firstString);
        stringList.add(secondString);
        stringList.register(firstListener);
        stringList.register(secondListener);

        // call
        stringList.clear();

        // verifications
        verify(firstListener).onListChange(expectedNotification);
        verify(secondListener).onListChange(expectedNotification);

        // assertions
        assertThat(stringList)
                .as("Making sure the list is empty")
                .isEmpty();
    }

    @Test
    public void shouldNotSendNotificationsIfListWasAlreadyEmptyBeforeClearing() throws Exception {

        // initialization
        stringList.register(firstListener);
        stringList.register(secondListener);

        // call
        stringList.clear();

        // verifications
        verifyZeroInteractions(firstListener);
        verifyZeroInteractions(secondListener);

        // assertions
        assertThat(stringList)
                .as("Making sure the list is empty")
                .isEmpty();
    }

    @Test
    public void shouldRemoveElementAndSendNotification() throws Exception {

        // initialization
        final int expectedSize = 1;
        final List<String> removedList = newArrayList(firstString);
        final ListNotification<String> expectedNotification =
                ListNotification.newListRemoveNotification(removedList, stringList);
        stringList.add(firstString);
        stringList.add(secondString);
        stringList.register(firstListener);
        stringList.register(secondListener);

        // call
        boolean result = stringList.remove(firstString);

        // verifications
        verify(firstListener).onListChange(expectedNotification);
        verify(secondListener).onListChange(expectedNotification);

        // assertions
        assertThat(result)
                .as("Checking return value of the 'remove' method")
                .isTrue();
        assertThat(stringList)
                .as("Checking the size of the list")
                .hasSize(expectedSize);
        assertThat(stringList)
                .as("Making sure the list contains the expected elements")
                .containsOnly(secondString);
    }

    @Test
    public void shouldNotSendNotificationWhenElementNotRemoved() throws Exception {

        // initialization
        final int expectedSize = 2;
        stringList.add(firstString);
        stringList.add(secondString);
        stringList.register(firstListener);
        stringList.register(secondListener);

        // call
        boolean result = stringList.remove(thirdString);

        // verifications
        verifyZeroInteractions(firstListener);
        verifyZeroInteractions(secondListener);

        // assertions
        assertThat(result)
                .as("Checking return value of the 'remove' method")
                .isFalse();
        assertThat(stringList)
                .as("Checking the size of the list")
                .hasSize(expectedSize);
        assertThat(stringList)
                .as("Making sure the list contains the expected elements")
                .containsOnly(firstString, secondString);
    }

    @Test
    public void shouldRemoveElementOnIndexAndSendNotification() throws Exception {

        // initialization
        final int expectedSize = 1;
        final List<String> removedList = newArrayList(firstString);
        final ListNotification<String> expectedNotification =
                ListNotification.newListRemoveNotification(removedList, stringList);
        stringList.add(firstString);
        stringList.add(secondString);
        stringList.register(firstListener);
        stringList.register(secondListener);

        // call
        String result = stringList.remove(0);

        // verifications
        verify(firstListener).onListChange(expectedNotification);
        verify(secondListener).onListChange(expectedNotification);

        // assertions
        assertThat(result)
                .as("Checking the return value of the 'remove' (with index) method")
                .isEqualTo(firstString);
        assertThat(stringList)
                .as("Checking the size of the list")
                .hasSize(expectedSize);
        assertThat(stringList)
                .as("Making sure the list contains the expected elements")
                .containsOnly(secondString);
    }

    @Test
    public void shouldRemoveAllInCollectionAndSendNotification() throws Exception {

        // initialization
        final int expectedSize = 1;
        final List<String> listToUseAsArgument = newArrayList(firstString, thirdString, fourthString);
        final List<String> removedList = newArrayList(firstString, thirdString);
        final ListNotification<String> expectedNotification =
                ListNotification.newListRemoveNotification(removedList, stringList);
        stringList.add(firstString);
        stringList.add(secondString);
        stringList.add(thirdString);
        stringList.register(firstListener);
        stringList.register(secondListener);

        // call
        boolean result = stringList.removeAll(listToUseAsArgument);

        // verifications
        verify(firstListener).onListChange(expectedNotification);
        verify(secondListener).onListChange(expectedNotification);

        // assertions
        assertThat(result)
                .as("Checking return value of the 'removeAll' method")
                .isTrue();
        assertThat(stringList)
                .as("Checking the size of the list")
                .hasSize(expectedSize);
        assertThat(stringList)
                .as("Making sure the list contains the expected elements")
                .containsOnly(secondString);
    }

    @Test
    public void shouldNotSendNotificationWhenRemovingFromEmptyList() {

        // initialization
        final List<String> listToUseAsArgument = newArrayList(firstString, thirdString, fourthString);
        stringList.register(firstListener);
        stringList.register(secondListener);

        // call
        boolean result = stringList.removeAll(listToUseAsArgument);

        // verifications
        verifyZeroInteractions(firstListener);
        verifyZeroInteractions(secondListener);

        // assertions
        assertThat(result)
                .as("Checking return value of the 'removeAll' method")
                .isFalse();
        assertThat(stringList)
                .as("Making sure the list is empty")
                .isEmpty();
    }

    @Test
    public void shouldNotSendNotificationWhenPassingEmptyCollectionToRemove() {

        // initialization
        final int expectedSize = 1;
        final List<String> listToUseAsArgument = newArrayList();
        stringList.add(firstString);
        stringList.register(firstListener);
        stringList.register(secondListener);

        // call
        boolean result = stringList.removeAll(listToUseAsArgument);

        // verifications
        verifyZeroInteractions(firstListener);
        verifyZeroInteractions(secondListener);

        // assertions
        assertThat(result)
                .as("Checking return value of the 'removeAll' method")
                .isFalse();
        assertThat(stringList)
                .as("Checking the size of the list")
                .hasSize(expectedSize);
        assertThat(stringList)
                .as("Making sure the list contains the expected elements")
                .containsOnly(firstString);
    }

    @Test
    public void shouldRemoveIfPredicateIsTrueForSomeElementsAndSendNotification() throws Exception {

        // initialization
        final int expectedSize = 2;
        final List<String> removedList = newArrayList(firstString, fourthString);
        final ListNotification<String> expectedNotification =
                ListNotification.newListRemoveNotification(removedList, stringList);
        stringList.add(firstString);
        stringList.add(secondString);
        stringList.add(thirdString);
        stringList.add(fourthString);
        stringList.register(firstListener);
        stringList.register(secondListener);

        // call
        boolean result = stringList.removeIf(element -> element.startsWith("F"));

        // verifications
        verify(firstListener).onListChange(expectedNotification);
        verify(secondListener).onListChange(expectedNotification);

        // assertions
        assertThat(result)
                .as("Checking the return value of the 'removeIf' method")
                .isTrue();
        assertThat(stringList)
                .as("Checking the size of the list")
                .hasSize(expectedSize);
        assertThat(stringList)
                .as("Making sure the list contains the expected elements")
                .containsOnly(secondString, thirdString);
    }

    @Test
    public void shouldNotSendNotificationWhenPredicateIsAlwaysFalse() {

        // initialization
        final int expectedSize = 2;
        stringList.add(firstString);
        stringList.add(secondString);

        stringList.register(firstListener);
        stringList.register(secondListener);

        // call
        boolean result = stringList.removeIf(element -> element.startsWith("K"));

        // verifications
        verifyZeroInteractions(firstListener);
        verifyZeroInteractions(secondListener);

        // assertions
        assertThat(result)
                .as("Checking the return value of the 'removeIf' method")
                .isFalse();
        assertThat(stringList)
                .as("Checking the size of the list")
                .hasSize(expectedSize);
        assertThat(stringList)
                .as("Making sure the list contains the expected elements")
                .containsOnly(firstString, secondString);
    }

    @Test
    public void shouldNotSendNotificationWhenRemovingIfFromEmptyList() {

        // initialization
        stringList.register(firstListener);
        stringList.register(secondListener);

        // call
        boolean result = stringList.removeIf(element -> element.startsWith("K"));

        // verifications
        verifyZeroInteractions(firstListener);
        verifyZeroInteractions(secondListener);

        // assertions
        assertThat(result)
                .as("Checking return value of the 'removeIf' method")
                .isFalse();
        assertThat(stringList)
                .as("Making sure the list is empty")
                .isEmpty();
    }

    @Test
    public void shouldRemoveRangeAndSendNotification() throws Exception {

        // initialization
        final int expectedSize = 2;
        final List<String> removedList = newArrayList(secondString, thirdString);
        final ListNotification<String> expectedNotification =
                ListNotification.newListRemoveNotification(removedList, stringList);
        stringList.add(firstString);
        stringList.add(secondString);
        stringList.add(thirdString);
        stringList.add(fourthString);
        stringList.register(firstListener);
        stringList.register(secondListener);

        // call
        stringList.removeRange(1, 3);

        // verifications
        verify(firstListener).onListChange(expectedNotification);
        verify(secondListener).onListChange(expectedNotification);

        // assertions
        assertThat(stringList)
                .as("Checking the size of the list")
                .hasSize(expectedSize);
        assertThat(stringList)
                .as("Making sure the list contains the expected elements")
                .containsOnly(firstString, fourthString);
    }

    @Test
    public void shouldSendNotificationWhenRetainingAllInCollection() throws Exception {

        // initialization
        final int expectedSize = 2;
        final List<String> listToUseAsArgument = newArrayList(firstString, thirdString, fourthString);
        final List<String> removedList = newArrayList(secondString);
        final ListNotification<String> expectedNotification =
                ListNotification.newListRemoveNotification(removedList, stringList);
        stringList.add(firstString);
        stringList.add(secondString);
        stringList.add(thirdString);
        stringList.register(firstListener);
        stringList.register(secondListener);

        // call
        boolean result = stringList.retainAll(listToUseAsArgument);

        // verifications
        verify(firstListener).onListChange(expectedNotification);
        verify(secondListener).onListChange(expectedNotification);

        // assertions
        assertThat(result)
                .as("Checking the return value of the 'retainAll' method")
                .isTrue();
        assertThat(stringList)
                .as("Checking the size of the list")
                .hasSize(expectedSize);
        assertThat(stringList)
                .as("Making sure the list contains the expected elements")
                .containsOnly(firstString, thirdString);
    }

    @Test
    public void shouldSendNotificationWhenRetainingAllInEmptyCollection() throws Exception {

        // initialization
        final List<String> listToUseAsArgument = newArrayList();
        final List<String> removedList = newArrayList(firstString, secondString);
        final ListNotification<String> expectedNotification =
                ListNotification.newListRemoveNotification(removedList, stringList);
        stringList.add(firstString);
        stringList.add(secondString);
        stringList.register(firstListener);
        stringList.register(secondListener);

        // call
        boolean result = stringList.retainAll(listToUseAsArgument);

        // verifications
        verify(firstListener).onListChange(expectedNotification);
        verify(secondListener).onListChange(expectedNotification);

        // assertions
        assertThat(result)
                .as("Checking the return value of the 'retainAll' method")
                .isTrue();
        assertThat(stringList)
                .as("Making sure the list is empty")
                .isEmpty();
    }

    @Test
    public void shouldNotSendNotificationWhenRetainingFromEmptyList() {

        // initialization
        final List<String> listToUseAsArgument = newArrayList(firstString, thirdString, fourthString);
        stringList.register(firstListener);
        stringList.register(secondListener);

        // call
        boolean result = stringList.retainAll(listToUseAsArgument);

        // verifications
        verifyZeroInteractions(firstListener);
        verifyZeroInteractions(secondListener);

        // assertions
        assertThat(result)
                .as("Checking return value of the 'retainAll' method")
                .isFalse();
        assertThat(stringList)
                .as("Making sure the list is empty")
                .isEmpty();
    }

    @Test
    public void shouldNotSendNotificationWhenPassingSimilarListToRetainAll() {

        // initialization
        final int expectedSize = 2;
        final List<String> listToUseAsArgument = newArrayList(firstString, secondString);
        stringList.add(firstString);
        stringList.add(secondString);
        stringList.register(firstListener);
        stringList.register(secondListener);

        // call
        boolean result = stringList.retainAll(listToUseAsArgument);

        // verifications
        verifyZeroInteractions(firstListener);
        verifyZeroInteractions(secondListener);

        // assertions
        assertThat(result)
                .as("Checking return value of the 'retainAll' method")
                .isFalse();
        assertThat(stringList)
                .as("Checking the size of the list")
                .hasSize(expectedSize);
        assertThat(stringList)
                .as("Making sure the list contains the expected elements")
                .containsOnly(firstString, secondString);
    }
}