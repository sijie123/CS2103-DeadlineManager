package seedu.address.model.task;

import static java.util.Objects.requireNonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.function.Predicate;

import seedu.address.model.task.exceptions.InvalidPredicateException;
import seedu.address.model.task.exceptions.InvalidPredicateOperatorException;
import seedu.address.model.task.exceptions.InvalidPredicateTestPhraseException;

/**
 * Represents a Task's deadline in the deadline manager. Guarantees: immutable; represents a valid date
 */
public class Deadline implements Comparable<Deadline> {

    public static final String MESSAGE_DEADLINE_CONSTRAINTS =
            "Deadline has to be a valid date";

    public final Date value;

    /**
     * Constructs an {@code Address}.
     *
     * @param deadline A valid deadline.
     */
    public Deadline(Date deadline) {
        requireNonNull(deadline);
        this.value = deadline;
    }

    /**
     * Constructs an {@code Address}.
     *
     * @param deadline A valid deadline.
     */
    public Deadline(String deadline) {
        requireNonNull(deadline);

        DateFormat formatter = new SimpleDateFormat("d/M/y", new Locale("en", "SG"));

        try {
            this.value = formatter.parse(deadline);
        } catch (ParseException e) {
            throw new IllegalArgumentException(MESSAGE_DEADLINE_CONSTRAINTS, e);
        }
    }

    /**
     * Constructs a predicate from the given operator and test phrase.
     *
     * @param operator The operator for this predicate.
     * @param testPhrase The test phrase for this predicate.
     */
    public static Predicate<Deadline> makeFilter(String operator, String testPhrase) throws InvalidPredicateException {
        Deadline testDeadline;
        try {
            testDeadline = new Deadline(testPhrase);
        } catch (IllegalArgumentException e) {
            throw new InvalidPredicateTestPhraseException(e);
        }
        switch (operator) {
        case "=":
            return deadline -> deadline.equals(testDeadline);
        case ":": // convenience operator, works the same as "<"
        case "<":
            return deadline -> deadline.compareTo(testDeadline) <= 0;
        case ">":
            return deadline -> deadline.compareTo(testDeadline) >= 0;
        default:
            throw new InvalidPredicateOperatorException();
        }
    }

    @Override
    public String toString() {
        DateFormat formatter = DateFormat.getInstance();
        return formatter.format(value);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Deadline // instanceof handles nulls
                && value.equals(((Deadline) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public int compareTo(Deadline other) {
        return this.value.compareTo(other.value);
    }
}
