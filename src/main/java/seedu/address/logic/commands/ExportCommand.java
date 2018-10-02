package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.TaskCollection;
import seedu.address.model.task.Task;
import seedu.address.storage.Storage;

/**
 * Imports all tasks from an external deadline manager export file.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";
    public static final String MESSAGE_EXPORT_ERROR = "Export failed. Error: %s";
    public static final String MESSAGE_SUCCESS = "Exported successfully to external file.";
    public static final String MESSAGE_USAGE = "export filename";
    private Path pathName;
    public ExportCommand(String filename) {
        this.pathName = Paths.get(filename);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredPersonList();
        TaskCollection exportCollection = new TaskCollection();
        exportCollection.setTasks(lastShownList);
        try {
            Storage.exportTaskCollection(exportCollection, pathName);
        } catch (IOException e) {
            throw new CommandException(String.format(MESSAGE_EXPORT_ERROR, e));
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
