package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_TASK;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_TASK;
import static seedu.address.testutil.TypicalTasks.getTypicalTaskCollections;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.TaskCollection;
import seedu.address.model.UserPrefs;
import seedu.address.model.attachment.Attachment;
import seedu.address.model.task.Task;
import seedu.address.testutil.TaskBuilder;

/**
 * Contains unit tests for AttachmentCommand.
 */
public class AttachmentCommandTest {
    private static File nonExistentFile = new File("blabla/not-here1827364.txt");

    private Model model = new ModelManager(getTypicalTaskCollections(), new UserPrefs());
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
     *
     * @param task        The original task
     * @param attachments Attachments to be added to the task
     */
    private Task addAttachmentToTask(Task task, Attachment... attachments) {
        Set<Attachment> attachmentSet = new HashSet<>(task.getAttachments());
        List<Attachment> newAttachments = Arrays.asList(attachments);
        attachmentSet.addAll(new HashSet<>(newAttachments));
        Task modifiedTask = new TaskBuilder(task).withAttachments(attachmentSet).build();
        return modifiedTask;
    }

    @Test
    public void execute_addAttachmentFileNotFound_error() {
        Task task = model.getFilteredTaskList().get(0);
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
            model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased()),
            new Attachment(tempFile));

        Model expectedModel = new ModelManager(new TaskCollection(model.getTaskCollection()),
            new UserPrefs());
        expectedModel.updateTask(model.getFilteredTaskList().get(0), expectedTask);
        expectedModel.commitTaskCollection();

        assertCommandSuccess(attachmentCommand, model, commandHistory, expectedMessage, expectedModel);

        deleteTestFile(tempFile);
    }

    @Test
    public void execute_addAttachmentFileDuplicatedFile_error() {
        File tempFile = createTestFile();
        // Construct a file with one attachment
        Task task = addAttachmentToTask(
            model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased()),
            new Attachment(tempFile));
        Model modelStub = new ModelManager(new TaskCollection(model.getTaskCollection()),
            new UserPrefs());
        modelStub.updateTask(model.getFilteredTaskList().get(0), task);
        modelStub.commitTaskCollection();

        //Attempt to add it again using absolute path -> should fail
        String filePath = tempFile.getAbsolutePath();
        AttachmentCommand.AttachmentAction action =
            new AttachmentCommand.AddAttachmentAction(filePath);
        AttachmentCommand attachmentCommand = new AttachmentCommand(INDEX_FIRST_TASK, action);
        String expectedMessage = Attachment.MESSAGE_DUPLICATE_ATTACHMENT_NAME;
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
        Task task = model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        Model expectedModel = new ModelManager(new TaskCollection(model.getTaskCollection()),
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
            expectedModel.updateTask(task, expectedTask);
            expectedModel.commitTaskCollection();
            assertCommandSuccess(attachmentCommand, model, commandHistory, expectedMessage, expectedModel);
            task = expectedTask;
        }

        for (int i = 0; i < numberOfFiles; i++) {
            deleteTestFile(tempFiles[i]);
        }
    }

    @Test
    public void execute_deleteAttachmentInvalid_error() {
        Task task = model.getFilteredTaskList().get(0);
        String nonExistentFileName = nonExistentFile.getName();
        AttachmentCommand.AttachmentAction action =
            new AttachmentCommand.DeleteAttachmentAction(nonExistentFileName);
        AttachmentCommand attachmentCommand = new AttachmentCommand(INDEX_FIRST_TASK, action);
        String expectedMessage = String.format(
            AttachmentCommand.MESSAGE_NAME_NOT_FOUND, nonExistentFileName);
        assertCommandFailure(attachmentCommand, model, commandHistory, expectedMessage);
    }

    @Test
    public void execute_deleteAttachment_success() {
        File tempFile = createTestFile();
        // Construct a file with one attachment
        Task originalTask = model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        Task taskWithAttachment = addAttachmentToTask(originalTask, new Attachment(tempFile));
        Model modelStub = new ModelManager(new TaskCollection(model.getTaskCollection()),
            new UserPrefs());
        modelStub.updateTask(originalTask, taskWithAttachment);
        modelStub.commitTaskCollection();

        Model baseModel = new ModelManager(new TaskCollection(modelStub.getTaskCollection()),
            new UserPrefs());

        //Attempt to remove it from attachments
        String fileName = tempFile.getName();
        AttachmentCommand.AttachmentAction action =
            new AttachmentCommand.DeleteAttachmentAction(fileName);
        AttachmentCommand attachmentCommand = new AttachmentCommand(INDEX_FIRST_TASK, action);
        String expectedMessage = String.format(
            AttachmentCommand.DeleteAttachmentAction.MESSAGE_SUCCESS, fileName);

        Model expectedModel = new ModelManager(new TaskCollection(modelStub.getTaskCollection()),
            new UserPrefs());
        expectedModel.updateTask(taskWithAttachment, originalTask);
        expectedModel.commitTaskCollection();

        assertCommandSuccess(attachmentCommand, baseModel, commandHistory, expectedMessage, expectedModel);

        deleteTestFile(tempFile);
    }

    @Test
    public void execute_listAttachment_success() {
        AttachmentCommand.AttachmentAction action = new AttachmentCommand.ListAttachmentAction();
        AttachmentCommand attachmentCommand = new AttachmentCommand(INDEX_FIRST_TASK, action);
        Task task = model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased());

        Set<Attachment> attachments = task.getAttachments();
        String expectedMessage = String.format(
            AttachmentCommand.ListAttachmentAction.MESSAGE_TOTAL_ATTACHMENTS, attachments.size());
        int indexCounter = 0;
        for (Attachment attachment : attachments) {
            indexCounter++;
            expectedMessage += String.format(
                AttachmentCommand.ListAttachmentAction.MESSAGE_LIST_ATTACHMENT_DETAILS, indexCounter,
                attachment.toString());
        }

        Model expectedModel = model;
        assertCommandSuccess(attachmentCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_listAttachment_error() {
        AttachmentCommand.AttachmentAction action = new AttachmentCommand.ListAttachmentAction();
        Index invalidIndex = Index.fromOneBased(model.getFilteredTaskList().size() + 5);
        AttachmentCommand attachmentCommand = new AttachmentCommand(invalidIndex, action);
        String expectedMessage = Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX;
        assertCommandFailure(attachmentCommand, model, commandHistory, expectedMessage);
    }


    @Test
    public void execute_getAttachmentInvalid_error() {
        Task task = model.getFilteredTaskList().get(0);
        String nonExistentFileName = nonExistentFile.getName();
        AttachmentCommand.AttachmentAction action =
            new AttachmentCommand.GetAttachmentAction(nonExistentFileName, nonExistentFileName);
        AttachmentCommand attachmentCommand = new AttachmentCommand(INDEX_FIRST_TASK, action);

        String expectedMessage = String.format(
            AttachmentCommand.MESSAGE_NAME_NOT_FOUND, nonExistentFileName);

        assertCommandFailure(attachmentCommand, model, commandHistory, expectedMessage);
    }


    @Test
    public void execute_getAttachment_success() throws IOException {
        File tempFile = createTestFile();
        // Initialize file with some content that can be verified later
        String fileContent = "Hello Rar the Cat!\n";
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tempFile.getPath()));
        bufferedWriter.write(fileContent);
        bufferedWriter.close();

        // Construct a file with one attachment
        Task originalTask = model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        Task taskWithAttachment = addAttachmentToTask(originalTask, new Attachment(tempFile));
        Model modelStub = new ModelManager(new TaskCollection(model.getTaskCollection()),
            new UserPrefs());
        modelStub.updateTask(originalTask, taskWithAttachment);
        modelStub.commitTaskCollection();


        File outputFile = createTestFile();
        deleteTestFile(outputFile);

        //Attempt to get it from attachments
        String fileName = tempFile.getName();
        String outputPath = outputFile.getPath();
        AttachmentCommand.AttachmentAction action =
            new AttachmentCommand.GetAttachmentAction(fileName, outputPath);
        AttachmentCommand attachmentCommand = new AttachmentCommand(INDEX_FIRST_TASK, action);
        String expectedMessage = String.format(
            AttachmentCommand.GetAttachmentAction.MESSAGE_SUCCESS, fileName, outputPath);

        Model expectedModel = modelStub;

        assertCommandSuccess(attachmentCommand, modelStub, commandHistory, expectedMessage, expectedModel);

        // Check if output file have the correct content
        BufferedReader bufferedReader = new BufferedReader(new FileReader(outputFile.getPath()));
        assertTrue(bufferedReader.readLine().equals(fileContent.trim()));
        bufferedReader.close();

        bufferedReader = new BufferedReader(new FileReader(tempFile.getPath()));
        assertTrue(bufferedReader.readLine().equals(fileContent.trim()));
        bufferedReader.close();

        deleteTestFile(tempFile);
        deleteTestFile(outputFile);
    }

    @Test
    public void execute_getAttachmentSameFile_success() throws IOException {
        File tempFile = createTestFile();
        // Initialize file with some content that can be verified later
        String fileContent = "Hello Rar the Cat!\n";
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tempFile.getPath()));
        bufferedWriter.write(fileContent);
        bufferedWriter.close();

        // Construct a file with one attachment
        Task originalTask = model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        Task taskWithAttachment = addAttachmentToTask(originalTask, new Attachment(tempFile));
        Model modelStub = new ModelManager(new TaskCollection(model.getTaskCollection()),
            new UserPrefs());
        modelStub.updateTask(originalTask, taskWithAttachment);
        modelStub.commitTaskCollection();

        // Same output location as where the attachment is
        File outputFile = tempFile;

        //Attempt to get it from attachments
        String fileName = tempFile.getName();
        String outputPath = outputFile.getPath();
        AttachmentCommand.AttachmentAction action =
            new AttachmentCommand.GetAttachmentAction(fileName, outputPath);
        AttachmentCommand attachmentCommand = new AttachmentCommand(INDEX_FIRST_TASK, action);
        String expectedMessage = String.format(
            AttachmentCommand.GetAttachmentAction.MESSAGE_SUCCESS, fileName, outputPath);

        Model expectedModel = modelStub;

        assertCommandSuccess(attachmentCommand, modelStub, commandHistory, expectedMessage, expectedModel);

        // Check if output file have the correct content
        BufferedReader bufferedReader = new BufferedReader(new FileReader(outputFile.getPath()));
        assertTrue(bufferedReader.readLine().equals(fileContent.trim()));
        bufferedReader.close();

        bufferedReader = new BufferedReader(new FileReader(tempFile.getPath()));
        assertTrue(bufferedReader.readLine().equals(fileContent.trim()));
        bufferedReader.close();

        deleteTestFile(tempFile);
    }

    @Test
    public void equals() {
        AttachmentCommand.AttachmentAction add = new AttachmentCommand.AddAttachmentAction("helloworld.docx");
        assertEquals(add, new AttachmentCommand.AddAttachmentAction("helloworld.docx"));
        assertNotEquals(add, new AttachmentCommand.AddAttachmentAction("meow.txt"));

        AttachmentCommand.AttachmentAction delete = new AttachmentCommand.DeleteAttachmentAction(
            "delete please.docx");
        assertEquals(delete, new AttachmentCommand.DeleteAttachmentAction("delete please.docx"));
        assertNotEquals(delete, new AttachmentCommand.DeleteAttachmentAction("hello.txt"));
        assertNotEquals(delete, add);

        AttachmentCommand.AttachmentAction list = new AttachmentCommand.ListAttachmentAction();
        assertEquals(list, new AttachmentCommand.ListAttachmentAction());
        assertNotEquals(list, add);
        assertNotEquals(list, delete);

        AttachmentCommand.AttachmentAction get = new AttachmentCommand.GetAttachmentAction("helloworld.txt",
            "hello/world/helloworld.txt");
        assertEquals(get, new AttachmentCommand.GetAttachmentAction(
            "helloworld.txt", "hello/world/helloworld.txt"));
        assertNotEquals(get, new AttachmentCommand.GetAttachmentAction("hello/world/helloworld.txt",
            "helloworld.txt"));
        assertNotEquals(get, list);

        AttachmentCommand attachmentCommand = new AttachmentCommand(INDEX_SECOND_TASK, list);
        assertEquals(attachmentCommand, new AttachmentCommand(INDEX_SECOND_TASK, list));
        assertNotEquals(attachmentCommand, new AttachmentCommand(INDEX_THIRD_TASK, list));
        assertNotEquals(attachmentCommand, new AttachmentCommand(INDEX_SECOND_TASK, get));
        assertNotEquals(attachmentCommand, null);

        assertNotEquals(add, attachmentCommand);
        assertNotEquals(delete, attachmentCommand);
        assertNotEquals(list, attachmentCommand);
        assertNotEquals(get, attachmentCommand);

        assertEquals(attachmentCommand, attachmentCommand);
        assertEquals(add, add);
        assertEquals(delete, delete);
        assertEquals(list, list);
        assertEquals(get, get);
    }

    @Test
    public void toStringTest() {
        AttachmentCommand.AttachmentAction add = new AttachmentCommand.AddAttachmentAction("helloworld.docx");
        AttachmentCommand.AttachmentAction delete = new AttachmentCommand.DeleteAttachmentAction(
            "delete please.docx");
        AttachmentCommand.AttachmentAction list = new AttachmentCommand.ListAttachmentAction();
        AttachmentCommand.AttachmentAction get = new AttachmentCommand.GetAttachmentAction("helloworld.txt",
            "hello/world/helloworld.txt");
        AttachmentCommand attachmentCommand = new AttachmentCommand(INDEX_SECOND_TASK, delete);

        assertEquals(add.toString(), String.format("Add attachment at path %s", "helloworld.docx"));
        assertEquals(delete.toString(), String.format("Delete attachment with name %s", "delete please.docx"));
        assertEquals(list.toString(), "List attachments");
        assertEquals(get.toString(),
            String.format("Put attachment with name %s to %s", "helloworld.txt", "hello/world/helloworld.txt"));

        String expected = String.format("AttachmentCommand at index %d, with action: %s",
            INDEX_THIRD_TASK.getOneBased(), delete.toString());
        assertEquals(new AttachmentCommand(INDEX_THIRD_TASK, delete).toString(), expected);

    }

}
