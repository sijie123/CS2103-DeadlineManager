package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.TaskCollectionChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.ReadOnlyTaskCollection;
import seedu.address.model.UserPrefs;

/**
 * Manages storage of TaskCollection data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    public static final String MESSAGE_WRITE_SAME_FILE_ERROR = "Cannot overwrite save file! "
            + "Export as a different filename.";
    public static final String MESSAGE_READ_SAME_FILE_ERROR = "Cannot import from running data file."
            + "Double check your import file.";
    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private TaskCollectionStorage privateTaskCollectionStorage;
    private UserPrefsStorage userPrefsStorage;

    public StorageManager(TaskCollectionStorage privateTaskCollectionStorage,
                          UserPrefsStorage userPrefsStorage) {
        super();
        this.privateTaskCollectionStorage = privateTaskCollectionStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }

    // ================ TaskCollection methods ==============================

    @Override
    public Path getTaskCollectionFilePath() {
        return privateTaskCollectionStorage.getTaskCollectionFilePath();
    }

    @Override
    public Optional<ReadOnlyTaskCollection> readTaskCollection()
        throws DataConversionException, IOException {
        return readTaskCollection(privateTaskCollectionStorage.getTaskCollectionFilePath());
    }

    @Override
    public Optional<ReadOnlyTaskCollection> readTaskCollection(Path filePath)
        throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return privateTaskCollectionStorage.readTaskCollection(filePath);
    }

    @Override
    public void saveTaskCollection(ReadOnlyTaskCollection taskCollection) throws IOException {
        saveTaskCollection(taskCollection, privateTaskCollectionStorage.getTaskCollectionFilePath());
    }

    @Override
    public void saveTaskCollection(ReadOnlyTaskCollection taskCollection, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        privateTaskCollectionStorage.saveTaskCollection(taskCollection, filePath);
    }

    @Override
    public Optional<ReadOnlyTaskCollection> importTaskCollection(Path filePath)
            throws DataConversionException, IOException, IllegalValueException {
        if (privateTaskCollectionStorage.getTaskCollectionFilePath().equals(filePath)) {
            throw new IllegalValueException(MESSAGE_READ_SAME_FILE_ERROR);
        }
        TaskCollectionStorage importExportStorage = new XmlTaskCollectionStorage(filePath);
        logger.fine("Attempting to import from file: " + filePath);
        return importExportStorage.readTaskCollection(filePath);
    }

    @Override
    public void exportTaskCollection(ReadOnlyTaskCollection taskCollection,
                                     Path filePath)
                                     throws IOException, IllegalValueException {
        if (privateTaskCollectionStorage.getTaskCollectionFilePath().equals(filePath)) {
            throw new IllegalValueException(MESSAGE_WRITE_SAME_FILE_ERROR);
        }
        TaskCollectionStorage importExportStorage = new XmlTaskCollectionStorage(filePath);
        logger.fine("Attempting to export to file: " + filePath);
        importExportStorage.saveTaskCollection(taskCollection);
    }

    @Override
    @Subscribe
    public void handleTaskCollectionChangedEvent(TaskCollectionChangedEvent event) {
        logger.info(
            LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveTaskCollection(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

}
