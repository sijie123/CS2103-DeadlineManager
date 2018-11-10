package seedu.address.logic.parser.exceptions;

import static java.util.Objects.checkIndex;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.ui.ResultDisplay;

/**
 * Represents a parse error encountered by a parser.  It can contain rich formatting.
 */
public class ParseException extends IllegalValueException {

    public final List<ResultDisplay.StyledText> message;

    public ParseException(String... parts) {
        this(makeMessage(parts));
    }

    public ParseException(Throwable cause, String... parts) {
        this(makeMessage(parts), cause);
    }

    public ParseException(List<ResultDisplay.StyledText> message) {
        this(message, null);
    }

    public ParseException(List<ResultDisplay.StyledText> message, Throwable cause) {
        super(getTextMessage(message), cause);
        this.message = message;
    }

    private static String getTextMessage(List<ResultDisplay.StyledText> message) {
        requireNonNull(message);
        StringBuilder sb = new StringBuilder();
        for (ResultDisplay.StyledText styledText : message) {
            sb.append(styledText.text);
        }
        return sb.toString();
    }

    /**
     * Converts a list of text parts and associated style classes into the whole rich message.
     */
    private static List<ResultDisplay.StyledText> makeMessage(String[] parts) {
        assert parts.length % 2 == 0;
        assert parts.length % 2 == 0;
        List<ResultDisplay.StyledText> result = new ArrayList<>();
        for (int i = 0; i < parts.length; i += 2) {
            requireNonNull(parts[i]);
            checkIndex(i + 1, parts.length);
            requireNonNull(parts[i + 1]);
            result.add(new ResultDisplay.StyledText(parts[i], parts[i + 1]));
        }
        return result;
    }
}
