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
        assertParseFailure(parser, " csv n",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " notCsv n",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " p/all r/",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " a*b",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
    }


    @Test
    public void parse_validFileName_returnsExportCommand() {
        assertParseSuccess(parser, " p/ab",
            new ExportCommand("ab", false, false));
        assertParseSuccess(parser, " p/a_b",
            new ExportCommand("a_b", false, false));
        assertParseSuccess(parser, " p/fullstop.txt",
            new ExportCommand("fullstop.txt", false, false));
        assertParseSuccess(parser, " p/.",
            new ExportCommand(".", false, false));
        assertParseSuccess(parser, " p/./folder/file",
            new ExportCommand("./folder/file", false, false));
        assertParseSuccess(parser, " p/../folder/file",
            new ExportCommand("../folder/file", false, false));
        assertParseSuccess(parser, " p/华文",
            new ExportCommand("华文", false, false));
        //Filename is "ab c/what"
        assertParseSuccess(parser, " p/ab c/what",
            new ExportCommand("ab c/what", false, false));
        //Filename is ""
        assertParseSuccess(parser, " p/    ",
            new ExportCommand("", false, false));
    }

    @Test
    public void parse_validFileNameCsv_returnsExportCommand() {
        assertParseSuccess(parser, " csv p/ab",
            new ExportCommand("ab", false, true));
        assertParseSuccess(parser, " csv p/.csv",
            new ExportCommand(".csv", false, true));
        //Filename is ab c/what
        assertParseSuccess(parser, " csv p/ab c/what",
            new ExportCommand("ab c/what", false, true));
        //Filename is ""
        assertParseSuccess(parser, " csv p/    ",
            new ExportCommand("", false, true));
    }

    @Test
    public void parse_invalidParameters_throwsParseException() {
        assertParseFailure(parser, " notCSV p/file r/override",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " p/file r/override",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " p/file r/invalid",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " p/wrongCommand r//all",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        //The below should fail. If a directory is indeed file p/name, it should be in quotes.
        assertParseFailure(parser, " p/file p/name",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validFileNameParameters_returnsExportCommand() {
        assertParseSuccess(parser, " csv p/ab r/overwrite",
            new ExportCommand("ab", true, true));
        assertParseSuccess(parser, " p/a_b",
            new ExportCommand("a_b", false, false));
        assertParseSuccess(parser, " p/filename_xml.txt r/overwrite",
            new ExportCommand("filename_xml.txt", true, false));
        assertParseSuccess(parser, " p/'file p/name'",
            new ExportCommand("file p/name", false, false));
    }
}
