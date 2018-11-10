package seedu.address.model.attachment;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.function.Predicate;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.StringUtil;
import seedu.address.model.task.FilterOperator;
import seedu.address.model.task.exceptions.InvalidPredicateOperatorException;

/**
 * Represents a Attachment in the deadline manager. Guarantees: immutable;
 */
public class Attachment {
    public static final String MESSAGE_DUPLICATE_ATTACHMENT_NAME = "There cannot be more than one attachment"
            + "with the same file name. Please rename one of them.";
    private static final Logger logger = LogsCenter.getLogger(Attachment.class);

    public final File file;


    public Attachment(File file) {
        requireNonNull(file);
        this.file = file;
    }

    /**
     * Checks if the attachment can be read by the system.
     * THat is, if it exists, it is a file and it is possible for us to read it.
     *
     * @return True if the attachment can be read by the system, False otherwise
     */
    public boolean isReadable() {
        if (!file.exists()) {
            logger.info(String.format("Attachment %s is not readable as file.exists() fails", file));
            return false;
        }
        if (!file.isFile()) {
            logger.info(String.format("Attachment %s is not readable as file.isFile() fails", file));
            return false;
        }
        if (!file.canRead()) {
            logger.info(String.format("Attachment %s is not readable as file.canRead() fails", file));
            return false;
        }
        if (!Files.isReadable(FileSystems.getDefault().getPath(file.getPath()))) {
            logger.info(String.format("Attachment %s is not readable as isReadable fails", file));
            return false;
        }
        return true;
    }

    /**
     * Copies the file from file to a specified location, overwriting the file at destination if a file exists
     *
     * @param savePath path to save the attachment to
     */
    public File saveTo(String savePath) throws IOException {
        File destination = new File(savePath);
        if (!FileUtil.isValidPath(savePath)) {
            throw new IOException("Invalid file path");
        }
        if (destination.exists()) {
            logger.warning(
                    String.format("Attachment destination %s will be overwritten.", destination.getAbsolutePath()));
        }

        File copiedFile = Files.copy(file.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING).toFile();
        return copiedFile;
    }

    /**
     * @return Name of the attachment, which is the file name.
     */
    public String getName() {
        return file.getName();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Attachment // instanceof handles nulls
                    && file.getAbsolutePath().equals(((Attachment) other).file.getAbsolutePath())); // state check
    }

    @Override
    public int hashCode() {
        return file.getAbsolutePath().hashCode();
    }

    @Override
    public String toString() {
        return file.getName();
    }

    /**
     * Constructs a predicate from the given operator and test phrase.
     *
     * @param operator   The operator for this predicate.
     * @param testPhrase The test phrase for this predicate.
     */
    public static Predicate<Attachment> makeFilter(FilterOperator operator, String testPhrase)
            throws InvalidPredicateOperatorException {
        switch (operator) {
        case EQUAL:
            return attachment -> StringUtil.equalsIgnoreCase(attachment.getName(), testPhrase);
        case LESS:
            return attachment -> StringUtil.containsFragmentIgnoreCase(testPhrase, attachment.getName());
        case CONVENIENCE: // convenience operator, works the same as ">"
        case GREATER:
            return attachment -> StringUtil.containsFragmentIgnoreCase(attachment.getName(), testPhrase);
        default:
            throw new InvalidPredicateOperatorException();
        }
    }
}
