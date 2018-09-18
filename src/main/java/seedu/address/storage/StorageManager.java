package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyTaskCollection;
import seedu.address.model.UserPrefs;

/**
 * Manages storage of TaskCollection data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private TaskCollectionStorage taskCollectionStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(TaskCollectionStorage taskCollectionStorage,
                          UserPrefsStorage userPrefsStorage) {
        super();
        this.taskCollectionStorage = taskCollectionStorage;
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
    public Path getAddressBookFilePath() {
        return taskCollectionStorage.getAddressBookFilePath();
    }

    @Override
    public Optional<ReadOnlyTaskCollection> readAddressBook()
        throws DataConversionException, IOException {
        return readAddressBook(taskCollectionStorage.getAddressBookFilePath());
    }

    @Override
    public Optional<ReadOnlyTaskCollection> readAddressBook(Path filePath)
        throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return taskCollectionStorage.readAddressBook(filePath);
    }

    @Override
    public void saveAddressBook(ReadOnlyTaskCollection addressBook) throws IOException {
        saveAddressBook(addressBook, taskCollectionStorage.getAddressBookFilePath());
    }

    @Override
    public void saveAddressBook(ReadOnlyTaskCollection addressBook, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        taskCollectionStorage.saveAddressBook(addressBook, filePath);
    }


    @Override
    @Subscribe
    public void handleAddressBookChangedEvent(AddressBookChangedEvent event) {
        logger.info(
            LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveAddressBook(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

}
