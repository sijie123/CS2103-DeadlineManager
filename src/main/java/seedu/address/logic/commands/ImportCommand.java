package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyTaskCollection;
import seedu.address.model.task.Task;
import seedu.address.storage.Storage;

/**
 * Imports all tasks from an external deadline manager export file.
 */
public class ImportCommand extends Command {

    public static final String COMMAND_WORD = "import";
    public static final String MESSAGE_IMPORT_ERROR = "Import failed. Error: %s";
    public static final String MESSAGE_SUCCESS = "Imported successfully from external file.";
    public static final String MESSAGE_USAGE = "import filename";
    private Path pathName;
    public ImportCommand(String filename) {
        this.pathName = Paths.get(filename);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        try {
            ReadOnlyTaskCollection importedCollection = Storage.importTaskCollection(pathName).get();
            for (Task task: importedCollection.getTaskList()) {
                model.addPerson(task);
            }
        } catch (DataConversionException | IOException e) {
            throw new CommandException(String.format(MESSAGE_IMPORT_ERROR, e));
        }
        //model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
