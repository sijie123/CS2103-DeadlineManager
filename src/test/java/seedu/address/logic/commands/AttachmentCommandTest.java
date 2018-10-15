package seedu.address.logic.commands;

import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.task.Task;

/**
 * Contains unit tests for AttachmentCommand.
 */
public class AttachmentCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();
    private static File nonExistentFile = new File("not-here1827364.txt");

    /**
     * Creates a temporary file for use in testing Attachment Command
     *
     * @return Temporary file
     * @throws IOException
     */
    private File createTestFile() throws IOException {
        File file = File.createTempFile("deadline-manager-attachment-test-", ".zip");
        return file;
    }

    private void deleteTestFile(File file) {
        assertTrue(file.delete());
    }


    /**
     * TODO:
     * - Add attachment
     * - Delete attachment
     * - List attachment
     * - Get attachment
     */

    @Test
    public void execute_addAttachmentFileNotFound_error() {
        Task task = model.getFilteredPersonList().get(0);
        String nonExistentFilePath = nonExistentFile.getPath();
        AttachmentCommand.AttachmentAction action =
            new AttachmentCommand.AddAttachmentAction(nonExistentFilePath);
        AttachmentCommand attachmentCommand = new AttachmentCommand(INDEX_FIRST_TASK, action);

        String expectedMessage = String.format(
            AttachmentCommand.AddAttachmentAction.MESSAGE_ADD_NOT_A_FILE, nonExistentFilePath);

        assertCommandFailure(attachmentCommand, model, commandHistory, expectedMessage);
    }
}
