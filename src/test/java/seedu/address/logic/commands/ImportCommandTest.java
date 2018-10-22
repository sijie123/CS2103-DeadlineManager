package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.io.IOException;

import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.events.storage.ImportExportExceptionEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.model.ModelManager.ImportConflictMode;
import seedu.address.model.TaskCollection;
import seedu.address.model.task.Task;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ImportCommandTest {

    public final String temporaryFilePath = "dummySaveFile";
    public final String doesNotExistPath = "doesNotExist";
    private CommandHistory commandHistory = new CommandHistory();
    private String defaultFile = "fakeDefaultPath";

    @Test
    public void execute_importMissingFile_exceptionThrown() {
        IOException expectedException = new IOException(StorageManager.MESSAGE_READ_FILE_MISSING_ERROR);
        ImportCommand testCommand = new ImportCommand(doesNotExistPath);
        ModelStubWithImportAddressBook modelStub = new ModelStubWithImportAddressBook(defaultFile, testCommand);
        assertCommandFailure(testCommand, modelStub, commandHistory,
                String.format(ImportCommand.MESSAGE_IMPORT_ERROR, expectedException.toString()));
    }

    @Test
    public void execute_importExistingFile_importSuccessful() {
        ImportCommand testCommand = new ImportCommand(temporaryFilePath);
        ModelStubWithImportAddressBook modelStub = new ModelStubWithImportAddressBook(defaultFile, testCommand);
        assertCommandSuccess(testCommand, modelStub, commandHistory,
                String.format(ImportCommand.MESSAGE_SUCCESS, temporaryFilePath), modelStub);
    }

    private class ModelStubWithImportAddressBook extends ModelStub {
        private String filename = "";
        private ImportCommand testCommand = null;
        public ModelStubWithImportAddressBook(String defaultFile) {
            filename = defaultFile;
        }
        public ModelStubWithImportAddressBook(String defaultFile, ImportCommand importCommand) {
            this(defaultFile);
            testCommand = importCommand;
        }

        @Override
        public TaskCollection getTaskCollection() {
            return new TaskCollection();
        }

        @Override
        public ObservableList<Task> getFilteredTaskList() {
            return FXCollections.unmodifiableObservableList(new TaskCollection().getTaskList());
        }

        @Override
        public void importTaskCollection(String filename, ImportConflictMode conflictMode) {
            if (filename.equals(temporaryFilePath)) {
                //No error.
                return;
            }
            if (filename.equals(this.filename)) {
                Exception fileRepeatException = new IOException(Storage.MESSAGE_READ_FILE_SAME_ERROR);
                testCommand.handleImportExportExceptionEvent(new ImportExportExceptionEvent(fileRepeatException));
                return;
            }
            if (filename.equals(doesNotExistPath)) {
                Exception fileMissingException = new IOException(Storage.MESSAGE_READ_FILE_MISSING_ERROR);
                testCommand.handleImportExportExceptionEvent(new ImportExportExceptionEvent(fileMissingException));
                return;
            }
            throw new AssertionError("Should not use this filename");

        }
    }
}
