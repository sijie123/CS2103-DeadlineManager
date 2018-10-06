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
    private final String str;
    private final Predicate<Character> delimPred;
    private final Predicate<Character> quotePred;
    private int nextIndex;

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
     * Consume the next tokens.
     */
    public String nextString() {
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
                assert quotePred.test(str.charAt(startLocation)) && quotePred.test(str.charAt(nextIndex - 1)) : "String did not start or end with quotes!";
                // remove leading/trailing quote before returning
                String ret = str.substring(startLocation + 1, nextIndex - 1);
                return ret;
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
                }
            }
            return str.substring(startLocation, nextIndex);
        }
    }

    /**
     * Consume the next token specified with the regex.
     */
    public String nextPattern(Pattern pattern) {
        if (!hasNextToken()) {
            throw new NoSuchElementException("Reached end of string while reading delimiter!");
        }

        Matcher matcher = pattern.matcher(str.subSequence(nextIndex, str.length()));

        if (!matcher.matches() || matcher.start() != 0) {
            throw new InputMismatchException("The next token does not match the given pattern!");
        }

        nextIndex += matcher.end();

        return matcher.group();
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
