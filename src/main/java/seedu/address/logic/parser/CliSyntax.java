package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PRIORITY = new Prefix("P/");
    public static final Prefix PREFIX_FREQUENCY = new Prefix("f/");
    public static final Prefix PREFIX_DEADLINE = new Prefix("d/");
    public static final Prefix PREFIX_TAG = new Prefix("t/");

    /* Prefix definitions for attachments */
    public static final Prefix PREFIX_FILEPATH = new Prefix("p/");
    public static final Prefix PREFIX_FILENAME = new Prefix("n/");
}
