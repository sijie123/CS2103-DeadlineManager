package seedu.address.logic.parser.tokenizer.exceptions;

/**
 * Thrown by a {@code StringTokenizer} to indicate that the token
 * retrieved does not match the pattern for the expected type, or
 * that the token is out of range for the expected type.
 */
public class TokenizationMismatchException extends TokenizationException {

    // Note: sometimes beginIndex might be equal to endIndex.
    // This indicates that the error is just a single location rather than a range.
    private final int beginIndex;
    private final int endIndex;

    /**
     * Constructs a {@code TokenizationMismatchException}, saving a reference
     * to the error message string {@code s} for later retrieval by the
     * {@code getMessage} method.
     *
     * @param beginIndex the begin index in the string.
     * @param endIndex the end index in the string.
     * @param message the detail message.
     */
    public TokenizationMismatchException(int beginIndex, int endIndex, String message) {
        super(message);
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }
}
