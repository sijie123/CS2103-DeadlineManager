package seedu.address.logic.parser.tokenizer.exceptions;

/**
 * Thrown by a {@code BooleanExpressionParser} to indicate that a left bracket has no matching right bracket.
 */
public class BooleanExpressionMismatchedLeftBracketException extends TokenizationMismatchException {

    /**
     * Constructs a {@code BooleanExpressionMismatchedLeftBracketException}, saving a reference
     * to the error message string {@code s} for later retrieval by the
     * {@code getMessage} method.
     *
     * @param beginIndex the begin index in the string.
     * @param endIndex the end index in the string.
     * @param message the detail message.
     */
    public BooleanExpressionMismatchedLeftBracketException(int beginIndex, int endIndex, String message) {
        super(beginIndex, endIndex, message);
    }
}
