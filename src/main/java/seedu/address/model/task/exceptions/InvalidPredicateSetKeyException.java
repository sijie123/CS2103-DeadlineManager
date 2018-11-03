package seedu.address.model.task.exceptions;

/**
 * Signals that the predicate key (i.e. field) is not allowed as a set-based filter predicate.
 */
public class InvalidPredicateSetKeyException extends InvalidPredicateException {
    public InvalidPredicateSetKeyException() {
        super();
    }

    public InvalidPredicateSetKeyException(Throwable cause) {
        super(cause);
    }
}
