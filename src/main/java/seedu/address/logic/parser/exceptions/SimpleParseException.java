package seedu.address.logic.parser.exceptions;

import seedu.address.ui.ResultDisplay;

/**
 * Represents a parse error encountered by a parser that contains a single text string.
 */
public class SimpleParseException extends ParseException {

    public SimpleParseException(String message) {
        super(message, ResultDisplay.TEXT_STYLE_CLASS_DEFAULT);
    }

    public SimpleParseException(String message, Throwable cause) {
        super(cause, message, ResultDisplay.TEXT_STYLE_CLASS_DEFAULT);
    }
}
