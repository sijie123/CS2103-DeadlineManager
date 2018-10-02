package seedu.address.model.task;

import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Represents an operator used for the filter predicate.
 */
public enum FilterOperator {
    CONVENIENCE,
    EQUAL,
    LESS,
    GREATER;

    /**
     * Parses a string into a filter operator.
     *
     * @param str The string to parse.
     */
    public static FilterOperator parse(String str) throws ParseException {
        switch(str) {
        case ":":
            return CONVENIENCE;
        case "=":
            return EQUAL;
        case "<":
            return LESS;
        case ">":
            return GREATER;
        default:
            throw new ParseException("Invalid filter operator!");
        }
    }
}
