package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FREQUENCY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.task.Task;

/**
 * Adds a task to the deadline manager.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to the deadline manager. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + "[" + PREFIX_PRIORITY + "PRIORITY] "
            + "[" + PREFIX_FREQUENCY + "FREQUENCY] "
            + PREFIX_DEADLINE + "DEADLINE "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "CS2103T Peer Feedback "
            + PREFIX_PRIORITY + "1 "
            + PREFIX_FREQUENCY + "7 "
            + PREFIX_DEADLINE + "10/11/2018 "
            + PREFIX_TAG + "cs2103t "
            + PREFIX_TAG + "project";

    public static final String MESSAGE_SUCCESS = "New task added: %1$s";

    private final Task toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Task}
     */
    public AddCommand(Task task) {
        requireNonNull(task);
        toAdd = task;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);

        model.addTask(toAdd);
        model.commitTaskCollection();
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddCommand // instanceof handles nulls
                && toAdd.equals(((AddCommand) other).toAdd));
    }
}
