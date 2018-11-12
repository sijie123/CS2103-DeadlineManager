package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.io.IOException;

import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.events.storage.ImportExportExceptionEvent;
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

    @Test
    public void execute_exportOnExistingFile_exceptionThrown() {
        IOException expectedException = new IOException(
                String.format(StorageManager.MESSAGE_WRITE_FILE_EXISTS_ERROR, defaultFile));
        ExportCommand testCommand = new ExportCommand(defaultFile, false, false);
        ModelStubWithExportTaskCollection modelStub = new ModelStubWithExportTaskCollection(defaultFile, testCommand);
        assertCommandFailure(testCommand, modelStub, commandHistory,
                String.format(ExportCommand.MESSAGE_EXPORT_ERROR, expectedException.getMessage()));
    }

    @Test
    public void execute_exportNewFile_exportSuccessful() {
        ExportCommand testCommand = new ExportCommand(temporaryFilePath, false, false);
        ModelStubWithExportTaskCollection modelStub = new ModelStubWithExportTaskCollection(defaultFile, testCommand);
        assertCommandSuccess(testCommand, modelStub, commandHistory,
                String.format(ExportCommand.MESSAGE_SUCCESS, temporaryFilePath), modelStub);
    }

    @Test
    public void execute_overwriteExportOnExistingFile_exceptionThrown() {
        ExportCommand testCommand = new ExportCommand(defaultFile, true, false);
        ModelStubWithExportTaskCollection modelStub = new ModelStubWithExportTaskCollection(defaultFile, testCommand);
        assertCommandSuccess(testCommand, modelStub, commandHistory,
                String.format(ExportCommand.MESSAGE_SUCCESS, defaultFile), modelStub);
    }

    private class ModelStubWithExportTaskCollection extends ModelStub {
        private String filename = "";
        private ExportCommand testCommand = null;

        public ModelStubWithExportTaskCollection(String defaultFile) {
            filename = defaultFile;
        }

        public ModelStubWithExportTaskCollection(String defaultFile, ExportCommand importCommand) {
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
        public void exportTaskCollection(String filename, boolean shouldOverwrite, boolean isCsvFormat) {
            if (shouldOverwrite) {
                //No error.
                return;
            }
            if (filename.equals(this.filename)) {
                Exception fileExistException = new IOException(
                        String.format(Storage.MESSAGE_WRITE_FILE_EXISTS_ERROR, this.filename));
                testCommand.handleImportExportExceptionEvent(new ImportExportExceptionEvent(fileExistException));
                return;
            }
            //No error otherwise.
        }
    }
}
