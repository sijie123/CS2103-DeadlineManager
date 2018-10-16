package seedu.address.logic.commands;

import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.TaskCollection;
import seedu.address.model.UserPrefs;
import seedu.address.model.attachment.Attachment;
import seedu.address.model.task.Task;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains unit tests for AttachmentCommand.
 */
public class AttachmentCommandTest {
    private static File nonExistentFile = new File("blabla/not-here1827364.txt");

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    /**
     * Creates a temporary file for use in testing Attachment Command
     *
     * @return Temporary file
     * @throws IOException
     */
    private File createTestFile() {
        try {
            File file = File.createTempFile("deadline-manager-attachment-test-", ".zip");
            return file;
        } catch (IOException ioe) {
            return null;
        }
    }

    private void deleteTestFile(File file) {
        assertTrue(file.delete());
    }

    /**
     * Creates a copy of the provided task with additional attachments.
     * @param task The original task
     * @param attachments Attachments to be added to the task
     */
    private Task addAttachmentToTask(Task task, Attachment... attachments) {
        Set<Attachment> attachmentSet = new HashSet<>(task.getAttachments());
        List<Attachment> newAttachments = Arrays.asList(attachments);
        attachmentSet.addAll(new HashSet<>(newAttachments));
        Task modifiedTask = new PersonBuilder(task).withAttachments(attachmentSet).build();
        return modifiedTask;
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

    @Test
    public void execute_addAttachmentFile_success() {
        File tempFile = createTestFile();
        String filePath = tempFile.getAbsolutePath();
        AttachmentCommand.AttachmentAction action =
            new AttachmentCommand.AddAttachmentAction(filePath);
        AttachmentCommand attachmentCommand = new AttachmentCommand(INDEX_FIRST_TASK, action);
        String expectedMessage = String.format(
            AttachmentCommand.AddAttachmentAction.MESSAGE_SUCCESS, tempFile.getName());
        Task expectedTask = addAttachmentToTask(
            model.getFilteredPersonList().get(INDEX_FIRST_TASK.getZeroBased()),
            new Attachment(tempFile));

        Model expectedModel = new ModelManager(new TaskCollection(model.getAddressBook()),
            new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), expectedTask);
        expectedModel.commitAddressBook();

        assertCommandSuccess(attachmentCommand, model, commandHistory, expectedMessage, expectedModel);

        deleteTestFile(tempFile);
    }

    @Test
    public void execute_addAttachmentFileDuplicatedFile_error() {
        File tempFile = createTestFile();
        // Construct a file with one attachment
        Task task = addAttachmentToTask(
            model.getFilteredPersonList().get(INDEX_FIRST_TASK.getZeroBased()),
            new Attachment(tempFile));
        Model modelStub = new ModelManager(new TaskCollection(model.getAddressBook()),
            new UserPrefs());
        modelStub.updatePerson(model.getFilteredPersonList().get(0), task);
        modelStub.commitAddressBook();

        //Attempt to add it again using absolute path -> should fail
        String filePath = tempFile.getAbsolutePath();
        AttachmentCommand.AttachmentAction action =
            new AttachmentCommand.AddAttachmentAction(filePath);
        AttachmentCommand attachmentCommand = new AttachmentCommand(INDEX_FIRST_TASK, action);
        String expectedMessage =  Attachment.MESSAGE_DUPLICATE_ATTACHMENT_NAME;
        assertCommandFailure(attachmentCommand, modelStub, commandHistory, expectedMessage);

        //Attempt to add it using relative path --> should fail
        filePath = tempFile.getPath();
        action = new AttachmentCommand.AddAttachmentAction(filePath);
        attachmentCommand = new AttachmentCommand(INDEX_FIRST_TASK, action);
        assertCommandFailure(attachmentCommand, modelStub, commandHistory, expectedMessage);

        //Attempt to add a non-existing file with duplicated name --> should show file not exists
        filePath = tempFile.getName();
        action = new AttachmentCommand.AddAttachmentAction(filePath);
        attachmentCommand = new AttachmentCommand(INDEX_FIRST_TASK, action);
        expectedMessage = String.format(
            AttachmentCommand.AddAttachmentAction.MESSAGE_ADD_NOT_A_FILE, filePath);
        assertCommandFailure(attachmentCommand, modelStub, commandHistory, expectedMessage);

        deleteTestFile(tempFile);
    }

    @Test
    public void execute_addAttachmentMultipleFiles_success() {
        int numberOfFiles = 25;
        File[] tempFiles = new File[numberOfFiles];
        Task task = model.getFilteredPersonList().get(INDEX_FIRST_TASK.getZeroBased());
        Model expectedModel = new ModelManager(new TaskCollection(model.getAddressBook()),
            new UserPrefs());

        for (int i = 0; i < numberOfFiles; i++) {
            tempFiles[i] = createTestFile();
            String filePath = tempFiles[i].getAbsolutePath();
            AttachmentCommand.AttachmentAction action =
                new AttachmentCommand.AddAttachmentAction(filePath);
            AttachmentCommand attachmentCommand = new AttachmentCommand(INDEX_FIRST_TASK, action);
            String expectedMessage = String.format(
                AttachmentCommand.AddAttachmentAction.MESSAGE_SUCCESS, tempFiles[i].getName());
            Task expectedTask = addAttachmentToTask(task, new Attachment(tempFiles[i]));
            expectedModel.updatePerson(task, expectedTask);
            expectedModel.commitAddressBook();
            assertCommandSuccess(attachmentCommand, model, commandHistory, expectedMessage, expectedModel);
            task = expectedTask;
        }

        for (int i = 0; i < numberOfFiles; i++) {
            deleteTestFile(tempFiles[i]);
        }
    }

    @Test
    public void execute_deleteAttachmentInvalid_error() {
        Task task = model.getFilteredPersonList().get(0);
        String nonExistentFileName = nonExistentFile.getName();
        AttachmentCommand.AttachmentAction action =
            new AttachmentCommand.DeleteAttachmentAction(nonExistentFileName);
        AttachmentCommand attachmentCommand = new AttachmentCommand(INDEX_FIRST_TASK, action);

        String expectedMessage = String.format(
            AttachmentCommand.DeleteAttachmentAction.MESSAGE_NAME_NOT_FOUND, nonExistentFileName);

        assertCommandFailure(attachmentCommand, model, commandHistory, expectedMessage);
    }

    @Test
    public void execute_deleteAttachment_success() {
        File tempFile = createTestFile();
        // Construct a file with one attachment
        Task originalTask = model.getFilteredPersonList().get(INDEX_FIRST_TASK.getZeroBased());
        Task taskWithAttachment = addAttachmentToTask(originalTask, new Attachment(tempFile));
        Model modelStub = new ModelManager(new TaskCollection(model.getAddressBook()),
            new UserPrefs());
        modelStub.updatePerson(originalTask, taskWithAttachment);
        modelStub.commitAddressBook();

        Model baseModel = new ModelManager(new TaskCollection(modelStub.getAddressBook()),
            new UserPrefs());

        //Attempt to remove it from attachments
        String fileName = tempFile.getName();
        AttachmentCommand.AttachmentAction action =
            new AttachmentCommand.DeleteAttachmentAction(fileName);
        AttachmentCommand attachmentCommand = new AttachmentCommand(INDEX_FIRST_TASK, action);
        String expectedMessage =  String.format(
            AttachmentCommand.DeleteAttachmentAction.MESSAGE_SUCCESS, fileName);

        Model expectedModel = new ModelManager(new TaskCollection(modelStub.getAddressBook()),
            new UserPrefs());
        expectedModel.updatePerson(taskWithAttachment, originalTask);
        expectedModel.commitAddressBook();

        assertCommandSuccess(attachmentCommand, baseModel, commandHistory, expectedMessage, expectedModel);

        deleteTestFile(tempFile);
    }

}
