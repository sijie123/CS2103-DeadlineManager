package seedu.address.logic.parser.tokenizer.exceptions;

import seedu.address.model.task.exceptions.InvalidPredicateException;

/**
 * Represents an exception that signals that an InvalidPrdicateException was thrown by makeFilter().
 */
public class TokenizationInvalidPredicateException extends TokenizationMismatchException {

    private final InvalidPredicateException predicateException;

    /**
     * Constructs a {@code TokenizationInvalidPredicateException}, saving a reference
     * to the error message string {@code s} for later retrieval by the
     * {@code getMessage} method.
     *
     * @param beginIndex the begin index in the string.
     * @param endIndex the end index in the string.
     * @param message the detail message.
     */
    public TokenizationInvalidPredicateException(int beginIndex, int endIndex, String message,
                                                 InvalidPredicateException predicateException) {
        super(beginIndex, endIndex, message);
        this.predicateException = predicateException;
    }

    public InvalidPredicateException getPredicateException() {
        return predicateException;
    }
}
