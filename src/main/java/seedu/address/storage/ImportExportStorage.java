package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyTaskCollection;
import seedu.address.model.TaskCollection;

/**
 * Represents a storage for {@link TaskCollection}.
 */
public interface ImportExportStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getTaskCollectionFilePath();

    /**
     * Returns TaskCollection data as a {@link ReadOnlyTaskCollection}. Returns {@code Optional.empty()}
     * if import file is not found.
     *
     * @param filePath location of import file.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException             if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyTaskCollection> importTaskCollection(Path filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyTaskCollection} to the path specified.
     * @param taskCollection cannot be null. The task collection to be saved.
     * @param filePath the destination to save the file
     * @param shouldOverwrite if the file exists, whether the file should be overwritten.
     * @param isCsvFormat whether the task collection should be saved as a CSV or a XML file.
     * @throws IOException
     */
    void exportTaskCollection(ReadOnlyTaskCollection taskCollection, Path filePath, boolean shouldOverwrite,
                              boolean isCsvFormat)
        throws IOException;

}
