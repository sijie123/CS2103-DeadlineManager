package seedu.address.model.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class PriorityTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Priority((String) null));
    }

    @Test
    public void constructor_invalidPriority_throwsIllegalArgumentException() {
        String invalidPriority = "";
        Integer invalidPriorityInteger = -1;
        Assert.assertThrows(IllegalArgumentException.class, () -> new Priority(invalidPriority));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Priority(invalidPriorityInteger));
    }

    @Test
    public void isValidPriority() {
        // null priority number
        Assert.assertThrows(NullPointerException.class, () -> Priority.isValidPriority(null));

        // invalid priority numbers
        assertFalse(Priority.isValidPriority("")); // empty string
        assertFalse(Priority.isValidPriority(" ")); // spaces only
        assertFalse(Priority.isValidPriority("5")); // not 1, 2, 3, or 4

        // valid priority numbers
        assertTrue(Priority.isValidPriority("1")); // 1, 2, 3, or 4
        assertTrue(Priority.isValidPriority("2"));
        assertTrue(Priority.isValidPriority("3")); // 1, 2, 3, or 4
        assertTrue(Priority.isValidPriority("4"));
    }

    @Test
    public void isValidComparison() {
        Priority a = new Priority("3");
        Priority b = new Priority("4");
        Priority c = new Priority(3);
        assertTrue(a.compareTo(b) == 1);
        assertTrue(b.compareTo(a) == -1);
        assertTrue(a.compareTo(c) == 0);
    }
}
