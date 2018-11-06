package seedu.address.model.task;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.function.Predicate;

import seedu.address.model.task.exceptions.InvalidPredicateOperatorException;
import seedu.address.model.task.exceptions.InvalidPredicateTestPhraseException;

/**
 * Represents a Task's priority in the deadline manager. Guarantees: immutable; is valid as declared
 * in {@link #isValidPriority(String)}
 */
public class Priority implements Comparable<Priority> {


    public static final String MESSAGE_PRIORITY_CONSTRAINTS =
            "Priority should only be 0, 1, 2, 3, or 4";
    public static final String PRIORITY_VALIDATION_REGEX = "[01234]";
    public static final String NO_PRIORITY = "0";
    public static final int LARGEST_PRIORITY_VALUE = 4;
    public final int value;

    /**
     * Constructs a {@code Priority}.
     *
     * @param priority A valid priority.
     */
    public Priority(String priority) {
        requireNonNull(priority);
        checkArgument(isValidPriority(priority), MESSAGE_PRIORITY_CONSTRAINTS);
        this.value = Integer.parseInt(priority);
    }

    public Priority(int priority) {
        checkArgument(isValidPriority(priority), MESSAGE_PRIORITY_CONSTRAINTS);
        this.value = priority;
    }

    /**
     * Returns true if a given string is a valid priority number.
     */
    public static boolean isValidPriority(int test) {
        return test >= 0 && test <= LARGEST_PRIORITY_VALUE;
    }

    /**
     * Returns true if a given string is a valid priority number.
     */
    public static boolean isValidPriority(String test) {
        return test.matches(PRIORITY_VALIDATION_REGEX);
    }

    /**
     * Constructs a predicate from the given operator and test phrase.
     *
     * @param operator   The operator for this predicate.
     * @param testPhrase The test phrase for this predicate.
     */
    public static Predicate<Priority> makeFilter(FilterOperator operator, String testPhrase)
            throws InvalidPredicateTestPhraseException, InvalidPredicateOperatorException {
        Priority tmpPriority;
        try {
            tmpPriority = new Priority(testPhrase);
        } catch (IllegalArgumentException e) {
            throw new InvalidPredicateTestPhraseException(e);
        }
        switch (operator) {
        case EQUAL:
            return priority -> priority.equals(tmpPriority);
        case LESS:
            return priority -> priority.compareTo(tmpPriority) <= 0;
        case CONVENIENCE: // convenience operator, works the same as ">"
        case GREATER:
            return priority -> priority.compareTo(tmpPriority) >= 0;
        default:
            throw new InvalidPredicateOperatorException();
        }
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Priority // instanceof handles nulls
                    && value == ((Priority) other).value); // state check
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

    @Override
    public int compareTo(Priority other) {
        int booleanCompareResult = Boolean.compare(this.value > 0, other.value > 0);
        if (booleanCompareResult != 0) {
            return booleanCompareResult;
        }
        return Integer.compare(other.value, this.value);
    }
}
