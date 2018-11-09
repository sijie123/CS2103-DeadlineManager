package seedu.address.logic.parser.tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.parser.tokenizer.exceptions.TokenizationEndOfStringException;
import seedu.address.logic.parser.tokenizer.exceptions.TokenizationMismatchException;
import seedu.address.logic.parser.tokenizer.exceptions.TokenizationMissingEndQuoteException;
import seedu.address.logic.parser.tokenizer.exceptions.TokenizationNoMatchableCharacterException;
import seedu.address.logic.parser.tokenizer.exceptions.TokenizationUnexpectedQuoteException;

/**
 * This class tokenizes a string.
 * The token can be any predicate,
 * and it supports quoted strings (and arbitrary levels of recursiveness) too.
 * It is better than java.util.StringTokenizer.
 * Multiple consecutive delimiters are treated as a single delimiter.
 */
public class StringTokenizer {

    private static final Predicate<Character> WHITESPACE_PREDICATE = Character::isWhitespace;
    private static final Predicate<Character> ANY_QUOTES_PREDICATE = ch -> ch == '\'' || ch == '\"';

    private final String str;
    private final Predicate<Character> delimPred;
    private final Predicate<Character> quotePred;
    private int nextIndex;

    /**
     * Constructs an instance of Stringtokenizer, using default predicates.
     *
     * @param str The strong to tokenize, it is not allowed to be null.
     */
    public StringTokenizer(String str) {
        this(str, WHITESPACE_PREDICATE, ANY_QUOTES_PREDICATE);
    }

    /**
     * Constructs an instance of Stringtokenizer.
     *
     * @param delimPred A predicate that returns true if the given character is a delimiter.
     * @param quotePred A predicate that returns true if the given character is a quote.
     */
    public StringTokenizer(String str, Predicate<Character> delimPred, Predicate<Character> quotePred) {
        this.str = str;
        this.delimPred = delimPred;
        this.quotePred = quotePred;
        this.nextIndex = 0;
    }

    /**
     * Returns true if there are still non-delimiter characters in the string.
     */
    public boolean hasNextToken() {
        // gobble up all the delims
        while (nextIndex < str.length() && delimPred.test(str.charAt(nextIndex))) {
            ++nextIndex;
        }

        return nextIndex < str.length();
    }

    /**
     * Consume the next string token.
     */
    public String nextString()
            throws TokenizationMissingEndQuoteException, TokenizationUnexpectedQuoteException,
            TokenizationNoMatchableCharacterException, TokenizationEndOfStringException {

        return nextString(ch -> true);
    }

    /**
     * Consume the next string token.
     *
     * @param validPred A predicate that returns false if the given character is not allowed in this token.
     *                  If such a character is encountered, the token would be considered to have ended
     *                  just before that character.  It is only used for unquoted strings.
     * @return The string that is extracted from the input.
     * @throws TokenizationMissingEndQuoteException if an end quote is missing.
     * @throws TokenizationUnexpectedQuoteException if an unexpected quote was encountered.
     * @throws TokenizationNoMatchableCharacterException if no characters could be matched.
     * @throws TokenizationEndOfStringException if the string has reached the end.
     */
    public String nextString(final Predicate<Character> validPred)
            throws TokenizationMissingEndQuoteException, TokenizationUnexpectedQuoteException,
            TokenizationNoMatchableCharacterException, TokenizationEndOfStringException {

        if (!hasNextToken()) {
            throw new TokenizationEndOfStringException("Reached end of string while reading delimiter!");
        }

        if (quotePred.test(str.charAt(nextIndex))) {
            // starts with quote
            return nextQuotedString();
        } else {
            // does not start with quote
            return nextUnquotedString(validPred);
        }
    }

    /**
     * Consume the next string, assuming that the next character is not a quote.
     *
     * @param validPred A predicate that returns false if the given character is not allowed in this token.
     *                  If such a character is encountered, the token would be considered to have ended
     *                  just before that character.
     * @return The string that is extracted from the input.
     * @throws TokenizationUnexpectedQuoteException if an unexpected quote was encountered.
     * @throws TokenizationNoMatchableCharacterException if no characters could be matched.
     */
    private String nextUnquotedString(final Predicate<Character> validPred)
            throws TokenizationUnexpectedQuoteException, TokenizationNoMatchableCharacterException {
        int startLocation = nextIndex;
        // there should be no quotes within this string
        for (; nextIndex < str.length(); ++nextIndex) {
            char ch = str.charAt(nextIndex);
            if (delimPred.test(ch)) {
                break;
            } else if (quotePred.test(ch)) {
                int tmpNextIndex = nextIndex;
                // rollback nextIndex
                nextIndex = startLocation;
                // throw an exception
                throw new TokenizationUnexpectedQuoteException(tmpNextIndex, tmpNextIndex + 1,
                        "Quotes encountered in the middle of unquoted string!");
            } else if (!validPred.test(ch)) {
                // invalid character -> next token
                break;
            }
        }
        if (startLocation == nextIndex) {
            // bad, we didn't read any characters at all
            throw new TokenizationNoMatchableCharacterException(nextIndex, nextIndex, "Input is invalid!");
        }
        return str.substring(startLocation, nextIndex);
    }

    /**
     * Consume the next string, assuming that the next character is a quote.
     *
     * @return The string that is extracted from the input.
     * @throws TokenizationMissingEndQuoteException if an end quote is missing.
     */
    private String nextQuotedString()
            throws TokenizationMissingEndQuoteException {
        int startLocation = nextIndex;
        char quote = str.charAt(nextIndex);
        assert quotePred.test(quote);
        boolean hasEndedWithQuote = false;
        for (++nextIndex; nextIndex < str.length(); ++nextIndex) {
            char ch = str.charAt(nextIndex);
            if (quote == ch) {
                // matching quote, break
                hasEndedWithQuote = true;
                ++nextIndex;
                break;
            }
        }
        if (hasEndedWithQuote) {
            // successful read of quoted string
            assert quotePred.test(str.charAt(startLocation)) && quotePred.test(str.charAt(nextIndex - 1))
                    : "String did not start or end with quotes!";
            // remove leading/trailing quote before returning
            return str.substring(startLocation + 1, nextIndex - 1);
        } else {
            // bad, we ran out of characters
            // rollback nextIndex
            nextIndex = startLocation;
            // throw an exception
            throw new TokenizationMissingEndQuoteException(nextIndex, str.length(),
                    "Reached end of string while reading quoted string!");
        }
    }

    /**
     * Consume the next token specified with the regex.
     *
     * @throws TokenizationNoMatchableCharacterException if the string does not match the given pattern.
     * @throws TokenizationEndOfStringException if the string has reached the end.
     */
    public String nextPattern(Pattern pattern)
            throws TokenizationNoMatchableCharacterException, TokenizationEndOfStringException {

        return nextMatcher(pattern).group();
    }

    /**
     * Consume the next token specified with the regex.
     *
     * @throws TokenizationNoMatchableCharacterException if the string does not match the given pattern.
     * @throws TokenizationEndOfStringException if the string has reached the end.
     */
    public Matcher nextMatcher(Pattern pattern)
            throws TokenizationNoMatchableCharacterException, TokenizationEndOfStringException {

        if (!hasNextToken()) {
            throw new TokenizationEndOfStringException("Reached end of string while reading delimiter!");
        }

        CharSequence tmp = str.subSequence(nextIndex, str.length());
        Matcher matcher = pattern.matcher(tmp);

        if (!matcher.find() || matcher.start() != 0) {
            // we could find a match, or the match didn't start from the current character
            // - this is not considered a match
            throw new TokenizationNoMatchableCharacterException(nextIndex, nextIndex,
                    "The next token does not match the given pattern!");
        }

        nextIndex += matcher.end();

        return matcher;
    }

    /**
     * Try to consume the next token specified with the regex.
     * Returns null if no token available.
     *
     * @throws TokenizationEndOfStringException if the string has reached the end.
     */
    public String tryNextPattern(Pattern pattern) throws TokenizationEndOfStringException {
        try {
            return nextPattern(pattern);
        } catch (TokenizationMismatchException e) {
            return null;
        }
    }

    /**
     * Try to consume the next token specified with the regex.
     * Returns null if no token available.
     *
     * @throws TokenizationEndOfStringException if the string has reached the end.
     */
    public Matcher tryNextMatcher(Pattern pattern) throws TokenizationEndOfStringException {
        try {
            return nextMatcher(pattern);
        } catch (TokenizationMismatchException e) {
            return null;
        }
    }

    /**
     * Consume the next string token.
     * Returns null if no token available.
     *
     * @param validPred A predicate that returns false if the given character is not allowed in this token.
     *                  If such a character is encountered, the token would be considered to have ended
     *                  just before that character.  It is only used for unquoted strings.
     *
     * @throws TokenizationEndOfStringException if the string has reached the end.
     */
    public String tryNextString(Predicate<Character> validPred) throws TokenizationEndOfStringException {
        try {
            return nextString(validPred);
        } catch (TokenizationMismatchException e) {
            return null;
        }
    }

    /**
     * Gets the current read location in the string.
     */
    public int getLocation() {
        return nextIndex;
    }

    /**
     * Sets the current read location in the string.
     */
    public void setLocation(int location) {
        assert(location >= 0 && location <= str.length());
        nextIndex = location;
    }

    /**
     * Gets all the tokens from this tokenizer.
     *
     * @throws TokenizationMissingEndQuoteException if an end quote is missing.
     * @throws TokenizationUnexpectedQuoteException if an unexpected quote was encountered.
     * @throws TokenizationNoMatchableCharacterException if no characters could be matched.
     */
    public List<String> toList()
        throws TokenizationMissingEndQuoteException, TokenizationUnexpectedQuoteException,
        TokenizationNoMatchableCharacterException, TokenizationEndOfStringException {
        ArrayList<String> items = new ArrayList<>();
        while (hasNextToken()) {
            items.add(nextString());
        }
        return items;
    }
}
