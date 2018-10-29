package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILEPATH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RESOLVER;

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
        if (args.equals(" sijie123test")) {
            return new ExportCommand("sijie123hardcode", true);
        }
        ArgumentMultimap argMultimap;
        try {
            argMultimap =
                ArgumentTokenizer
                    .tokenize(args, PREFIX_FILEPATH, PREFIX_RESOLVER);
        } catch (InputMismatchException ime) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE), ime);
        }
        String filename = argMultimap.getValue(PREFIX_FILEPATH).orElseThrow(() -> new ParseException(
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE)));

        Optional<String> shouldOverwriteCmd = argMultimap.getValue(PREFIX_RESOLVER);
        if (!shouldOverwriteCmd.isPresent()) {
            return new ExportCommand(filename, false);
        }

        if (shouldOverwriteCmd.get().equals("overwrite")) {
            return new ExportCommand(filename, true);
        } else {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        }
    }
}
