package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.AttachmentCommand.COMMAND_ADD_ACTION;
import static seedu.address.logic.commands.AttachmentCommand.COMMAND_DELETE_ACTION;
import static seedu.address.logic.commands.AttachmentCommand.COMMAND_GET_ACTION;
import static seedu.address.logic.commands.AttachmentCommand.COMMAND_LIST_ACTION;
import static seedu.address.logic.commands.AttachmentCommand.MESSAGE_MISSING_ARGUMENTS;
import static seedu.address.logic.commands.AttachmentCommand.MESSAGE_USAGE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_TASK;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AttachmentCommand;

/**
 * Test scope: Export command parser.
 * Tests for valid and invalid filenames.
 */
public class AttachmentCommandParserTest {
    private static final String ATTACHMENT_ADD_FORMAT = " %1$d " + COMMAND_ADD_ACTION + " p/%2$s";
    private static final String ATTACHMENT_LIST_FORMAT = " %1$d " + COMMAND_LIST_ACTION;
    private static final String ATTACHMENT_DELETE_FORMAT = " %1$d " + COMMAND_DELETE_ACTION + " n/%2$s";
    private static final String ATTACHMENT_GET_WITH_PATH_NAME_FORMAT = " %1$d " + COMMAND_GET_ACTION + " p/%2$s n/%3$s";
    private static final String ATTACHMENT_GET_WITH_NAME_PATH_FORMAT = " %1$d " + COMMAND_GET_ACTION + " n/%3$s p/%2$s";

    private AttachmentCommandParser parser = new AttachmentCommandParser();

    @Test
    public void parse_emptyString_throwsParseException() {
        String expected = String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE);
        assertParseFailure(parser, "", expected);
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        String expected = String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE);
        assertParseFailure(parser, "miao add", expected);
        assertParseFailure(parser, "p/123 delete", expected);
        assertParseFailure(parser, "0 list", expected);
        assertParseFailure(parser, "-1 get", expected);
    }

    @Test
    public void parse_invalidAction_throwsParseException() {
        String expected = String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE);
        assertParseFailure(parser, "1", expected);
        assertParseFailure(parser, "1 hello", expected);
        assertParseFailure(parser, "1 LIST", expected);
        assertParseFailure(parser, "1 2", expected);
        assertParseFailure(parser, "1 $/#", expected);
    }

    @Test
    public void parse_addActionUnquotedFilePath_returnsAttachmentCommand() {
        Index targetIndex = INDEX_SECOND_TASK;
        String filePath = "helloworld.docx";
        AttachmentCommand expected = new AttachmentCommand(targetIndex,
            new AttachmentCommand.AddAttachmentAction(filePath));
        String command = String.format(ATTACHMENT_ADD_FORMAT, targetIndex.getOneBased(), filePath);
        assertParseSuccess(parser, command, expected);
    }

    @Test
    public void parse_addActionQuotedFilePath_returnsAttachmentCommand() {
        Index targetIndex = INDEX_SECOND_TASK;
        String filePath = "/123 p/";
        AttachmentCommand expected = new AttachmentCommand(targetIndex,
            new AttachmentCommand.AddAttachmentAction(filePath));
        String command = String.format(ATTACHMENT_ADD_FORMAT, targetIndex.getOneBased(),
            "\"" + filePath + "\"");
        assertParseSuccess(parser, command, expected);
        command = String.format(ATTACHMENT_ADD_FORMAT, targetIndex.getOneBased(),
            "\'" + filePath + "\'");
        assertParseSuccess(parser, command, expected);
    }

    @Test
    public void parse_addActionInvalidArguments_throwsParseException() {
        String expected = String.format(MESSAGE_MISSING_ARGUMENTS, "FILEPATH", "add");
        assertParseFailure(parser, "1 add", expected);
        assertParseFailure(parser, "1 add n/HelloWorld.docx", expected);
        expected = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AttachmentCommand.MESSAGE_USAGE, "");
        assertParseFailure(parser, "1 add p/HelloWorld p/HelloWorld.docx", expected);
    }

    @Test
    public void parse_listAction_returnsAttachmentCommand() {
        Index targetIndex = INDEX_FIRST_TASK;
        AttachmentCommand expected = new AttachmentCommand(targetIndex,
            new AttachmentCommand.ListAttachmentAction());
        String command = String.format(ATTACHMENT_LIST_FORMAT, targetIndex.getOneBased());
        assertParseSuccess(parser, command, expected);
    }

    @Test
    public void parse_deleteActionUnquotedFileName_returnsAttachmentCommand() {
        Index targetIndex = INDEX_SECOND_TASK;
        String fileName = "123";
        AttachmentCommand expected = new AttachmentCommand(targetIndex,
            new AttachmentCommand.DeleteAttachmentAction(fileName));
        String command = String.format(ATTACHMENT_DELETE_FORMAT, targetIndex.getOneBased(), fileName);
        assertParseSuccess(parser, command, expected);
    }

    @Test
    public void parse_deleteActionQuotedFileName_returnsAttachmentCommand() {
        Index targetIndex = INDEX_SECOND_TASK;
        String fileName = "p  .docx";
        AttachmentCommand expected = new AttachmentCommand(targetIndex,
            new AttachmentCommand.DeleteAttachmentAction(fileName));
        String command = String.format(ATTACHMENT_DELETE_FORMAT, targetIndex.getOneBased(), "'" + fileName + "'");
        assertParseSuccess(parser, command, expected);
    }

    @Test
    public void parse_deleteActionInvalidArguments_throwsParseException() {
        String expected = String.format(MESSAGE_MISSING_ARGUMENTS, "FILENAME", "delete");
        assertParseFailure(parser, "1 delete", expected);
        assertParseFailure(parser, "1 delete p/HelloWorld.docx", expected);
        expected = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AttachmentCommand.MESSAGE_USAGE, "");
        assertParseFailure(parser, "1 delete n/HelloWorld n/HelloWorld.docx", expected);
    }

    @Test
    public void parse_getActionUnquotedFileNameFilePath_returnsAttachmentCommand() {
        Index targetIndex = INDEX_SECOND_TASK;
        String filePath = "../hello_world";
        String fileName = "hello_world...";

        AttachmentCommand expected = new AttachmentCommand(targetIndex,
            new AttachmentCommand.GetAttachmentAction(fileName, filePath));
        String command = String.format(ATTACHMENT_GET_WITH_PATH_NAME_FORMAT,
            targetIndex.getOneBased(), filePath, fileName);
        assertParseSuccess(parser, command, expected);

        command = String.format(ATTACHMENT_GET_WITH_NAME_PATH_FORMAT,
            targetIndex.getOneBased(), filePath, fileName);
        assertParseSuccess(parser, command, expected);
    }

    @Test
    public void parse_getActionQuotedFileNameFilePath_returnsAttachmentCommand() {
        Index targetIndex = INDEX_SECOND_TASK;
        String filePath = "../hello p/hello world.txt";
        String fileName = "hello  world.in";

        AttachmentCommand expected = new AttachmentCommand(targetIndex,
            new AttachmentCommand.GetAttachmentAction(fileName, filePath));
        String command = String.format(ATTACHMENT_GET_WITH_PATH_NAME_FORMAT,
            targetIndex.getOneBased(), "'" + filePath + "'", "'" + fileName + "'");
        assertParseSuccess(parser, command, expected);

        command = String.format(ATTACHMENT_GET_WITH_NAME_PATH_FORMAT,
            targetIndex.getOneBased(), "\"" + filePath + "\"", "\"" + fileName + "\"");
        assertParseSuccess(parser, command, expected);
    }

    @Test
    public void parse_getActionInvalidArguments_throwsParseException() {
        String expected = String.format(MESSAGE_MISSING_ARGUMENTS, "FILENAME", "get");
        assertParseFailure(parser, "1 get", expected);
        assertParseFailure(parser, "1 get p/HelloWorld.docx", expected);
        expected = String.format(MESSAGE_MISSING_ARGUMENTS, "FILEPATH", "get");
        assertParseFailure(parser, "1 get n/Hello Kitty.txt", expected);
        expected = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AttachmentCommand.MESSAGE_USAGE, "");
        assertParseFailure(parser, "1 get n/HelloWorld n/HelloWorld.docx p/Meow.txt", expected);
        assertParseFailure(parser, "1 get n/HelloWorld p/HelloWorld.docx p/Meow.txt", expected);
    }
}
