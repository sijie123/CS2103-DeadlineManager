package seedu.address.model.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.logic.parser.tokenizer.StringTokenizer;
import seedu.address.logic.parser.tokenizer.exceptions.TokenizationException;
import seedu.address.model.task.FilterOperator;
import seedu.address.model.task.exceptions.InvalidPredicateOperatorException;
import seedu.address.model.task.exceptions.InvalidPredicateSetOperatorException;
import seedu.address.model.task.exceptions.InvalidPredicateTestPhraseException;

/**
 * Contains utility methods for manipulating Sets.
 * It is currently used to make filters for tags and attachments.
 */
public class SetUtil {
    /**
     * Constructs a predicate from the given set operator, given field operator and test phrase.
     * Test phrase should be a comma-separated list of values.
     *
     * @param clazz         The class that this filter is being made for.
     * @param setOperator   The set operator for this predicate.
     * @param fieldOperator The set operator for this predicate.
     * @param testPhrase    The test phrase for this predicate.
     *
     * @return The predicate that is constructed.
     */
    public static <T> Predicate<Set<T>> makeFilter(Class<T> clazz, FilterOperator setOperator,
            FilterOperator fieldOperator, String testPhrase)
            throws InvalidPredicateTestPhraseException, InvalidPredicateOperatorException,
            InvalidPredicateSetOperatorException, IllegalArgumentException {
        assert (testPhrase != null);
        assert (fieldOperator != null);
        assert (setOperator != null);

        List<Predicate<T>> predicates = parseTestPhrase(clazz, fieldOperator, testPhrase);

        final Predicate<Set<T>> lessPredicate = set -> set.stream().allMatch(item -> predicates.stream()
                .anyMatch(predicate -> predicate.test(item)));
        final Predicate<Set<T>> greaterPredicate = set -> predicates.stream()
                .allMatch(predicate -> set.stream().anyMatch(predicate));

        switch (setOperator) {
        case EQUAL:
            return lessPredicate.and(greaterPredicate);
        case LESS:
            return lessPredicate;
        case CONVENIENCE: // convenience operator, works the same as ">"
        case GREATER:
            return greaterPredicate;
        default:
            throw new InvalidPredicateSetOperatorException();
        }
    }

    /**
     * Constructs a list of predicates from the given field operator and test phrase that represents a set of values.
     *
     * @param clazz         The class that the filters are being made for.
     * @param fieldOperator The set operator for this predicate.
     * @param testPhrase    The test phrase for this predicate.
     *
     * @return The list of predicates that is constructed.
     */
    private static <T> List<Predicate<T>> parseTestPhrase(Class<T> clazz, FilterOperator fieldOperator,
            String testPhrase) throws InvalidPredicateTestPhraseException, InvalidPredicateOperatorException,
            IllegalArgumentException {
        // comma-separated quotable tokenizer
        StringTokenizer tokenizer = new StringTokenizer(testPhrase, ch -> ch == ',', ch -> ch == '\'' || ch == '\"');
        List<Predicate<T>> predicates = new ArrayList<>();
        try {
            for (String token : tokenizer.toList()) {
                token = token.trim();
                if (token.isEmpty()) {
                    throw new IllegalArgumentException("Tag cannot be empty");
                }
                predicates.add(makeFieldPredicate(clazz, fieldOperator, token));
            }
        } catch (TokenizationException e) {
            throw new InvalidPredicateTestPhraseException(e);
        }
        return predicates;
    }

    /**
     * Constructs a predicate from the given field operator and test phrase.
     *
     * @param clazz         The class that this filter is being made for.
     * @param fieldOperator The set operator for this predicate.
     * @param testPhrase    The test phrase for this predicate.
     *
     * @return The predicate that is constructed.
     */
    private static <T> Predicate<T> makeFieldPredicate(Class<T> clazz, FilterOperator fieldOperator, String testPhrase)
            throws InvalidPredicateTestPhraseException, InvalidPredicateOperatorException, IllegalArgumentException {
        assert (testPhrase != null);
        assert (fieldOperator != null);
        try {
            Method method = clazz.getMethod("makeFilter", FilterOperator.class, String.class);
            if ((method.getModifiers() & Modifier.STATIC) == 0) {
                // method is non-static
                throw new IllegalArgumentException(
                        "Class has a non-static makeFilter(FilterOperator, String) method, "
                        + "but we need a static method");
            }
            if (method.getReturnType() != Predicate.class) {
                // method returns the wrong type
                throw new IllegalArgumentException("Class returns the wrong type");
            }

            // Note: Unchecked cast because we are using reflection to call the method.
            @SuppressWarnings("unchecked")
            Predicate<T> returnPredicate = (Predicate<T>) method.invoke(null, fieldOperator, testPhrase);

            return returnPredicate;

        } catch (InvocationTargetException e) {
            // this happens when makeFilter() fails
            if (e.getCause() instanceof InvalidPredicateTestPhraseException) {
                throw (InvalidPredicateTestPhraseException) e.getCause();
            } else if (e.getCause() instanceof InvalidPredicateOperatorException) {
                throw (InvalidPredicateOperatorException) e.getCause();
            } else {
                throw new IllegalArgumentException("Unexpected exception thrown by makeFilter()", e.getCause());
            }
        } catch (NoSuchMethodException | IllegalArgumentException | IllegalAccessException e) {
            throw new IllegalArgumentException(
                    "Class does not have a static makeFilter(FilterOperator, String) method", e);
        }
    }
}
