package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.function.Predicate;

import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.task.Task;

/**
 * Finds and lists all tasks in deadline manager that satisfy the given filter expression.
 */
public class FilterCommand extends Command {

    public static final String COMMAND_WORD = "filter";

    private static final String DISPLAY_DATE_NOW =
            new SimpleDateFormat("d/M/yyyy", new Locale("en", "SG")).format(new Date());

    public static final String MESSAGE_USAGE =
            COMMAND_WORD + ": Display only those tasks which satisfies the given filter expression "
                + "and displays them as a list with index numbers.\n"
                + "Parameters: FILTER_EXPRESSION   (see user guide for more information)\n"
                + "Example 1: " + COMMAND_WORD + " homework\n"
                + "Example 2: " + COMMAND_WORD + " assignment CS2103\n"
                + "Example 3: " + COMMAND_WORD + " d:" + DISPLAY_DATE_NOW + "\n"
                + "Example 3: " + COMMAND_WORD + " assignment d=" + DISPLAY_DATE_NOW + "\n"
                + "Example 4: " + COMMAND_WORD + " n:\"practical exam\" & t:CS2103,CS2106\n"
                + "Example 5: " + COMMAND_WORD + " n:\"practical exam\" & (t:CS2103,CS2106 | ! a:lecture.pdf)\n"
                + "Example 6: " + COMMAND_WORD + " n:\"practical exam\"(t:CS2103,CS2106|!a:lecture.pdf)\n"
                + "Example 7: " + COMMAND_WORD + " t=:CS";

    private final Predicate<Task> predicate;

    public FilterCommand(Predicate<Task> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.updateFilteredTaskList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW,
                    model.getFilteredTaskList().size()));
    }

    // FilterCommand should only compare equal by identity -
    // you can't really check two non-identical predicates for equality.
}
