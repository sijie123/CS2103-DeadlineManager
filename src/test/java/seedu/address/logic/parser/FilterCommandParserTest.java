package seedu.address.logic.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.Test;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class FilterCommandParserTest {

    private FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_doesNotThrow() {
        assertParseSuccess(parser, "d<1/10/2018");
        assertParseSuccess(parser, "due<1/10/2018");
        assertParseSuccess(parser, "d=1/10/2018");
        assertParseSuccess(parser, "d>1/10/2018");
        assertParseSuccess(parser, "d<01/02/19");
        assertParseSuccess(parser, "d<\"01/02/19\"");
        assertParseSuccess(parser, "d < \"01/02/19\"");
        assertParseSuccess(parser, "d < 01/02/19");
        assertParseSuccess(parser, "d <01/02/19");
        assertParseSuccess(parser, "d< 01/02/19");
        assertParseSuccess(parser, "d<01/02/19");
        assertParseSuccess(parser, "d:01/02/19");

        assertParseSuccess(parser, "n>Hello");
        assertParseSuccess(parser, "n>\"Hello\"");
        assertParseSuccess(parser, "n>\"Hello World\"");
        assertParseSuccess(parser, "n=\"Hello World\"");
        assertParseSuccess(parser, "n:\"Hello World\"");
        assertParseSuccess(parser, "n<\"Hello World\"");
        assertParseSuccess(parser, "n:Test");
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseThrowsException(parser, "d<\"1/10/2018");
        assertParseThrowsException(parser, "d<1/10/2018\"");
        assertParseThrowsException(parser, "d<\'1/10/2018\"");
        assertParseThrowsException(parser, "d<\'30/9/17");
        assertParseThrowsException(parser, "d>\'30/9/17");
        assertParseThrowsException(parser, "d<\'\'30/9/17");
        assertParseThrowsException(parser, "d<30/9/17\'\'");
        assertParseThrowsException(parser, "d<");
        assertParseThrowsException(parser, "d=");
        assertParseThrowsException(parser, "d>");
        assertParseThrowsException(parser, "d");
        assertParseThrowsException(parser, "=");
        assertParseThrowsException(parser, ":");
        assertParseThrowsException(parser, "-");
        assertParseThrowsException(parser, "test=test");
        assertParseThrowsException(parser, "=test");
        assertParseThrowsException(parser, "name>");
        assertParseThrowsException(parser, "name<");
        assertParseThrowsException(parser, "name~");
        assertParseThrowsException(parser, "name:");
    }

    /**
     * Asserts that the parse was successful.
     */
    private void assertParseSuccess(FilterCommandParser parser, String str) {
        try {
            FilterCommand command = parser.parse(str);
            assertNotNull("Expected not null", command);
        } catch (ParseException e) {
            fail("Expected no parse error");
        }
    }

    /**
     * Asserts that the parse throws a ParseException.
     */
    private void assertParseThrowsException(FilterCommandParser parser, String str) {
        try {
            parser.parse(str);
            fail("Expected a parse error");
        } catch (ParseException ignore) {
            // Do not do anything, because we just want to make sure that an exception is thrown
        }
    }

}
