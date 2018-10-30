package seedu.address.storage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.ExportRequestEvent;
import seedu.address.commons.events.model.ImportRequestEvent;
import seedu.address.commons.events.model.TaskCollectionChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.events.storage.ImportDataAvailableEvent;
import seedu.address.commons.events.storage.ImportExportExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.ReadOnlyTaskCollection;
import seedu.address.model.UserPrefs;

/**
 * Manages storage of TaskCollection data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {
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
        throws IOException {
        if (!fileExists(filePath)) {
            throw new IOException(MESSAGE_READ_FILE_MISSING_ERROR);
        }
        TaskCollectionStorage importExportStorage = new XmlTaskCollectionStorage(filePath);
        logger.fine("Attempting to import from file: " + filePath);
        try {
            return importExportStorage.readTaskCollection(filePath);
        } catch (DataConversionException dce) {
            throw new IOException(String.format(MESSAGE_READ_FILE_PARSE_ERROR, filePath));
        }
    }

    @Override
    public void exportTaskCollection(ReadOnlyTaskCollection taskCollection, Path filePath, boolean shouldOverwrite)
        throws IOException {
        if (!shouldOverwrite && fileExists(filePath)) {
            throw new IOException(String.format(MESSAGE_WRITE_FILE_EXISTS_ERROR, filePath));
        }
        TaskCollectionStorage importExportStorage = new XmlTaskCollectionStorage(filePath);
        logger.fine("Attempting to export to file: " + filePath);
        try {
            importExportStorage.saveTaskCollection(taskCollection);
        } catch (IOException ioe) {
            throw new IOException(String.format(MESSAGE_WRITE_FILE_NO_PERMISSION_ERROR, filePath), ioe);
        }

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

    @Override
    @Subscribe
    public void handleExportRequestEvent(ExportRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Exporting file"));
        try {
            exportTaskCollection(event.data, getPathFromFileName(event.filename), event.overwrite);
        } catch (IOException e) {
            raise(new ImportExportExceptionEvent(e));
        }
    }

    @Override
    @Subscribe
    public void handleImportRequestEvent(ImportRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Importing file"));
        try {
            ReadOnlyTaskCollection data = importTaskCollection(getPathFromFileName(event.filename)).get();
            raise(new ImportDataAvailableEvent(data));
        } catch (IOException ioe) {
            raise(new ImportExportExceptionEvent(ioe));
        }
    }

    /**
     * Helper function to determine whether file exists.
     * @param filePath File to be inspected
     * @return true if file exists, false otherwise
     */
    private static boolean fileExists(Path filePath) {
        File file = new File(filePath.toString());
        return file.exists() && file.isFile();
    }


    private Path getPathFromFileName(String fileName) throws IOException {
        if (!FileUtil.isValidPath(fileName)) {
            throw new IOException(String.format(MESSAGE_FILE_INVALID_ERROR, fileName));
        }
        return Paths.get(fileName);
    }

}
