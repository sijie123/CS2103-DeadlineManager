package seedu.address.model.task.exceptions;

/**
 * Signals that the predicate is invalid.
 */
public class InvalidPredicateException extends Exception {
    public InvalidPredicateException() {
        super();
    }

    public InvalidPredicateException(String message) {
        super(message);
    }

    public InvalidPredicateException(Throwable cause) {
        super(cause);
    }

    public InvalidPredicateException(String message, Throwable cause) {
        super(message, cause);
    }
}
