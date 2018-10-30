package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.events.model.ExportRequestEvent;
import seedu.address.commons.events.model.ImportRequestEvent;
import seedu.address.commons.events.model.TaskCollectionChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyTaskCollection;
import seedu.address.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends ImportExportStorage, TaskCollectionStorage, UserPrefsStorage {

    String MESSAGE_WRITE_FILE_EXISTS_ERROR = "Save file at %s already exists."
            + " Please rename or force export with the r/overwrite flag.";
    String MESSAGE_WRITE_FILE_NO_PERMISSION_ERROR = "Cannot write to the file at %s. "
            + " Please check your file permission settings.";
    String MESSAGE_FILE_INVALID_ERROR = "Filename %s is invalid. Please check your filename and try again.";
    String MESSAGE_READ_FILE_MISSING_ERROR = "File does not exist."
            + " Double check your import file.";
    String MESSAGE_READ_FILE_PARSE_ERROR = "Failed to read file at %s."
            + " The file format is incompatible with Deadine Manager.";

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

    @Override
    Optional<ReadOnlyTaskCollection> importTaskCollection(Path filePath)
            throws DataConversionException, IOException;

    @Override
    void exportTaskCollection(ReadOnlyTaskCollection taskCollection, Path filePath, boolean shouldOverwrite,
                              boolean isCsvFormat)
        throws IOException;

    /**
     * Saves the current version of the deadline manager to the hard disk. Creates the data file if it
     * is missing. Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleTaskCollectionChangedEvent(TaskCollectionChangedEvent abce);

    void handleExportRequestEvent(ExportRequestEvent ere);

    void handleImportRequestEvent(ImportRequestEvent ire);

}

