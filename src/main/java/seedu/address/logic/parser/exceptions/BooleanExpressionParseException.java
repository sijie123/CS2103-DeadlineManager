package seedu.address.logic.parser.exceptions;

/**
 * Represents an exception that is caused by parsing a boolean expression.
 * This exception is thrown by methods in BooleanExpressionParser.
 */
public class BooleanExpressionParseException extends ParseException {
    public BooleanExpressionParseException(String message) {
        super(message);
    }

    public BooleanExpressionParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
