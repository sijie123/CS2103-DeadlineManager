package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyTaskCollection;
import seedu.address.model.TaskCollection;

/**
 * Represents the input storage for {@link TaskCollection}.
 */
public interface TaskCollectionReadStorage {

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

}
