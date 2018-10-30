package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.FilterOperator;
import seedu.address.model.task.Frequency;
import seedu.address.model.task.Name;
import seedu.address.model.task.Priority;
import seedu.address.model.task.Task;
import seedu.address.model.task.exceptions.InvalidPredicateException;
import seedu.address.model.task.exceptions.InvalidPredicateOperatorException;
import seedu.address.model.task.exceptions.InvalidPredicateTestPhraseException;
import seedu.address.model.util.SetUtil;

/**
 * Parses input arguments and creates a new FilterCommand object
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    private static final String MESSAGE_INVALID_KEY_FORMAT = "Invalid key: %1$s";
    private static final String MESSAGE_INVALID_OPERATOR_FORMAT = "Invalid filter operator: %1$s";
    private static final String MESSAGE_INVALID_TESTPHRASE_FORMAT = "Invalid filter test value: %1$s";
    private static final String MESSAGE_INVALID_GENERAL_PREDICATE_FORMAT = "Invalid filter: %1$s";

    private static final Predicate<Task> ALWAYS_FALSE = task -> false;

    private static Predicate<Task> createNamePredicate(FilterOperator operator, String testPhrase)
        throws InvalidPredicateOperatorException {
        Predicate<Name> namePredicate = Name.makeFilter(operator, testPhrase);
        return task -> namePredicate.test(task.getName());
    }

    private static Predicate<Task> createDeadlinePredicate(FilterOperator operator, String testPhrase)
        throws InvalidPredicateException {
        Predicate<Deadline> deadlinePredicate = Deadline.makeFilter(operator, testPhrase);
        return task -> deadlinePredicate.test(task.getDeadline());
    }

    private static Predicate<Task> createPriorityPredicate(FilterOperator operator, String testPhrase)
        throws InvalidPredicateException {
        Predicate<Priority> priorityPredicate = Priority.makeFilter(operator, testPhrase);
        return task -> priorityPredicate.test(task.getPriority());
    }

    private static Predicate<Task> createFrequencyPredicate(FilterOperator operator, String testPhrase)
        throws InvalidPredicateException {
        Predicate<Frequency> frequencyPredicate = Frequency.makeFilter(operator, testPhrase);
        return task -> frequencyPredicate.test(task.getFrequency());
    }

    private static Predicate<Task> createTagsPredicate(FilterOperator operator, String testPhrase)
        throws InvalidPredicateException {
        Predicate<Set<Tag>> tagsPredicate = SetUtil.makeFilter(Tag.class, operator, testPhrase);
        return task -> tagsPredicate.test(task.getTags());
    }

    /**
     * A functional interface that represents a supplier than can throw InvalidPredicateException.
     */
    @FunctionalInterface
    private interface ExceptionalSupplier<T> {
        T get() throws InvalidPredicateException;
    }

    /**
     * Invokes the supplier, and converts an InvalidPredicateException to a predicate that always returns false.
     *
     * @param supplier The supplier that either produces a predicate or throws an exception.
     * @return On success returns the predicate that is returned by the supplier,
     *         on failure returns a predicate that is always false.
     */
    private static Predicate<Task> silencePredicateException(ExceptionalSupplier<Predicate<Task>> supplier) {
        try {
            return supplier.get();
        } catch (InvalidPredicateException e) {
            return ALWAYS_FALSE;
        }
    }

    /**
     * Creates a predicate from the specified key, operator, and test phrase.
     *
     * @param key        The key that refers to the specific field in a task.
     * @param operator   The filter operator.
     * @param testPhrase The test phrase to compare with the specific field of each task.
     * @return The predicate that is created.
     */
    private static Predicate<Task> createPredicate(String key, FilterOperator operator, String testPhrase)
        throws ParseException, InvalidPredicateException {

        try {
            switch (key) {
            case "n": // fallthrough
            case "name": {
                return createNamePredicate(operator, testPhrase);
            }
            case "d": // fallthrough
            case "due": {
                return createDeadlinePredicate(operator, testPhrase);
            }
            case "p": // fallthrough
            case "priority": {
                return createPriorityPredicate(operator, testPhrase);
            }
            case "f": // fallthrough
            case "frequency": {
                return createFrequencyPredicate(operator, testPhrase);
            }
            case "t": // fallthrough
            case "tag": {
                return createTagsPredicate(operator, testPhrase);
            }
            default:
                throw new ParseException(String.format(MESSAGE_INVALID_KEY_FORMAT, key));
            }
        } catch (InvalidPredicateOperatorException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_OPERATOR_FORMAT, operator), e);
        } catch (InvalidPredicateTestPhraseException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_TESTPHRASE_FORMAT, testPhrase), e);
        }
    }

    /**
     * Creates a predicate matches any textual or date field in a task, using the convenience operator.
     * It does not match numeric fields because they clutter the results and are usually not intended by the user.
     *
     * @param testPhrase The test phrase to compare with the specific field of each task.
     */
    private static Predicate<Task> createPredicateAny(String testPhrase)
        throws InvalidPredicateException {
        return ALWAYS_FALSE
            .or(silencePredicateException(() -> createNamePredicate(FilterOperator.CONVENIENCE, testPhrase)))
            .or(silencePredicateException(() -> createDeadlinePredicate(FilterOperator.CONVENIENCE, testPhrase)))
            .or(silencePredicateException(() -> createTagsPredicate(FilterOperator.CONVENIENCE, testPhrase)));
    }



    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand and returns an
     * FilterCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public FilterCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        // pattern that matches things like:
        // due=1/10/2018
        // test<blah
        // name>"hello world"

        // note: '/' is necessary for dates, ',' is necessary for tags
        final Predicate<Character> allowedUnquotedCharacterPredicate = ch -> (ch >= 'A' && ch <= 'Z')
                                                                     || (ch >= 'a' && ch <= 'z')
                                                                     || (ch >= '0' && ch <= '9')
                                                                     || ch == '_'
                                                                     || ch == '-'
                                                                     || ch == '/'
                                                                     || ch == ',';

        final Predicate<Character> allowedKeyCharacterPredicate = ch -> (ch >= 'A' && ch <= 'Z')
                                                                || (ch >= 'a' && ch <= 'z');

        try {
            BooleanExpressionParser<Task> expressionParser =
                new BooleanExpressionParser<>((tokenizer, reservedCharPredicate) -> {
                    final Predicate<Character> effectiveUnquotedCharacterPredicate = reservedCharPredicate.negate()
                        .and(allowedUnquotedCharacterPredicate);

                    int tokenizerLocation = tokenizer.getLocation(); // get the location in case we need to rewind
                    final String key = tokenizer.tryNextString(allowedKeyCharacterPredicate);
                    String opString;
                    if (key != null && tokenizer.hasNextToken()
                        && (opString = tokenizer.tryNextPattern(Pattern.compile("[\\=\\<\\>\\:]"))) != null) {
                        final FilterOperator operator = FilterOperator.parse(opString);
                        final String testPhrase = tokenizer.nextString(effectiveUnquotedCharacterPredicate);
                        try {
                            return createPredicate(key, operator, testPhrase);
                        } catch (InvalidPredicateException e) { // note: this catch block can never happen
                            throw new ParseException(String.format(MESSAGE_INVALID_GENERAL_PREDICATE_FORMAT, key + ' '
                                + opString + ' ' + testPhrase), e);
                        }
                    } else {
                        tokenizer.setLocation(tokenizerLocation); // rewind the tokenizer
                        final String testPhrase = tokenizer.nextString(effectiveUnquotedCharacterPredicate);
                        try {
                            return createPredicateAny(testPhrase);
                        } catch (InvalidPredicateException e) { // note: this catch block can never happen
                            throw new ParseException(
                                String.format(MESSAGE_INVALID_GENERAL_PREDICATE_FORMAT, testPhrase), e);
                        }
                    }
                });
            Predicate<Task> predicate = expressionParser.parse(args);

            return new FilterCommand(predicate);

        } catch (IllegalArgumentException | NoSuchElementException e) {
            throw new ParseException("Invalid filter expression: " + e.getMessage(), e);
        }

    }

}
