package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Name;
import seedu.address.model.task.Task;
import seedu.address.model.task.exceptions.InvalidPredicateException;
import seedu.address.model.task.exceptions.InvalidPredicateOperatorException;
import seedu.address.model.task.exceptions.InvalidPredicateTestPhraseException;

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
        Pattern pattern = Pattern.compile("^([a-zA-Z]+)\\s*([\\=\\<\\>\\:])\\s*(\".+?\"|\'.+?\'|[\\S&&[^\"\']]+)$");
        Matcher matcher = pattern.matcher(trimmedArgs);

        if (!matcher.matches() || matcher.groupCount() != 3) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        final String key = matcher.group(1);
        final String operator = matcher.group(2);
        String value = matcher.group(3);
        if (value.startsWith("\"") || value.startsWith("\'")) {
            assert value.length() >= 2 : "Regex error, string length not more than 2!";
            value = value.substring(1, value.length() - 1);
        }
        final String testPhrase = value;

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
            default:
                throw new ParseException(String.format(MESSAGE_INVALID_KEY_FORMAT, key));
            }
        } catch (InvalidPredicateOperatorException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_OPERATOR_FORMAT, operator), e);
        } catch (InvalidPredicateTestPhraseException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_TESTPHRASE_FORMAT, testPhrase), e);
        } catch (InvalidPredicateException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_GENERAL_PREDICATE_FORMAT, trimmedArgs), e);
        }

        return new FilterCommand(predicate);
    }

}
