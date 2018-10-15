package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Comparator;
import java.util.Set;

import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Task;

/**
 * Parses input arguments and creates a new SortCommand object
 */
public class SortCommandParser implements Parser<SortCommand> {

    private static final String MESSAGE_INVALID_KEY_FORMAT = "Invalid key: %1$s";

    /**
     * Parses the given {@code String} of arguments in the context of the SortCommand and returns an
     * SortCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public SortCommand parse(String args) throws ParseException {
        StringBuilder trimmedArgs = new StringBuilder(args.trim());
        if (trimmedArgs.toString().equals("")) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        int bracketDepth = 0;
        for (int i = 0; i < trimmedArgs.length(); i++) {
            if (trimmedArgs.charAt(i) == '{') {
                bracketDepth++;
            } else if (trimmedArgs.charAt(i) == '}') {
                bracketDepth--;
            } else if (trimmedArgs.charAt(i) == ' ' && bracketDepth > 0) {
                trimmedArgs.setCharAt(i, '~');
            }
        }
        String[] splittedArgs = trimmedArgs.toString().split("\\s+");

        // pattern that matches things like:
        // due< name>
        // n<
        // name> p<
        // t<{cs2103t programming} name>

        Comparator<Task> comparator = Comparator.comparing(Task::getClass, (a, b) -> {
            return 0; // a default comparator which compares every task equal
        });

        for (String element: splittedArgs) {

            if (element.isEmpty()) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
            }

            final String[] splittedComparator = element.split("(?<=[<>])");
            final String taskField = splittedComparator[0].substring(0, splittedComparator[0].length() - 1);
            final char comparisonCharacter = splittedComparator[0].charAt(splittedComparator[0].length() - 1);

            if (comparisonCharacter != '>' && comparisonCharacter != '<') {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_KEY_FORMAT, element));
            }

            if (splittedComparator.length > 1 && (!taskField.equals("t") && !taskField.equals("tag"))) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_KEY_FORMAT, element));
            }

            switch (taskField) {
            case "n": // fallthrough
            case "name": {
                if (comparisonCharacter == '<') {
                    comparator = comparator.thenComparing(Task::getName);
                } else {
                    comparator = comparator.thenComparing(Task::getName, Comparator.reverseOrder());
                }
                break;
            }
            case "d": // fallthrough
            case "due": {
                if (comparisonCharacter == '<') {
                    comparator = comparator.thenComparing(Task::getDeadline);
                } else {
                    comparator = comparator.thenComparing(Task::getDeadline, Comparator.reverseOrder());
                }
                break;
            }
            case "p": // fallthrough
            case "priority": {
                if (comparisonCharacter == '<') {
                    comparator = comparator.thenComparing(Task::getPriority);
                } else {
                    comparator = comparator.thenComparing(Task::getPriority, Comparator.reverseOrder());
                }
                break;
            }
            case "t": // fallthrough
            case "tag": {
                String tags = splittedComparator[1];
                if (tags.isEmpty() || tags.length() < 2) {
                    throw new ParseException(
                            String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
                }

                tags = tags.substring(1, tags.length() - 1); // removing '{' and '}'

                String[] tagsOrder = tags.split("~+");
                if (comparisonCharacter == '>') {
                    tagsOrder = reverseString(tagsOrder);
                }

                Tag[] tagsArray = new Tag[tagsOrder.length];
                for (int i = 0; i < tagsOrder.length; i++) {
                    try {
                        tagsArray[i] = new Tag(tagsOrder[i]);
                    } catch (IllegalArgumentException e) {
                        throw new ParseException(
                                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
                    }
                }

                comparator = comparator.thenComparing(Task::getTags, createTagsComparator(tagsArray));
                break;
            }
            default: {
                throw new ParseException(String.format(MESSAGE_INVALID_KEY_FORMAT, element));
            }
            }
        }

        return new SortCommand(comparator);
    }

    /**
     * Reverses the given {@code String} as argument and returns the
     * reverse string
     */
    String[] reverseString(String[] x) {
        int len = x.length;
        for (int i = 0; i < len / 2; i++) {
            String temp = x[i];
            x[i] = x[len - i - 1];
            x[len - i - 1] = temp;
        }
        return x;
    }

    /**
     * Builds a comparator to compare two sets of tags given {@code Tag[]} an array of tags as argument
     * and returns the comparator
     */
    Comparator<Set<Tag>> createTagsComparator(Tag[] tagsOrder) {
        Comparator<Set<Tag>> byTags = (Set<Tag> a, Set<Tag> b) -> {
            for (int i = 0; i < tagsOrder.length; i++) {
                boolean containsA = a.contains(tagsOrder[i]);
                boolean containsB = b.contains(tagsOrder[i]);
                if (containsA && !containsB) {
                    return -1;
                } else if (!containsA && containsB) {
                    return 1;
                }
            }
            return 0;
        };
        return byTags;
    }

}
