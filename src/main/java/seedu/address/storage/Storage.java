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

    String MESSAGE_WRITE_FILE_EXISTS_ERROR = "Save file already exists."
            + " Please rename to avoid losing data.";
    String MESSAGE_READ_FILE_MISSING_ERROR = "File does not exist."
            + " Double check your import file.";
    String MESSAGE_READ_FILE_SAME_ERROR = "Cannot import from the current working file.";

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

    /**
     * Imports a task collection from an external save file. The file must exist, otherwise an IOException
     * will be thrown.
     * @param filePath file to import
     * @return The task collection wrapped in an Optional class
     * @throws DataConversionException
     * @throws IOException
     */
    @Override
    Optional<ReadOnlyTaskCollection> importTaskCollection(Path filePath)
            throws DataConversionException, IOException;

    /**
     * Exports the current view of task collection to a path specified. The path must not already exist,
     * otherwise an IOException will be thrown
     * @param taskCollection A view of the task collection to be exported
     * @param filePath The file to export to
     * @throws IOException
     */
    @Override
    void exportTaskCollection(ReadOnlyTaskCollection taskCollection, Path filePath) throws IOException;


    /**
     * Saves the current version of the deadline manager to the hard disk. Creates the data file if it
     * is missing. Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleTaskCollectionChangedEvent(TaskCollectionChangedEvent abce);

    void handleExportRequestEvent(ExportRequestEvent ere);

    void handleImportRequestEvent(ImportRequestEvent ire);

}

