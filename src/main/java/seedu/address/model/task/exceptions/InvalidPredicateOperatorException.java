package seedu.address.model.task.exceptions;

/**
 * Signals that the predicate operator is invalid in the context of a particular field.
 */
public class InvalidPredicateOperatorException extends InvalidPredicateException {
    public InvalidPredicateOperatorException() {
        super();
    }

    public InvalidPredicateOperatorException(Throwable cause) {
        super(cause);
    }
}
