package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.model.ModelManager.ImportConflictMode;

import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ModelManager;

/**
 * Parses input arguments and creates a new ImportCommand object
 */
public class ImportCommandParser implements Parser<ImportCommand> {

    public static final String FILENAME_CONSTRAINT = "Invalid filename! File name can only contain alphanumeric"
        + " characters, full stop and the underscore [_] characters";

    /**
     * Parses the given {@code String} of arguments in the context of the ImportCommand and returns an
     * ImportCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    public ImportCommand parse(String args) throws ParseException {
        String[] input = args.trim().split("\\s+");
        if (input.length == 0) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
        }
        String filename = input[0].trim();
        if (!validFileName(filename)) {
            throw new ParseException(FILENAME_CONSTRAINT);
        }
        if (input.length == 1) {
            return new ImportCommand(filename);
        }
        else {
            String option = input[1].trim();
            switch (option) {
            case "/all":
                return new ImportCommand(filename, ImportConflictMode.DUPLICATE);
            case "/overwrite":
                return new ImportCommand(filename, ImportConflictMode.OVERWRITE);
            case "/skip":
                return new ImportCommand(filename, ImportConflictMode.IGNORE);
            default:
                throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
            }
        }


    }

    private boolean validFileName(String filename) {
        return filename.matches("^[a-zA-Z0-9_.]+$");
    }

}
