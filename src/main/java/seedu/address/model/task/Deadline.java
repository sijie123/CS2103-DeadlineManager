package seedu.address.model.task;

import static java.util.Objects.requireNonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Represents a Task's deadline in the deadline manager. Guarantees: immutable; represents a valid date
 */
public class Deadline {

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

        DateFormat formatter = DateFormat.getInstance();
        try {
            this.value = formatter.parse(deadline);
        } catch (ParseException e) {
            throw new IllegalArgumentException(MESSAGE_DEADLINE_CONSTRAINTS, e);
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
}
