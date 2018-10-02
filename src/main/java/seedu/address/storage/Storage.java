package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.events.model.TaskCollectionChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.ReadOnlyTaskCollection;
import seedu.address.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends TaskCollectionStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(UserPrefs userPrefs) throws IOException;

    @Override
    Path getTaskCollectionFilePath();

    @Override
    Optional<ReadOnlyTaskCollection> readTaskCollection() throws DataConversionException, IOException;

    @Override
    void saveTaskCollection(ReadOnlyTaskCollection taskCollection) throws IOException;

    Optional<ReadOnlyTaskCollection> importTaskCollection(Path filePath) throws DataConversionException, IOException;

    void exportTaskCollection(ReadOnlyTaskCollection taskCollection,
                              TaskCollectionStorage importExportTaskCollectionStorage)
                              throws IOException, IllegalValueException;

    /**
     * Saves the current version of the deadline manager to the hard disk. Creates the data file if it
     * is missing. Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleTaskCollectionChangedEvent(TaskCollectionChangedEvent abce);
}

