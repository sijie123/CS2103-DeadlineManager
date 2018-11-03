package seedu.address.model.task.exceptions;

/**
 * Signals that the set predicate operator is invalid in the context of a particular field.
 */
public class InvalidPredicateSetOperatorException extends InvalidPredicateException {
    public InvalidPredicateSetOperatorException() {
        super();
    }

    public InvalidPredicateSetOperatorException(Throwable cause) {
        super(cause);
    }
}
