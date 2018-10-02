package seedu.address.model.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class PriorityTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Priority(null));
    }

    @Test
    public void constructor_invalidPriority_throwsIllegalArgumentException() {
        String invalidPriority = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Priority(invalidPriority));
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
}
