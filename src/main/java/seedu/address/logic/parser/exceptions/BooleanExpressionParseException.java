package seedu.address.logic.parser.exceptions;

public class BooleanExpressionParseException extends ParseException {
    public BooleanExpressionParseException(String message) {
        super(message);
    }

    public BooleanExpressionParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
