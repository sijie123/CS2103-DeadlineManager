package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ImportCommand object
 */
public class ExportCommandParser implements Parser<ExportCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ImportCommand and returns an
     * ImportCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    public ExportCommand parse(String args) throws ParseException {
        String filename = args.trim();
        if (filename.isEmpty()) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        }

        return new ExportCommand(filename);
    }

}
