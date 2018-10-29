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
public interface TaskCollectionStorage extends TaskCollectionWriteStorage, TaskCollectionReadStorage {

    @Override
    Path getTaskCollectionFilePath();

    @Override
    Optional<ReadOnlyTaskCollection> readTaskCollection() throws DataConversionException, IOException;

    @Override
    Optional<ReadOnlyTaskCollection> readTaskCollection(Path filePath)
        throws DataConversionException, IOException;

    @Override
    void saveTaskCollection(ReadOnlyTaskCollection taskCollection) throws IOException;

    @Override
    void saveTaskCollection(ReadOnlyTaskCollection taskCollection, Path filePath) throws IOException;

}
