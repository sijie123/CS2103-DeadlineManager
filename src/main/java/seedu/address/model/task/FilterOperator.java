package seedu.address.model.task;

import seedu.address.logic.parser.exceptions.ParseException;

public enum FilterOperator {
    CONVENIENCE,
    EQUAL,
    LESS,
    GREATER;

    public static FilterOperator parse(String str) throws ParseException {
        switch(str){
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
