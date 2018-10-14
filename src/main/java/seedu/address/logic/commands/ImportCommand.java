package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Imports all tasks from an external deadline manager export file.
 */
public class ImportCommand extends Command {

    public static final String COMMAND_WORD = "import";
    public static final String MESSAGE_IMPORT_ERROR = "Import failed. Error: %s";
    public static final String MESSAGE_SUCCESS = "Imported successfully from external file.";
    public static final String MESSAGE_USAGE = "import filename";
    private String fileName;
    public ImportCommand(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        model.importAddressBook(fileName);
        if (model.importExportFailed()) {
            String errorMessage = model.getLastError();
            return new CommandResult(String.format(MESSAGE_IMPORT_ERROR, errorMessage));
        } else {
            //model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
            return new CommandResult(MESSAGE_SUCCESS);
        }
    }
}
