package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.io.IOException;

import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.logic.CommandHistory;
import seedu.address.model.TaskCollection;
import seedu.address.model.task.Task;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ExportCommandTest {

    public final String temporaryFilePath = "dummySaveFile";
    private CommandHistory commandHistory = new CommandHistory();
    private String defaultFile = "fakeDefaultPath";
    private ModelStubWithExportAddressBook modelStub = new ModelStubWithExportAddressBook(defaultFile);

    @Test
    public void execute_exportOnWorkingFile_exceptionThrown() {
        IOException expectedException = new IOException(StorageManager.MESSAGE_WRITE_FILE_EXISTS_ERROR);
        assertCommandFailure(new ExportCommand(defaultFile), modelStub, commandHistory,
                String.format(ExportCommand.MESSAGE_EXPORT_ERROR, expectedException.toString()));
    }

    @Test
    public void execute_exportNewFile_exportSuccessful() {
        assertCommandSuccess(new ExportCommand(temporaryFilePath), modelStub,
                commandHistory, String.format(ExportCommand.MESSAGE_SUCCESS, temporaryFilePath), modelStub);
    }

    private class ModelStubWithExportAddressBook extends ModelStub {
        private String filename = "";
        private String lastError = null;
        public ModelStubWithExportAddressBook(String defaultFile) {
            filename = defaultFile;
        }

        @Override
        public TaskCollection getAddressBook() {
            return new TaskCollection();
        }

        @Override
        public ObservableList<Task> getFilteredPersonList() {
            return FXCollections.unmodifiableObservableList(new TaskCollection().getTaskList());
        }

        @Override
        public void exportAddressBook(String filename) {
            if (filename == this.filename) {
                lastError = new IOException(Storage.MESSAGE_WRITE_FILE_EXISTS_ERROR).toString();
            }
        }

        @Override
        public boolean importExportFailed() {
            return lastError != null;
        }

        @Override
        public String getLastError() {
            return lastError;
        }
    }
}
