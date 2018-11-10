package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PRIORITY = new Prefix("p/");
    public static final Prefix PREFIX_FREQUENCY = new Prefix("f/");
    public static final Prefix PREFIX_DEADLINE = new Prefix("d/");
    public static final Prefix PREFIX_TAG = new Prefix("t/", true);

    /* Prefix definitions for attachments */
    public static final Prefix PREFIX_FILEPATH = new Prefix("p/");
    public static final Prefix PREFIX_FILENAME = new Prefix("n/");

    /* Prefix definitions for import and export command */
    public static final Prefix PREFIX_RESOLVER = new Prefix("r/");

    public static final String KEY_NAME_SHORT = "n";
    public static final String KEY_NAME_LONG = "name";
    public static final String KEY_DEADLINE_SHORT = "d";
    public static final String KEY_DEADLINE_MEDIUM = "due";
    public static final String KEY_DEADLINE_LONG = "deadline";
    public static final String KEY_PRIORITY_SHORT = "p";
    public static final String KEY_PRIORITY_LONG = "priority";
    public static final String KEY_FREQUENCY_SHORT = "f";
    public static final String KEY_FREQUENCY_LONG = "frequency";
    public static final String KEY_TAG_SHORT = "t";
    public static final String KEY_TAG_LONG = "tag";
    public static final String KEY_ATTACHMENT_SHORT = "a";
    public static final String KEY_ATTACHMENT_LONG = "attachment";
}
