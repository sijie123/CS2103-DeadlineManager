package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.task.Task;

/**
 * Finds and lists all persons in deadline manager whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FilterCommand extends Command {

    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE =
            COMMAND_WORD + ": Display only those tasks which satisfies the given filter predicate "
                    + "and displays them as a list with index numbers.\n"
                    + "Parameters: FILTER_PREDICATE [FILTER_PREDICATES]...\n"
                    + "Example: " + COMMAND_WORD + " due<1/10/2018";

    private final Predicate<Task> predicate;

    public FilterCommand(Predicate<Task> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW,
                        model.getFilteredPersonList().size()));
    }

    // FilterCommand should only compare equal by identity -
    // you can't really check two non-identical predicates for equality.
}
