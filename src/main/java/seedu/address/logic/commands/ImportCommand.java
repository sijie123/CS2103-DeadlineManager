package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.ModelManager.ImportConflictMode;

import com.google.common.eventbus.Subscribe;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.storage.ImportExportExceptionEvent;
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
    private String importError = "";

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
        EventsCenter.getInstance().registerHandler(this);
        model.importTaskCollection(fileName, conflictResolver);
        EventsCenter.getInstance().unregisterHandler(this);
        if (hasImportError()) {
            throw new CommandException(String.format(MESSAGE_IMPORT_ERROR, importError));
        } else {
            //model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
            return new CommandResult(String.format(MESSAGE_SUCCESS, fileName));
        }
    }

    private boolean hasImportError() {
        return !(importError.equals(""));
    }

    @Subscribe
    public void handleImportExportExceptionEvent(ImportExportExceptionEvent event) {
        importError = event.toString();
    }

}
