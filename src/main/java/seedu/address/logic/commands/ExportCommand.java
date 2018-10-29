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
    public static final String MESSAGE_USAGE = "export [csv] p/FILEPATH [r/overwrite]";
    private String pathName;
    private String exportError = "";
    private boolean shouldOverwrite;
    private boolean isCsvFormat;

    public ExportCommand(String filename, boolean shouldOverwrite, boolean isCsvFormat) {
        this.pathName = filename;
        this.shouldOverwrite = shouldOverwrite;
        this.isCsvFormat = isCsvFormat;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        EventsCenter.getInstance().registerHandler(this);
        model.exportTaskCollection(pathName, shouldOverwrite, isCsvFormat);
        EventsCenter.getInstance().unregisterHandler(this);
        if (hasExportError()) {
            throw new CommandException(String.format(MESSAGE_EXPORT_ERROR, exportError));
        } else {
            return new CommandResult(String.format(MESSAGE_SUCCESS, pathName));
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof ExportCommand // instanceof handles nulls
            && pathName.equals(((ExportCommand) other).pathName)
            && shouldOverwrite == ((ExportCommand) other).shouldOverwrite
            && isCsvFormat == ((ExportCommand) other).isCsvFormat); // state check
    }

    private boolean hasExportError() {
        return !(exportError.equals(""));
    }

    @Subscribe
    public void handleImportExportExceptionEvent(ImportExportExceptionEvent event) {
        exportError = event.toString();
    }
}
