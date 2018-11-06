package seedu.address.logic.parser.tokenizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

import seedu.address.logic.parser.tokenizer.StringTokenizer;
import seedu.address.logic.parser.tokenizer.exceptions.TokenizationEndOfStringException;
import seedu.address.logic.parser.tokenizer.exceptions.TokenizationException;
import seedu.address.logic.parser.tokenizer.exceptions.TokenizationNoMatchableCharacterException;

public class StringTokenizerTest {

    @Test
    public void tokenize_nextStringSimple_success() throws TokenizationException {
        StringTokenizer tokenizer = new StringTokenizer("Hello world!   \t\t \"Test Test2\"");
        assertTrue(tokenizer.hasNextToken());
        assertEquals(tokenizer.nextString(), "Hello");
        assertTrue(tokenizer.hasNextToken());
        assertEquals(tokenizer.nextString(), "world!");
        assertTrue(tokenizer.hasNextToken());
        assertEquals(tokenizer.nextString(), "Test Test2");
        assertFalse(tokenizer.hasNextToken());
    }

    @Test
    public void tokenize_nextStringComplex_success() throws TokenizationException {
        StringTokenizer tokenizer = new StringTokenizer("Hello world!    \"Test Test2\"");
        assertTrue(tokenizer.hasNextToken());
        assertEquals(tokenizer.nextString(), "Hello");
        assertTrue(tokenizer.hasNextToken());
        assertThrows(TokenizationNoMatchableCharacterException.class, () ->
                tokenizer.nextPattern(Pattern.compile("!")));
        assertEquals(tokenizer.nextString(x -> x >= 'a' && x <= 'z'), "world");
        assertTrue(tokenizer.hasNextToken());
        assertEquals(tokenizer.nextPattern(Pattern.compile("\\!")), "!");
        assertTrue(tokenizer.hasNextToken());
        assertEquals(tokenizer.nextString(), "Test Test2");
        assertFalse(tokenizer.hasNextToken());
    }

    @Test
    public void tokenize_nextPattern_successAndException() throws TokenizationException {
        StringTokenizer tokenizer = new StringTokenizer("Hello world!    \"Test Test2\"");
        assertTrue(tokenizer.hasNextToken());
        assertNull(tokenizer.tryNextPattern(Pattern.compile("s")));
        assertTrue(tokenizer.hasNextToken());
        assertTrue(tokenizer.hasNextToken());
        assertNull(tokenizer.tryNextPattern(Pattern.compile("[abc]")));
        assertEquals(tokenizer.tryNextPattern(Pattern.compile("[A-Za-z]+")), "Hello");
        assertThrows(TokenizationNoMatchableCharacterException.class, () ->
                tokenizer.nextMatcher(Pattern.compile("[a-z&&[^w]]+")));
        assertEquals(tokenizer.tryNextMatcher(Pattern.compile("[A-Za-z]*")).group(), "world");
        assertThrows(TokenizationNoMatchableCharacterException.class, () ->
                tokenizer.nextPattern(Pattern.compile("h")));
        assertEquals(tokenizer.nextPattern(Pattern.compile("!")), "!");
        assertEquals(tokenizer.nextString(), "Test Test2");
        assertFalse(tokenizer.hasNextToken());
        assertThrows(TokenizationEndOfStringException.class, () ->
                tokenizer.nextMatcher(Pattern.compile("[a-z&&[^w]]+")));
        assertThrows(TokenizationEndOfStringException.class, () ->
                tokenizer.nextPattern(Pattern.compile("[a-z&&[^w]]+")));
    }

    @Test
    public void tokenize_toList_success() throws TokenizationException {
        StringTokenizer tokenizer = new StringTokenizer("Hello world!    \"Test Test2\"");
        assertEquals(tokenizer.toList(), List.of("Hello", "world!", "Test Test2"));
        tokenizer = new StringTokenizer("Hello  test world! 12345h    \'Test test2\'");
        assertEquals(tokenizer.toList(), List.of("Hello", "test", "world!", "12345h", "Test test2"));
    }

}
