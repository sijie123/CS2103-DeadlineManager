package seedu.address.logic.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.Test;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.RichParseException;

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
        assertParseSuccess(parser, "due:01/02/19");

        assertParseSuccess(parser, "n>Hello");
        assertParseSuccess(parser, "n>\"Hello\"");
        assertParseSuccess(parser, "n>\"Hello World\"");
        assertParseSuccess(parser, "n=\"Hello World\"");
        assertParseSuccess(parser, "n:\"Hello World\"");
        assertParseSuccess(parser, "n<\"Hello World\"");
        assertParseSuccess(parser, "n:Test");
        assertParseSuccess(parser, "name:Test");
        assertParseSuccess(parser, "n:\\");
        assertParseSuccess(parser, "n:-");
        assertParseSuccess(parser, "n:_");
        assertParseSuccess(parser, "n:/");
        assertParseSuccess(parser, "n:.");
        assertParseSuccess(parser, "n:,");

        assertParseSuccess(parser, "p>1");
        assertParseSuccess(parser, "p:1");
        assertParseSuccess(parser, "p<1");
        assertParseSuccess(parser, "p=1");
        assertParseSuccess(parser, "p:0");
        assertParseSuccess(parser, "p:2");
        assertParseSuccess(parser, "p:3");
        assertParseSuccess(parser, "p:4");
        assertParseSuccess(parser, "p:\"3\"");
        assertParseSuccess(parser, "priority:3");

        assertParseSuccess(parser, "f>1");
        assertParseSuccess(parser, "f:1");
        assertParseSuccess(parser, "f<1");
        assertParseSuccess(parser, "f=1");
        assertParseSuccess(parser, "f:0");
        assertParseSuccess(parser, "f:2");
        assertParseSuccess(parser, "f:3");
        assertParseSuccess(parser, "f:4");
        assertParseSuccess(parser, "f:\"3\"");
        assertParseSuccess(parser, "frequency:1");

        assertParseSuccess(parser, "t:CS2101,CS2103");
        assertParseSuccess(parser, "t:CS2101");
        assertParseSuccess(parser, "t:\"CS2101\"");
        assertParseSuccess(parser, "t:\"CS2101,CS2103\"");
        assertParseSuccess(parser, "t:\'CS2101,CS2103\'");
        assertParseSuccess(parser, "t:\'CS2101, CS2103\'");
        assertParseSuccess(parser, "t :  \'CS2101, CS2103\'");
        assertParseSuccess(parser, "t=\"CS2101,CS2103\"");
        assertParseSuccess(parser, "t<\"CS2101,CS2103\"");
        assertParseSuccess(parser, "t>\"CS2101,CS2103\"");
        assertParseSuccess(parser, "t>\"CS2101,\'CS2103\'\"");
        assertParseSuccess(parser, "t:\"CS2101,,CS2103\"");
        assertParseSuccess(parser, "tag:\"CS2101,CS2103\"");

        assertParseSuccess(parser, "a:CS2101,CS2103");
        assertParseSuccess(parser, "a:CS2101");
        assertParseSuccess(parser, "a:\"CS2101\"");
        assertParseSuccess(parser, "a:\"CS2101,CS2103\"");
        assertParseSuccess(parser, "a:\'CS2101,CS2103\'");
        assertParseSuccess(parser, "a:\'CS2101, CS2103\'");
        assertParseSuccess(parser, "a :  \'CS2101, CS2103\'");
        assertParseSuccess(parser, "a=\"CS2101,CS2103\"");
        assertParseSuccess(parser, "a<\"CS2101,CS2103\"");
        assertParseSuccess(parser, "a>\"CS2101,CS2103\"");
        assertParseSuccess(parser, "a>\"CS2101,\'CS2103\'\"");
        assertParseSuccess(parser, "a:\"CS2101,,CS2103\"");
        assertParseSuccess(parser, "a:CS2101.html");
        assertParseSuccess(parser, "attachment:\"CS2101,CS2103\"");
    }

    @Test
    public void parse_compositeFilters_doesNotThrow() {
        assertParseSuccess(parser, "n:Hello & n:World");
        assertParseSuccess(parser, "n:Hello && n:World");
        assertParseSuccess(parser, "n:Hello &&n:World");
        assertParseSuccess(parser, "n:Hello&& n:World");
        assertParseSuccess(parser, "n:Hello&n:World");
        assertParseSuccess(parser, "n:Hello&&n:World");
        assertParseSuccess(parser, "n:Hello | n:World");
        assertParseSuccess(parser, "n:Hello || n:World");
        assertParseSuccess(parser, "n:Hello ||n:World");
        assertParseSuccess(parser, "n:Hello|| n:World");
        assertParseSuccess(parser, "n:Hello|n:World");
        assertParseSuccess(parser, "n:Hello||n:World");
        assertParseSuccess(parser, "!n:Hello");
        assertParseSuccess(parser, "!n:Hello||!n:World");
        assertParseSuccess(parser, "(!n:Hello||!n:World)");
        assertParseSuccess(parser, "(n:Hello||n:World)");
        assertParseSuccess(parser, "(((n:Hello||n:World)))");
        assertParseSuccess(parser, "(( (n:Hello   || n:World)) )");
        assertParseSuccess(parser, "(n:Hello||n:World)&&n:Test");
        assertParseSuccess(parser, "(n:Hello||n:World) && n:Test");
        assertParseSuccess(parser, "(n:Hello || n:World) && n:Test");
        assertParseSuccess(parser, "(n:Hello || d:1/10/2018) && t:CS2103");
        assertParseSuccess(parser, "(n>Hello || d:1/10/2018) && t:CS2103");
        assertParseSuccess(parser, "(n<Hello || d:1/10/2018) && t:CS2103");
        assertParseSuccess(parser, "(n=Hello || d:1/10/2018) && t:CS2103");
        assertParseSuccess(parser, "n=Hello || d:1/10/2018 && t:CS2103");
        assertParseSuccess(parser, "(n:Hello || d:1/10/2018) && t:CS2103,CS2101");
        assertParseSuccess(parser, "(n:Hello || d:1/10/2018) && t:\"CS2103,CS2101\"");
        assertParseSuccess(parser, "(n:Hello || d:1/10/2018) && t:\'CS2103,CS2101\'");
        assertParseSuccess(parser, "(n:Hello || d:1/10/2018) && t:\"CS2103, CS2101\"");
        assertParseSuccess(parser, "(n:Hello || d:1/10/2018) && t:\'CS2103   ,  CS2101\'");
        assertParseSuccess(parser, "(n:Hello || d:1/10/2018) && t:\'CS2103   ,  CS2101\'"
            + " && (!n:World || ((!t:CS1231 || t:CS3230) && d:1/11/2018))");
        assertParseSuccess(parser, "(n:Hello||d:1/10/2018)&&t:CS2103,CS2101"
            + "&&(!n:World||((!t:CS1231||t:CS3230)&&d:1/11/2018))");
    }

    @Test
    public void parse_implicitOperator_doesNotThrow() {
        assertParseSuccess(parser, "n:Hello n:World");
        assertParseSuccess(parser, "n:Hello  n:World");
        assertParseSuccess(parser, "n:Hello      n:World");
        assertParseSuccess(parser, "n:Hello !n:World");
        assertParseSuccess(parser, "n:Hello!n:World");
        assertParseSuccess(parser, "(( (n:Hello    n:World)) )");
        assertParseSuccess(parser, "(( (n:Hello   ! n:World)) )");
        assertParseSuccess(parser, "(( (n:Hello    !n:World)) )");
        assertParseSuccess(parser, "(( (n:Hello!    n:World)) )");
        assertParseSuccess(parser, "(( (n:Hello!n:World)) )");
        assertParseSuccess(parser, "(n:Hello || d:1/10/2018) t:CS2103");
        assertParseSuccess(parser, "(n:Hello || d:1/10/2018)t:CS2103");
        assertParseSuccess(parser, "(n:Hello || d:1/10/2018)!t:CS2103");
        assertParseSuccess(parser, "t:CS2103(n:Hello || d:1/10/2018)");
        assertParseSuccess(parser, "t:CS2103 (n:Hello || d:1/10/2018)");
        assertParseSuccess(parser, "t:CS2103 !(n:Hello || d:1/10/2018)");
        assertParseSuccess(parser, "(n:Hello d:1/10/2018)t:CS2103");
        assertParseSuccess(parser, "t:CS2101((((n:Hello d:1/10/2018))))t:CS2103");
        assertParseSuccess(parser, "t:CS2101(n:Hello d:1/10/2018)t:CS2103");
        assertParseSuccess(parser, "t:CS2101 n:Hello d:1/10/2018 t:CS2103");
    }

    @Test
    public void parse_anyField_doesNotThrow() {
        assertParseSuccess(parser, "Testp");
        assertParseSuccess(parser, "1/10/2018");
        assertParseSuccess(parser, "1/10/2018 Testp");
        assertParseSuccess(parser, "1/10/2018 Testp htrhrth");
        assertParseSuccess(parser, "1/10/2018 \"Testp htrhrth\"");
        assertParseSuccess(parser, "Testp htrhrth 1/10/2018");
        assertParseSuccess(parser, "Testp 1/10/2018");
        assertParseSuccess(parser, "Testp   1/10/2018");
        assertParseSuccess(parser, "Testp && 1/10/2018");
        assertParseSuccess(parser, "Testp&&1/10/2018");
        assertParseSuccess(parser, "Testp!1/10/2018");
        assertParseSuccess(parser, "Testp d:1/10/2018");
        assertParseSuccess(parser, "n:Testp 1/10/2018");
        assertParseSuccess(parser, "Testp|d:1/10/2018");
        assertParseSuccess(parser, "Testp | d:1/10/2018");
        assertParseSuccess(parser, "Testp | d : 1/10/2018");
        assertParseSuccess(parser, "Testp || 1/10/2018");
        assertParseSuccess(parser, "n:Testp | 1/10/2018");
        assertParseSuccess(parser, "n:Testp|1/10/2018");
        assertParseSuccess(parser, "Testp|(!n:Hello||!n:World)");
        assertParseSuccess(parser, "Testp(!n:Hello||!n:World)");
        assertParseSuccess(parser, "(!n:Hello||!n:World)|Testp");
        assertParseSuccess(parser, "(!n:Hello||!n:World)Testp");
    }

    @Test
    public void parse_setFilter_doesNotThrow() {
        assertParseSuccess(parser, "t::x");
        assertParseSuccess(parser, "t:=x");
        assertParseSuccess(parser, "t:<x");
        assertParseSuccess(parser, "t:>x");
        assertParseSuccess(parser, "t<:x");
        assertParseSuccess(parser, "t>:x");
        assertParseSuccess(parser, "t=:xeg4");
        assertParseSuccess(parser, "a::x");
        assertParseSuccess(parser, "a:=x");
        assertParseSuccess(parser, "a:<x");
        assertParseSuccess(parser, "a:>x");
        assertParseSuccess(parser, "a<:x");
        assertParseSuccess(parser, "a>:x");
        assertParseSuccess(parser, "a=:xeg4");
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
        assertParseThrowsException(parser, "p:5");
        assertParseThrowsException(parser, "p:b");
        assertParseThrowsException(parser, "f:b");
        assertParseThrowsException(parser, "f:teh");
        assertParseThrowsException(parser, "n::");
        assertParseThrowsException(parser, "n:~");
        assertParseThrowsException(parser, "n:&");
        assertParseThrowsException(parser, "n:|");
        assertParseThrowsException(parser, "n:!");
        assertParseThrowsException(parser, "n:@");
        assertParseThrowsException(parser, "n:#");
        assertParseThrowsException(parser, "n:$");
        assertParseThrowsException(parser, "n:abc$");
        assertParseThrowsException(parser, "n:$abc$");
        assertParseThrowsException(parser, "n:$abc");
        assertParseThrowsException(parser, "=");
        assertParseThrowsException(parser, ":");
        assertParseThrowsException(parser, "test=test");
        assertParseThrowsException(parser, "=test");
        assertParseThrowsException(parser, "name>");
        assertParseThrowsException(parser, "name<");
        assertParseThrowsException(parser, "name:");
        assertParseThrowsException(parser, "1:");
        assertParseThrowsException(parser, "2:");
        assertParseThrowsException(parser, "-:");
        assertParseThrowsException(parser, "/:");
        assertParseThrowsException(parser, "wert/:");
        assertParseThrowsException(parser, "t:\"CS2101,CS2103");
        assertParseThrowsException(parser, "(t:\"CS2101,CS2103");
        assertParseThrowsException(parser, "t:CS2101,CS2103\"");
        assertParseThrowsException(parser, "(t:CS2101,CS2103\"");
        assertParseThrowsException(parser, "t:CS2101,CS2103 && ");
        assertParseThrowsException(parser, "t:CS2101,CS2103 && )");
        assertParseThrowsException(parser, "t:CS2101,CS2103 && n:Hello)");
        assertParseThrowsException(parser, "t:\"1/2/3\"");
        assertParseThrowsException(parser, "(");
        assertParseThrowsException(parser, ")");
        assertParseThrowsException(parser, "&& n:Hello");
        assertParseThrowsException(parser, "|| n:Hello");
        assertParseThrowsException(parser, "t:CS2101 && (n:Hello");
        assertParseThrowsException(parser, "(!n:Hello| |!n:World)");
        assertParseThrowsException(parser, "(!n:Hello|| ||!n:World)");
        assertParseThrowsException(parser, "(!n:Hello||||!n:World)");
        assertParseThrowsException(parser, "(!n:Hello|||!n:World)");
        assertParseThrowsException(parser, "n:Hello!");
        assertParseThrowsException(parser, "n::test");
        assertParseThrowsException(parser, "n=:test");
        assertParseThrowsException(parser, "n:=test");
        assertParseThrowsException(parser, "q::test");
        assertParseThrowsException(parser, "t::1/2/6");
        assertParseThrowsException(parser, "t:::test");
        assertParseThrowsException(parser, "a:::test");
    }

    /**
     * Asserts that the parse was successful.
     */
    private void assertParseSuccess(FilterCommandParser parser, String str) {
        try {
            FilterCommand command = parser.parse(str);
            assertNotNull("Expected not null", command);
        } catch (RichParseException e) {
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
        } catch (RichParseException ignore) {
            // Do not do anything, because we just want to make sure that an exception is thrown
        }
    }

}
