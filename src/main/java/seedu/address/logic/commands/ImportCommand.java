package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.ModelManager.ImportConflictMode;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Imports all tasks from an external deadline manager export file.
 */
public class ImportCommand extends Command {

    public static final String COMMAND_WORD = "import";
    public static final String MESSAGE_IMPORT_ERROR = "Import failed. Error: %s";
    public static final String MESSAGE_SUCCESS = "Imported successfully from file: %s";
    public static final String MESSAGE_USAGE = "import filename";
    private String fileName;
    private ImportConflictMode conflictResolver;

    public ImportCommand(String fileName) {
        this(fileName, ImportConflictMode.IGNORE);
    }
    public ImportCommand(String fileName, ImportConflictMode conflictResolver) {
        this.fileName = fileName;
        this.conflictResolver = conflictResolver;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        model.importTaskCollection(fileName, conflictResolver);
        if (model.importExportFailed()) {
            String errorMessage = model.getLastError();
            throw new CommandException(String.format(MESSAGE_IMPORT_ERROR, errorMessage));
        } else {
            //model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
            return new CommandResult(String.format(MESSAGE_SUCCESS, fileName));
        }
    }
}
