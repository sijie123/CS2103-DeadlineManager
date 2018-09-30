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
public interface TaskCollectionStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getTaskCollectionFilePath();

    /**
     * Returns TaskCollection data as a {@link ReadOnlyTaskCollection}. Returns {@code Optional.empty()}
     * if storage file is not found.
     *
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException             if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyTaskCollection> readTaskCollection() throws DataConversionException, IOException;

    /**
     * @see #getTaskCollectionFilePath()
     */
    Optional<ReadOnlyTaskCollection> readTaskCollection(Path filePath)
        throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyTaskCollection} to the storage.
     *
     * @param taskCollection cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveTaskCollection(ReadOnlyTaskCollection taskCollection) throws IOException;

    /**
     * @see #saveTaskCollection(ReadOnlyTaskCollection)
     */
    void saveTaskCollection(ReadOnlyTaskCollection addressBook, Path filePath) throws IOException;

}
