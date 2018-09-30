package seedu.address.model.task.exceptions;

/**
 * Signals that the predicate test phrase is invalid in the context of a particular field.
 */
public class InvalidPredicateTestPhraseException extends InvalidPredicateException {
    public InvalidPredicateTestPhraseException() {
        super();
    }

    public InvalidPredicateTestPhraseException(Throwable cause) {
        super(cause);
    }
}
