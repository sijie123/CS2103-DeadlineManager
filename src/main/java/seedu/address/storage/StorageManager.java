package seedu.address.storage;
import static java.util.Objects.requireNonNull;

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
import seedu.address.storage.csvstorage.CsvTaskCollectionWriteStorage;
import seedu.address.storage.xmlstorage.XmlTaskCollectionStorage;

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
    public Optional<ReadOnlyTaskCollection> importTaskCollection(Path filePath) throws IOException {
        if (!isExistingFile(filePath)) {
            throw new IOException(MESSAGE_READ_FILE_MISSING_ERROR);
        }
        TaskCollectionReadStorage importExportStorage = new XmlTaskCollectionStorage(filePath);
        logger.fine("Attempting to import from file: " + filePath);
        try {
            return importExportStorage.readTaskCollection(filePath);
        } catch (DataConversionException dce) {
            throw new IOException(String.format(MESSAGE_READ_FILE_PARSE_ERROR, filePath));
        }
    }

    @Override
    public void exportTaskCollection(ReadOnlyTaskCollection taskCollection, Path filePath, boolean shouldOverwrite,
                                     boolean isCsvFormat) throws IOException {
        if (!shouldWriteToPath(filePath, shouldOverwrite)) {
            throw new IOException(String.format(MESSAGE_WRITE_FILE_EXISTS_ERROR, filePath));
        }
        TaskCollectionWriteStorage exportStorage =
            createExportStorageFromPathname(filePath, isCsvFormat);
        logger.fine("Attempting to export to file: " + filePath);
        try {
            exportStorage.saveTaskCollection(taskCollection);
        } catch (IOException ioe) {
            throw new IOException(String.format(MESSAGE_WRITE_FILE_NO_PERMISSION_ERROR, filePath), ioe);
        }
    }

    /**
     * Determines whether export should be written to the path, or we should abort the export.
     * @param filePath path to write to
     * @param shouldOverwrite whether we should overwrite if file exists
     * @return true if we should write, false otherwise
     */
    private boolean shouldWriteToPath(Path filePath, boolean shouldOverwrite) {
        if (isExistingFile(filePath)) {
            if (!shouldOverwrite) {
                return false;
            }
            return isWritablePath(filePath);
        }
        return true;
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
        try {
            requireNonNull(event);
        } catch (NullPointerException npe) {
            raise(new ImportExportExceptionEvent(npe));
            return;
        }
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Exporting file"));
        try {
            exportTaskCollection(event.data, getPathFromFileName(event.filename), event.overwrite, event.isCsvFormat);
        } catch (IOException e) {
            raise(new ImportExportExceptionEvent(e));
        }
    }

    /**
     * Creates the relevant write storage from pathname and whether CSV is required.
     * @param pathname the pathname to export to
     * @param isCsvFormat whether the file should be exported in CSV format (or xml otherwise)
     * @return a TaskCollectionWriteStorage that can be used to export.
     */
    private TaskCollectionWriteStorage createExportStorageFromPathname(Path pathname, boolean isCsvFormat) {
        if (isCsvFormat) {
            return new CsvTaskCollectionWriteStorage(pathname);
        } else {
            return new XmlTaskCollectionStorage(pathname);
        }
    }

    @Override
    @Subscribe
    public void handleImportRequestEvent(ImportRequestEvent event) {
        try {
            requireNonNull(event);
        } catch (NullPointerException npe) {
            raise(new ImportExportExceptionEvent(npe));
            return;
        }
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
     * Works on both Windows and Unix-based systems.
     * Windows behaviour: filename is case-insensitive.
     * Unix behaviour: filename is case-sensitive.
     * @param filePath File to be inspected
     * @return true if file exists, false otherwise
     */
    private static boolean isExistingFile(Path filePath) {
        File file = new File(filePath.toString());
        /*
        try {
            if (!file.exists()) {
                return false;
            }
            //Conduct case-insensitive check on filename, for Windows-based systems.
            return file.getCanonicalFile().getName().equals(file.getName());
        } catch (IOException ioe) {
            return false;
        }*/
        return file.exists();
    }

    /**
     * Determines if the filePath is writable.
     * Note that filePaths are not writable if they represent a directory.
     * @param filePath the filePath to be analysed
     * @return true if the filePath is writable, false otherwise.
     */
    private static boolean isWritablePath(Path filePath) {
        File file = new File(filePath.toString());
        if (file.isDirectory()) {
            return false;
        }
        return file.canWrite();
    }


    private Path getPathFromFileName(String fileName) throws IOException {
        if (!FileUtil.isValidPath(fileName)) {
            throw new IOException(String.format(MESSAGE_FILE_INVALID_ERROR, fileName));
        }
        return Paths.get(fileName);
    }

}
