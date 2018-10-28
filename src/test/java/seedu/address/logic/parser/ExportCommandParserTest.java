package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.ExportCommand;

/**
 * Test scope: Export command parser.
 * Tests for valid and invalid filenames.
 */
public class ExportCommandParserTest {

    private ExportCommandParser parser = new ExportCommandParser();

    @Test
    public void parse_noFile_throwsParseException() {
        assertParseFailure(parser, "",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidCommand_throwsParseException() {
        assertParseFailure(parser, " ab",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " r/all",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " n",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " n/all r/",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " a*b",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
    }


    @Test
    public void parse_validFileName_returnsExportCommand() {
        assertParseSuccess(parser, " n/ab", new ExportCommand("ab", false));
        assertParseSuccess(parser, " n/a_b", new ExportCommand("a_b", false));
        assertParseSuccess(parser, " n/veryverylongname", new ExportCommand("veryverylongname", false));
        assertParseSuccess(parser, " n/fullstop.txt", new ExportCommand("fullstop.txt", false));
        assertParseSuccess(parser, " n/filename_xml.txt", new ExportCommand("filename_xml.txt", false));
        assertParseSuccess(parser, " n/.", new ExportCommand(".", false));
        assertParseSuccess(parser, " n/./folder/file", new ExportCommand("./folder/file", false));
        assertParseSuccess(parser, " n/../folder/file", new ExportCommand("../folder/file", false));
        assertParseSuccess(parser, " n/华文", new ExportCommand("华文", false));
        //Filename is "ab c/what"
        assertParseSuccess(parser, " n/ab c/what", new ExportCommand("ab c/what", false));
        //Filename is ""
        assertParseSuccess(parser, " n/    ", new ExportCommand("", false));
    }

    @Test
    public void parse_invalidParameters_throwsParseException() {
        assertParseFailure(parser, " n/file r/override",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " n/file r/invalid",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " n/wrongCommand r//all",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validFileNameParameters_returnsExportCommand() {
        assertParseSuccess(parser, " n/ab r/overwrite",
            new ExportCommand("ab", true));
        assertParseSuccess(parser, " n/a_b",
            new ExportCommand("a_b", false));
        assertParseSuccess(parser, " n/filename_xml.txt r/overwrite",
            new ExportCommand("filename_xml.txt", true));
    }
}
