package seedu.address.logic.parser.tokenizer.exceptions;

/**
 * Thrown by a {@code StringTokenizer} to indicate that the string has ended.
 */
public class TokenizationEndOfStringException extends TokenizationException {

    /**
     * Constructs a {@code TokenizationEndOfStringException}, saving a reference
     * to the error message string {@code s} for later retrieval by the
     * {@code getMessage} method.
     *
     * @param message the detail message.
     */
    public TokenizationEndOfStringException(String message) {
        super(message);
    }
}
