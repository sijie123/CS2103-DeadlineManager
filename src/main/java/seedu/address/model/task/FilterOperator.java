package seedu.address.model.task;

import seedu.address.model.task.exceptions.InvalidPredicateOperatorException;

/**
 * Represents an operator used for the filter predicate.
 */
public enum FilterOperator {
    CONVENIENCE {
        public String toString() {
            return ":";
        }
    },
    EQUAL {
        public String toString() {
            return "=";
        }
    },
    LESS {
        public String toString() {
            return "<";
        }
    },
    GREATER {
        public String toString() {
            return ">";
        }
    };

    /**
     * Parses a string into a filter operator.
     *
     * @param str The string to parse.
     */
    public static FilterOperator parse(String str) throws InvalidPredicateOperatorException {
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
            throw new InvalidPredicateOperatorException();
        }
    }
}
