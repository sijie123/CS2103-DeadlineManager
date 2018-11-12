package systemtests;

import static seedu.address.logic.commands.CommandTestUtil.INVALID_EXPORT_PATH;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EXPORT_PATH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILEPATH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RESOLVER;
import static seedu.address.logic.parser.ImportCommandParser.FLAG_DUPLICATE;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.commands.ImportCommand;
import seedu.address.model.Model;
import seedu.address.model.task.Task;
import seedu.address.storage.StorageManager;

public class ImportExportCommandSystemTest extends TaskCollectionSystemTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void importExport() {

        // Export a file to a valid file path
        String filePath = getTempFilePath(VALID_EXPORT_PATH).toString();
        Model expectedModel = getModel();
        String command = ExportCommand.COMMAND_WORD + " " + PREFIX_FILEPATH + filePath;
        assertExportCommandSuccess(command, filePath, expectedModel);

        // Export a file again to a valid file path, but file exists.
        IOException expectedException = new IOException(
                String.format(StorageManager.MESSAGE_WRITE_FILE_EXISTS_ERROR, filePath));
        assertExportCommandFailure(command,
                String.format(ExportCommand.MESSAGE_EXPORT_ERROR, expectedException.getMessage()), expectedModel);

        // Export a file to a invalid file path
        filePath = getTempFilePath(INVALID_EXPORT_PATH).toString();
        command = ExportCommand.COMMAND_WORD + " " + PREFIX_FILEPATH + filePath;
        expectedException = new IOException(
                String.format(StorageManager.MESSAGE_WRITE_FILE_NO_PERMISSION_ERROR, filePath));
        assertExportCommandFailure(command,
                String.format(ExportCommand.MESSAGE_EXPORT_ERROR, expectedException.getMessage()), expectedModel);

        // Import a file from an invalid file path, without flags
        filePath = getTempFilePath(INVALID_EXPORT_PATH).toString();
        expectedModel = getModel();
        command = ImportCommand.COMMAND_WORD + " " + PREFIX_FILEPATH + filePath;
        expectedException = new IOException(StorageManager.MESSAGE_READ_FILE_MISSING_ERROR);
        assertImportCommandFailure(command,
                String.format(ImportCommand.MESSAGE_IMPORT_ERROR, expectedException.getMessage()), expectedModel);

        // Import a file from a valid file path, without flags
        filePath = getTempFilePath(VALID_EXPORT_PATH).toString();
        expectedModel = getModel();
        command = ImportCommand.COMMAND_WORD + " " + PREFIX_FILEPATH + filePath;
        assertImportCommandSuccess(command, filePath, expectedModel, false);

        // Import a file from a valid file path, with all flag
        filePath = getTempFilePath(VALID_EXPORT_PATH).toString();
        expectedModel = getModel();
        Model modelCopy = getModel();
        for (Task task: modelCopy.getTaskCollection().getTaskList()) {
            expectedModel.addTask(task);
        }
        command = ImportCommand.COMMAND_WORD + " "
                + PREFIX_FILEPATH + filePath + " "
                + PREFIX_RESOLVER + FLAG_DUPLICATE;
        assertImportCommandSuccess(command, filePath, expectedModel, true);

    }

    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the
     * result display box displays the correct success message. <br>
     * Also verifies that the status bar remains unchanged, and the command box
     * has the default style class, and the selected card updated accordingly, depending on {@code
     * cardStatus}.
     *
     * @see TaskCollectionSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertExportCommandSuccess(String command, String filePath, Model expectedModel) {
        String expectedResultMessage = String.format(
                ExportCommand.MESSAGE_SUCCESS, filePath);

        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the
     * result display box displays the correct success message. <br>
     * Also verifies that the status bar remains unchanged, and the command box
     * has the default style class, and the selected card updated accordingly, depending on {@code
     * cardStatus}.
     *
     * @see TaskCollectionSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertImportCommandSuccess(String command, String filePath, Model expectedModel,
                                            boolean hasModelChanged) {
        String expectedResultMessage = String.format(
                ImportCommand.MESSAGE_SUCCESS, filePath);

        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        if (hasModelChanged) {
            assertStatusBarUnchangedExceptSyncStatus();
        } else {
            assertStatusBarUnchanged();
        }

    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the
     * result display box displays {@code expectedResultMessage} and the model related components
     * equal to the current model as nothing should be changed in the event of an error. <br>
     * Also verifies that the browser url, selected card and status bar remain unchanged, and the command
     * box has the error style.
     *
     * @see TaskCollectionSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertImportCommandFailure(String command, String expectedResultMessage, Model expectedModel) {
        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the
     * result display box displays {@code expectedResultMessage} and the model related components
     * equal to the current model as nothing should be changed in the event of an error. <br>
     * Also verifies that the browser url, selected card and status bar remain unchanged, and the command
     * box has the error style.
     *
     * @see TaskCollectionSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertExportCommandFailure(String command, String expectedResultMessage, Model untouchedModel) {
        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, untouchedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.getRoot().toPath().resolve(fileName);
    }
}
