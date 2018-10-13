package seedu.address.model.task;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Task's priority in the deadline manager. Guarantees: immutable; is valid as declared
 * in {@link #isValidPriority(String)}
 */
public class Priority implements Comparable<Priority> {


    public static final String MESSAGE_PRIORITY_CONSTRAINTS =
        "Priority should only be 1, 2, 3, or 4";
    public static final String PRIORITY_VALIDATION_REGEX = "[1234]";
    public final String value;

    /**
     * Constructs a {@code Priority}.
     *
     * @param priority A valid priority.
     */
    public Priority(String priority) {
        requireNonNull(priority);
        checkArgument(isValidPriority(priority), MESSAGE_PRIORITY_CONSTRAINTS);
        value = priority;
    }

    /**
     * Returns true if a given string is a valid priority number.
     */
    public static boolean isValidPriority(String test) {
        return test.matches(PRIORITY_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof Priority // instanceof handles nulls
            && value.equals(((Priority) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public int compareTo(Priority other) {
        return this.value.compareTo(other.value);
    }
}
