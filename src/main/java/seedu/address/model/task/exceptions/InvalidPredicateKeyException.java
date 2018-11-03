package seedu.address.model.task.exceptions;

/**
 * Signals that the predicate key (i.e. field) is invalid.
 */
public class InvalidPredicateKeyException extends InvalidPredicateException {
    public InvalidPredicateKeyException() {
        super();
    }

    public InvalidPredicateKeyException(Throwable cause) {
        super(cause);
    }
}
