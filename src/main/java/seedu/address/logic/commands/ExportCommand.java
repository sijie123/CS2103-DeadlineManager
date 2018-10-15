package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Imports all tasks from an external deadline manager export file.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";
    public static final String MESSAGE_EXPORT_ERROR = "Export failed. Error: %s";
    public static final String MESSAGE_SUCCESS = "Exported successfully to external file: %s";
    public static final String MESSAGE_USAGE = "export filename";
    private String pathName;
    public ExportCommand(String filename) {
        this.pathName = filename;
        //this.pathName = Paths.get(filename);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        model.exportAddressBook(pathName);
        if (model.importExportFailed()) {
            String errorMessage = model.getLastError();
            throw new CommandException(String.format(MESSAGE_EXPORT_ERROR, errorMessage));
        } else {
            return new CommandResult(String.format(MESSAGE_SUCCESS, pathName));
        }
    }
}
