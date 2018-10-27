package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILENAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RESOLVER;
import static seedu.address.logic.parser.ParserUtil.parseFileName;

import java.util.InputMismatchException;
import java.util.Optional;

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
        ArgumentMultimap argMultimap;
        try {
            argMultimap =
                ArgumentTokenizer
                    .tokenize(args, PREFIX_FILENAME, PREFIX_RESOLVER);
        } catch (InputMismatchException ime) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE), ime);
        }
        String filename = argMultimap.getValue(PREFIX_FILENAME).orElseThrow(() -> new ParseException(
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE)));
        String checkedFilename = parseFileName(filename);

        Optional<String> shouldOverwriteCmd = argMultimap.getValue(PREFIX_RESOLVER);
        if (!shouldOverwriteCmd.isPresent()) {
            return new ExportCommand(checkedFilename, false);
        }

        if (shouldOverwriteCmd.get().equals("overwrite")) {
            return new ExportCommand(checkedFilename, true);
        } else {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        }
    }
}
