package seedu.address.logic.parser.exceptions;

import seedu.address.ui.ResultDisplay;

/**
 * Represents an exception that is caused by parsing a boolean expression.
 * This exception is thrown by methods in BooleanExpressionParser.
 */
public class BooleanExpressionParseException extends RichParseException {
    public BooleanExpressionParseException(String message) {
        super(message, ResultDisplay.TEXT_STYLE_CLASS_DEFAULT);
    }

    public BooleanExpressionParseException(String message, Throwable cause) {
        super(cause, message, ResultDisplay.TEXT_STYLE_CLASS_DEFAULT);
    }
}
