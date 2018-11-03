package seedu.address.logic.parser.tokenizer.exceptions;

/**
 * Thrown by a {@code BooleanExpressionParser} to indicate that the string has ended unexpectedly.
 */
public class BooleanExpressionUnexpectedEndOfStringException extends TokenizationException {

    /**
     * Constructs a {@code BooleanExpressionUnexpectedEndOfStringException}, saving a reference
     * to the error message string {@code s} for later retrieval by the
     * {@code getMessage} method.
     *
     * @param message the detail message.
     */
    public BooleanExpressionUnexpectedEndOfStringException(String message) {
        super(message);
    }
}
