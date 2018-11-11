package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.model.task.FilterOperator;
import seedu.address.model.task.exceptions.InvalidPredicateOperatorException;
import seedu.address.model.task.exceptions.InvalidPredicateTestPhraseException;

/**
 * Represents a Tag in the deadline manager. Guarantees: immutable; name is valid as declared in {@link
 * #isValidTagName(String)}
 */
public class Tag {

    public static final String MESSAGE_TAG_CONSTRAINTS = "Tags names should be alphanumeric";
    public static final String TAG_VALIDATION_REGEX = "\\p{Alnum}+";

    public final String tagName;

    /**
     * Constructs a {@code Tag}.
     *
     * @param tagName A valid tag name.
     */
    public Tag(String tagName) {
        requireNonNull(tagName);
        checkArgument(isValidTagName(tagName), MESSAGE_TAG_CONSTRAINTS);
        this.tagName = tagName;
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test) {
        return test.matches(TAG_VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Tag // instanceof handles nulls
                && tagName.equals(((Tag) other).tagName)); // state check
    }

    @Override
    public int hashCode() {
        return tagName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + tagName + ']';
    }

    /**
     * Constructs a predicate from the given operator and test phrase.
     *
     * @param operator   The operator for this predicate.
     * @param testPhrase The test phrase for this predicate.
     */
    public static Predicate<Tag> makeFilter(FilterOperator operator, String testPhrase)
            throws InvalidPredicateTestPhraseException, InvalidPredicateOperatorException {
        if (!isValidTagName(testPhrase)) {
            throw new InvalidPredicateTestPhraseException();
        }
        switch (operator) {
        case EQUAL:
            return tag -> StringUtil.equalsIgnoreCase(tag.tagName, testPhrase);
        case LESS:
            return tag -> StringUtil.containsFragmentIgnoreCase(testPhrase, tag.tagName);
        case CONVENIENCE: // convenience operator, works the same as ">"
            //Fallthrough
        case GREATER:
            return tag -> StringUtil.containsFragmentIgnoreCase(tag.tagName, testPhrase);
        default:
            throw new InvalidPredicateOperatorException();
        }
    }

}
