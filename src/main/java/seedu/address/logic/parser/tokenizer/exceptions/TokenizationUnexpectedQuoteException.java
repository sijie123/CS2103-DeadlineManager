package seedu.address.logic.parser.tokenizer.exceptions;

/**
 * Thrown by a {@code StringTokenizer} to indicate that the token
 * retrieved does not match the pattern for the expected type, or
 * that the token is out of range for the expected type.
 */
public class TokenizationUnexpectedQuoteException extends TokenizationMismatchException {

    /**
     * Constructs a {@code TokenizationUnexpectedQuoteException}, saving a reference
     * to the error message string {@code s} for later retrieval by the
     * {@code getMessage} method.
     *
     * @param beginIndex the begin index in the string.
     * @param endIndex the end index in the string.
     * @param message the detail message.
     */
    public TokenizationUnexpectedQuoteException(int beginIndex, int endIndex, String message) {
        super(beginIndex, endIndex, message);
    }
}
