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
import seedu.address.model.task.Name;
import seedu.address.model.task.Task;
import seedu.address.model.task.exceptions.InvalidPredicateException;
import seedu.address.model.task.exceptions.InvalidPredicateOperatorException;
import seedu.address.model.task.exceptions.InvalidPredicateTestPhraseException;
import seedu.address.model.util.SetUtil;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    private static final String MESSAGE_INVALID_KEY_FORMAT = "Invalid key: %1$s";
    private static final String MESSAGE_INVALID_OPERATOR_FORMAT = "Invalid filter operator: %1$s";
    private static final String MESSAGE_INVALID_TESTPHRASE_FORMAT = "Invalid filter test value: %1$s";
    private static final String MESSAGE_INVALID_GENERAL_PREDICATE_FORMAT = "Invalid filter: %1$s";


    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand and returns an
     * FindCommand object for execution.
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

        final Predicate<Character> allowedKeyCharacterPredicate = ch
            -> (ch >= 'A' && ch <= 'Z')
            || (ch >= 'a' && ch <= 'z');

        try {
            BooleanExpressionParser<Task> expressionParser =
                new BooleanExpressionParser<>((tokenizer, reservedCharPredicate) -> {
                    final String key = tokenizer.nextString(allowedKeyCharacterPredicate);
                    final String opString = tokenizer.nextPattern(Pattern.compile("[\\=\\<\\>\\:]"));
                    final FilterOperator operator = FilterOperator.parse(opString);
                    final String value = tokenizer.nextString(reservedCharPredicate.negate());
                    try {
                        return createPredicate(key, operator, value);
                    } catch (InvalidPredicateException e) {
                        throw new ParseException(String.format(MESSAGE_INVALID_GENERAL_PREDICATE_FORMAT, key + ' '
                            + opString + ' ' + operator), e);
                    }
                });
            Predicate<Task> predicate = expressionParser.parse(args);

            return new FilterCommand(predicate);

        } catch (IllegalArgumentException | NoSuchElementException e) {
            throw new ParseException("Invalid filter expression: " + e.getMessage(), e);
        }

    }

    /**
     * Creates a predicate from the specified key, operator, and testphrase.
     */
    private static Predicate<Task> createPredicate(String key, FilterOperator operator, String testPhrase)
        throws ParseException, InvalidPredicateException {
        Predicate<Task> predicate;

        try {
            switch (key) {
            case "n": // fallthrough
            case "name": {
                Predicate<Name> namePredicate = Name.makeFilter(operator, testPhrase);
                predicate = task -> namePredicate.test(task.getName());
                break;
            }
            case "d": // fallthrough
            case "due": {
                Predicate<Deadline> deadlinePredicate = Deadline.makeFilter(operator, testPhrase);
                predicate = task -> deadlinePredicate.test(task.getDeadline());
                break;
            }
            case "t": // fallthrough
            case "tag": {
                Predicate<Set<Tag>> tagsPredicate = SetUtil.makeFilter(Tag.class, operator, testPhrase);
                predicate = task -> tagsPredicate.test(task.getTags());
                break;
            }
            default:
                throw new ParseException(String.format(MESSAGE_INVALID_KEY_FORMAT, key));
            }
        } catch (InvalidPredicateOperatorException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_OPERATOR_FORMAT, operator), e);
        } catch (InvalidPredicateTestPhraseException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_TESTPHRASE_FORMAT, testPhrase), e);
        }

        return predicate;
    }

}
