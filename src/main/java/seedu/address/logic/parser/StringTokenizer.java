package seedu.address.logic.parser;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public String nextString() {
        return nextString(ch -> true);
    }

    /**
     * Consume the next string token.
     *
     * @param validPred A predicate that returns false if the given character is not allowed in this token.
     *                  If such a character is encountered, the token would be considered to have ended
     *                  just before that character.  It is only used for unquoted strings.
     *
     * @throws InputMismatchException if the next token is malformed as a standard text token.
     * @throws NoSuchElementException if the string has reached the end.
     */
    public String nextString(Predicate<Character> validPred) {
        if (!hasNextToken()) {
            throw new NoSuchElementException("Reached end of string while reading delimiter!");
        }

        int startLocation = nextIndex;
        if (quotePred.test(str.charAt(nextIndex))) {
            // starts with quote
            char quote = str.charAt(nextIndex);
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
                throw new InputMismatchException("Reached end of string while reading quoted string!");
            }
        } else {
            // does not start with quote
            // there should be no quotes within this string
            for (; nextIndex < str.length(); ++nextIndex) {
                char ch = str.charAt(nextIndex);
                if (delimPred.test(ch)) {
                    break;
                } else if (quotePred.test(ch)) {
                    // rollback nextIndex
                    nextIndex = startLocation;
                    // throw an exception
                    throw new InputMismatchException("Quotes encountered in the middle of unquoted string!");
                } else if (!validPred.test(ch)) {
                    // invalid character -> next token
                    break;
                }
            }
            if (startLocation == nextIndex) {
                throw new InputMismatchException("Input is invalid!");
            }
            return str.substring(startLocation, nextIndex);
        }
    }

    /**
     * Consume the next token specified with the regex.
     *
     * @throws InputMismatchException if the string does not match the given pattern.
     * @throws NoSuchElementException if the string has reached the end.
     */
    public String nextPattern(Pattern pattern) {
        return nextMatcher(pattern).group();
    }

    /**
     * Consume the next token specified with the regex.
     *
     * @throws InputMismatchException if the string does not match the given pattern.
     * @throws NoSuchElementException if the string has reached the end.
     */
    public Matcher nextMatcher(Pattern pattern) {
        if (!hasNextToken()) {
            throw new NoSuchElementException("Reached end of string while reading delimiter!");
        }

        CharSequence tmp = str.subSequence(nextIndex, str.length());
        Matcher matcher = pattern.matcher(tmp);

        if (!matcher.find() || matcher.start() != 0) {
            throw new InputMismatchException("The next token does not match the given pattern!");
        }

        nextIndex += matcher.end();

        return matcher;
    }

    /**
     * Try to consume the next token specified with the regex.
     * Returns null if no token available.
     *
     * @throws NoSuchElementException if the string has reached the end.
     */
    public String tryNextPattern(Pattern pattern) {
        try {
            return nextPattern(pattern);
        } catch (InputMismatchException e) {
            return null;
        }
    }

    /**
     * Try to consume the next token specified with the regex.
     * Returns null if no token available.
     *
     * @throws NoSuchElementException if the string has reached the end.
     */
    public Matcher tryNextMatcher(Pattern pattern) {
        try {
            return nextMatcher(pattern);
        } catch (InputMismatchException e) {
            return null;
        }
    }

    /**
     * Gets all the tokens from this tokenizer.
     */
    public List<String> toList() {
        ArrayList<String> items = new ArrayList<>();
        while (hasNextToken()) {
            items.add(nextString());
        }
        return items;
    }
}
