package seedu.address.logic.parser.tokenizer.exceptions;

/**
 * Thrown when ArgumentMultimap encounters a duplicate prefix that is not allowed.
 */
public class DuplicatePrefixException extends TokenizationException {

    /**
     * Constructs a {@code DuplicatePrefixException}, saving a reference
     * to the error message string {@code s} for later retrieval by the
     * {@code getMessage} method.
     *
     * @param message the detail message.
     */
    public DuplicatePrefixException(String message) {
        super(message);
    }
}
