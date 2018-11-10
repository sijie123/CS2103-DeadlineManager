package seedu.address.logic.parser.tokenizer.exceptions;

/**
 * Thrown by a {@code StringTokenizer} to indicate that no characters matched the request.
 */
public class TokenizationNoMatchableCharacterException extends TokenizationMismatchException {

    /**
     * Constructs a {@code TokenizationNoMatchableCharacterException}, saving a reference
     * to the error message string {@code s} for later retrieval by the
     * {@code getMessage} method.
     *
     * @param beginIndex the begin index in the string.
     * @param endIndex the end index in the string.
     * @param message the detail message.
     */
    public TokenizationNoMatchableCharacterException(int beginIndex, int endIndex, String message) {
        super(beginIndex, endIndex, message);
    }
}
