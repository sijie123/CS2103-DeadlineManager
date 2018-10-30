package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;

import seedu.address.model.ReadOnlyTaskCollection;
import seedu.address.model.TaskCollection;

/**
 * Represents the output storage for {@link TaskCollection}.
 */
public interface TaskCollectionWriteStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getTaskCollectionFilePath();

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
    void saveTaskCollection(ReadOnlyTaskCollection taskCollection, Path filePath) throws IOException;

}
