package seedu.address.logic.parser.tokenizer.exceptions;

/**
 * Thrown by a {@code StringTokenizer} to indicate an exception from the StringTokenizer.
 */
public class TokenizationException extends Exception {

    /**
     * Constructs a {@code TokenizationException}, saving a reference
     * to the error message string {@code s} for later retrieval by the
     * {@code getMessage} method.
     *
     * @param message the detail message.
     */
    public TokenizationException(String message) {
        super(message);
    }
}
