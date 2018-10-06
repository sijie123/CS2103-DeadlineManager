package seedu.address.model.util;

import java.lang.reflect.InvocationTargetException;
import java.util.InputMismatchException;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.StringTokenizer;
import seedu.address.model.task.FilterOperator;
import seedu.address.model.task.exceptions.InvalidPredicateException;
import seedu.address.model.task.exceptions.InvalidPredicateOperatorException;
import seedu.address.model.task.exceptions.InvalidPredicateTestPhraseException;

/**
 * Contains utility methods for manipulating Sets.
 * It is currently used to make filters for tags and attachments.
 */
public class SetUtil {
    /**
     * Constructs a predicate from the given operator and test phrase.
     * Test phrase should be a comma-separated list of values.
     *
     * @param klass      The class that this filter is being made for.
     * @param operator   The operator for this predicate.
     * @param testPhrase The test phrase for this predicate.
     */
    public static <T> Predicate<Set<T>> makeFilter(Class<T> klass, FilterOperator operator, String testPhrase)
        throws InvalidPredicateException {
        try {
            // comma-separated quotable tokenizer
            StringTokenizer tokenizer = new StringTokenizer(testPhrase, ch -> ch == ',', ch -> ch == '\'' || ch == '\"');
            Set<T> items = tokenizer.toList().stream().map(token -> {
                try {
                    return klass.getDeclaredConstructor(String.class).newInstance(token);
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalArgumentException("Class does not support construction from a single string", e);
                }
            }).collect(Collectors.toSet());
            switch (operator) {
            case EQUAL:
                return set -> set.equals(items);
            case LESS:
                return set -> set.stream().allMatch(item -> items.contains(item));
            case CONVENIENCE: // convenience operator, works the same as ">"
            case GREATER:
                return set -> items.stream().allMatch(item -> set.contains(item));
            default:
                throw new InvalidPredicateOperatorException();
            }
        } catch (InputMismatchException e) {
            throw new InvalidPredicateTestPhraseException(e);
        }
    }
}
