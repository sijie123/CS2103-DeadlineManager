package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILENAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RESOLVER;
import static seedu.address.logic.parser.ParserUtil.parseFileName;

import java.util.Optional;

import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.DuplicateImportConflictResolver;
import seedu.address.model.IgnoreImportConflictResolver;
import seedu.address.model.ImportConflictResolver;
import seedu.address.model.OverwriteImportConflictResolver;

/**
 * Parses input arguments and creates a new ImportCommand object
 */
public class ImportCommandParser implements Parser<ImportCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ImportCommand and returns an
     * ImportCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    public ImportCommand parse(String args) throws ParseException {

        ArgumentMultimap argMultimap =
            ArgumentTokenizer
                .tokenize(args, PREFIX_FILENAME, PREFIX_RESOLVER);

        String filename = argMultimap.getValue(PREFIX_FILENAME).orElseThrow(() -> new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE)));
        String checkedFilename = parseFileName(filename);

        ImportConflictResolver resolver;
        Optional<String> resolverInput = argMultimap.getValue(PREFIX_RESOLVER);
        if (resolverInput.isPresent()) {
            resolver = resolverParser(resolverInput.get());
        } else {
            resolver = new IgnoreImportConflictResolver();
        }

        return new ImportCommand(checkedFilename, resolver);
    }

    /**
     * Determines which resolver to use based on the user input.
     * @param input user command arguments
     * @return the appropriate ImportConflictResolver
     * @throws ParseException if user's argument is not recognised
     */
    private ImportConflictResolver resolverParser(String input) throws ParseException {
        switch (input) {
        case "all":
            return new DuplicateImportConflictResolver();
        case "overwrite":
            return new OverwriteImportConflictResolver();
        case "skip":
            return new IgnoreImportConflictResolver();
        default:
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
        }
    }
}
