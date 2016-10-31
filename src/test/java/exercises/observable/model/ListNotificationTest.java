package exercises.observable.model;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Test class for ListNotification.
 *
 * Created by guisil on 10/08/2016.
 */
public class ListNotificationTest {

    private static ListNotification<String> firstNotification,
            secondNotification, thirdNotification, fourthNotification;

    @BeforeClass
    public static void init() throws Exception {

        List<String> firstList = Arrays.asList("First String", "Second String");
        List<String> secondList = Arrays.asList("Second String", "Third String");

        List<String> firstResultingList = Arrays.asList("First String", "Second String");
        List<String> secondResultingList = Arrays.asList("First String", "Second String", "Third String");

        firstNotification = ListNotification.newListAddNotification(firstList, firstResultingList);
        secondNotification = ListNotification.newListAddNotification(firstList, firstResultingList);
        thirdNotification = ListNotification.newListAddNotification(firstList, firstResultingList);
        fourthNotification = ListNotification.newListAddNotification(secondList, secondResultingList);
    }

    @Test
    public void testEquals() throws Exception {
        assertThat(firstNotification)
                .as("Checking if equals is reflexive")
                .isEqualTo(firstNotification);
        assertThat(firstNotification.equals(secondNotification)
                && secondNotification.equals(firstNotification))
                .as("Checking if equals is symmetric")
                .isTrue();
        assertThat(firstNotification.equals(secondNotification)
                && secondNotification.equals(thirdNotification)
                && firstNotification.equals(thirdNotification))
                .as("Checking if equals is transitive")
                .isTrue();
        assertThat(firstNotification.equals(secondNotification)
                && firstNotification.equals(secondNotification)
                && firstNotification.equals(secondNotification))
                .as("Checking if equals is consistent")
                .isTrue();
        assertThat(firstNotification)
                .as("Checking equals with null parameter")
                .isNotEqualTo(null);
        assertThat(firstNotification)
                .as("Checking equals for different objects")
                .isNotEqualTo(fourthNotification);
    }

    @Test
    public void testHashCode() throws Exception {
        assertThat(firstNotification.hashCode())
                .as("Checking hashCode for the same object")
                .isEqualTo(firstNotification.hashCode());
        assertThat(firstNotification.hashCode())
                .as("Checking hashCode for equal objects")
                .isEqualTo(secondNotification.hashCode());
    }
}