package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.ImportCommand;
import seedu.address.model.DuplicateImportConflictResolver;
import seedu.address.model.IgnoreImportConflictResolver;
import seedu.address.model.OverwriteImportConflictResolver;

/**
 * Test scope: Import command parser.
 * Tests for valid and invalid filenames, as well as valid and invalid command arguments
 */
public class ImportCommandParserTest {

    private ImportCommandParser parser = new ImportCommandParser();

    @Test
    public void parse_noFile_throwsParseException() {
        assertParseFailure(parser, "",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidCommand_throwsParseException() {
        assertParseFailure(parser, " ab",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " r/all",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " n",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " n/all r/",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " a*b",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validFileName_returnsImportCommand() {
        assertParseSuccess(parser, " n/ab", new ImportCommand("ab"));
        assertParseSuccess(parser, " n/a_b", new ImportCommand("a_b"));
        assertParseSuccess(parser, " n/veryverylongname", new ImportCommand("veryverylongname"));
        assertParseSuccess(parser, " n/fullstop.txt", new ImportCommand("fullstop.txt"));
        assertParseSuccess(parser, " n/filename_xml.txt", new ImportCommand("filename_xml.txt"));
        assertParseSuccess(parser, " n/.", new ImportCommand("."));
        assertParseSuccess(parser, " n/../folder/file", new ImportCommand("../folder/file"));
        assertParseSuccess(parser, " n/华文", new ImportCommand("华文"));
        //Filename is "
        // ab c/what"
        assertParseSuccess(parser, " n/ab c/what", new ImportCommand("ab c/what"));
    }

    @Test
    public void parse_invalidParameters_throwsParseException() {
        assertParseFailure(parser, " n/file r/override",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " n/file r/invalid",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " n/wrongCommand r//all",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validFileNameParameters_returnsImportCommand() {
        assertParseSuccess(parser, " n/   r/all",
            new ImportCommand("", new DuplicateImportConflictResolver()));
        assertParseSuccess(parser, " n/ab r/all",
            new ImportCommand("ab", new DuplicateImportConflictResolver()));
        assertParseSuccess(parser, " n/a_b",
            new ImportCommand("a_b", new IgnoreImportConflictResolver()));
        assertParseSuccess(parser, " n/veryverylongname r/overwrite",
            new ImportCommand("veryverylongname", new OverwriteImportConflictResolver()));
        assertParseSuccess(parser, " n/fullstop.txt r/all",
            new ImportCommand("fullstop.txt", new DuplicateImportConflictResolver()));
        assertParseSuccess(parser, " n/filename_xml.txt r/skip",
            new ImportCommand("filename_xml.txt", new IgnoreImportConflictResolver()));
    }
}
