package seedu.address.logic.parser;

import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tokenizes arguments string of the form: {@code preamble <prefix>value <prefix>value ...}<br> e.g.
 * {@code some preamble text t/ 11.00 t/12.00 k/ m/ July}  where prefixes are {@code t/ k/ m/}.<br>
 * 1. An argument's value can be an empty string e.g. the value of {@code k/} in the above
 * example.<br> 2. Leading and trailing whitespaces of an argument value will be discarded.<br> 3.
 * An argument may be repeated and all its values will be accumulated e.g. the value of {@code t/}
 * in the above example.<br>
 */
public class ArgumentTokenizer {

    /**
     * Tokenizes an arguments string and returns an {@code ArgumentMultimap} object that maps
     * prefixes to their respective argument values. Only the given prefixes will be recognized in
     * the arguments string.
     *
     * @param argsString Arguments string of the form: {@code preamble <prefix>value <prefix>value
     *                   ...}
     * @param prefixes   Prefixes to tokenize the arguments string with
     * @return ArgumentMultimap object that maps prefixes to their arguments
     */
    public static ArgumentMultimap tokenize(String argsString, Prefix... prefixes) throws InputMismatchException {
        StringTokenizer tokenizer = new StringTokenizer(argsString);
        if (prefixes.length == 0) {
            return tokenizeWithoutPrefix(tokenizer, argsString);
        }
        Pattern pattern = makePattern(prefixes);
        return tokenize(tokenizer, pattern, prefixes);
    }

    /**
     * Extracts prefixes and their argument values based on the given pattern and input tokenizer,
     * and returns an {@code ArgumentMultimap} object that maps the extracted prefixes to their respective arguments.
     *
     * @param tokenizer StringTokenizer that contains the input string
     * @param pattern   Pattern that describes the combined prefix regex that matches all prefixes
     * @param prefixes  List of valid prefixes
     * @return Pattern object that represents the regular expression
     */
    private static ArgumentMultimap tokenize(StringTokenizer tokenizer, Pattern pattern, Prefix... prefixes)
        throws InputMismatchException {
        ArgumentMultimap argMultimap = new ArgumentMultimap();
        Prefix currPrefix = Prefix.EMPTY;
        StringBuilder currArgumentValue = new StringBuilder();
        boolean isEmpty = true;
        while (tokenizer.hasNextToken()) {
            Matcher prefixMatcher = tokenizer.tryNextMatcher(pattern);
            if (prefixMatcher == null) {
                String textToken = tokenizer.nextString();
                if (!isEmpty) {
                    currArgumentValue.append(' ');
                } else {
                    isEmpty = false;
                }
                currArgumentValue.append(textToken);
            } else {
                argMultimap.put(currPrefix, currArgumentValue.toString());
                currPrefix = findGroupPrefix(prefixMatcher, prefixes);
                currArgumentValue = new StringBuilder();
                isEmpty = true;
            }
        }
        argMultimap.put(currPrefix, currArgumentValue.toString());
        return argMultimap;
    }

    /**
     * Tokenizes an arguments string without any prefixes.
     *
     * @param argsString Arguments string
     * @return ArgumentMultimap object which only contains a single string
     */
    public static ArgumentMultimap tokenizeWithoutPrefix(StringTokenizer tokenizer, String argsString) {
        ArgumentMultimap argMultimap = new ArgumentMultimap();
        StringBuilder currArgumentValue = new StringBuilder();
        boolean isEmpty = true;
        while (tokenizer.hasNextToken()) {
            String textToken = tokenizer.nextString();
            if (!isEmpty) {
                currArgumentValue.append(' ');
            } else {
                isEmpty = false;
            }
            currArgumentValue.append(textToken);
        }
        argMultimap.put(Prefix.EMPTY, currArgumentValue.toString());
        return argMultimap;
    }

    /**
     * Constructs a Pattern (for use with regular expressions) from the given list of valid prefixes.
     *
     * @param prefixes Prefixes to tokenize the arguments string with
     * @return Pattern object that represents the regular expression
     */
    private static Pattern makePattern(Prefix... prefixes) {
        StringBuilder patternBuilder = new StringBuilder();
        boolean isEmpty = true;
        for (int i = 0; i != prefixes.length; ++i) {
            if (!isEmpty) {
                patternBuilder.append('|');
            }
            // the following like makes a named capturing group
            patternBuilder.append("(?<n" + Integer.toString(i) + ">" + Pattern.quote(prefixes[i].getPrefix()) + ")");
            isEmpty = false;
        }
        return Pattern.compile(patternBuilder.toString());
    }

    /**
     * Determines which prefix in the list of prefixes caused the given match to succeed.
     *
     * @param prefixMatcher Match that matches a prefix
     * @param prefixes      List of valid prefixes
     * @return Prefix that caused the match
     */
    private static Prefix findGroupPrefix(Matcher prefixMatcher, Prefix... prefixes) {
        for (int i = 0; i != prefixes.length; ++i) {
            if (prefixMatcher.group("n" + Integer.toString(i)) != null) {
                return prefixes[i];
            }
        }
        assert false : "Cannot find group prefix";
        throw new RuntimeException("Somehow, no prefix matches the given matcher.  This is a bug.");
    }

}
