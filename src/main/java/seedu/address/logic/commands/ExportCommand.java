package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import com.google.common.eventbus.Subscribe;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.storage.ImportExportExceptionEvent;
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
    private String exportError = "";

    public ExportCommand(String filename) {
        this.pathName = filename;
        //this.pathName = Paths.get(filename);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        EventsCenter.getInstance().registerHandler(this);
        model.exportTaskCollection(pathName);
        EventsCenter.getInstance().unregisterHandler(this);
        if (hasExportError()) {
            throw new CommandException(String.format(MESSAGE_EXPORT_ERROR, exportError));
        } else {
            return new CommandResult(String.format(MESSAGE_SUCCESS, pathName));
        }
    }

    private boolean hasExportError() {
        return !(exportError.equals(""));
    }

    @Subscribe
    public void handleImportExportExceptionEvent(ImportExportExceptionEvent event) {
        exportError = event.toString();
    }
}
