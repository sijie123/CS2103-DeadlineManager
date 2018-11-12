package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILENAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILEPATH;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_TASKS;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.attachment.Attachment;
import seedu.address.model.task.Task;

/**
 * Edits the details of an existing task in the deadline manager.
 */
public class AttachmentCommand extends Command {
    public static final String COMMAND_WORD = "attachment";
    public static final String COMMAND_ADD_ACTION = "add";
    public static final String COMMAND_GET_ACTION = "get";
    public static final String COMMAND_DELETE_ACTION = "delete";
    public static final String COMMAND_LIST_ACTION = "list";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Modify and manages the attachments of the task identified "
            + "by the index number used in the displayed task list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[add|get|delete|list] "
            + "[" + PREFIX_FILEPATH + "\"FILEPATH\"] "
            + "[" + PREFIX_FILENAME + "\"FILENAME\"]\n"
            + "It is recommended for FILEPATH and FILENAME to be enclosed in quotes (\"\")."
            + "Otherwise, the application might fail to interpret your command.\n"
            + "Example 1: " + COMMAND_WORD + " 1 " + COMMAND_ADD_ACTION + " "
            + PREFIX_FILEPATH + "\"D:\\Documents\\HelloWorld.docx\"\n"
            + "Example 2: " + COMMAND_WORD + " 2 " + COMMAND_GET_ACTION + " "
            + PREFIX_FILEPATH + "\"D:\\Documents\\TaskAttachments.zip\"\n"
            + "Example 3: " + COMMAND_WORD + " 1 " + COMMAND_LIST_ACTION + "\n"
            + "Example 4: " + COMMAND_WORD + " 1 " + COMMAND_DELETE_ACTION + " "
            + PREFIX_FILENAME + "\"HelloWorld.docx\"";


    public static final String MESSAGE_MISSING_ARGUMENTS = "Missing argument %1$s for %2$s action";
    public static final String MESSAGE_NAME_NOT_FOUND = "%1$s is not an attachment.";

    private static final Logger logger = LogsCenter.getLogger(Attachment.class);
    private final Index index;
    private final AttachmentAction attachmentAction;

    /**
     * @param index            of the task in the filtered task list to edit
     * @param attachmentAction action that is to be performed on the task
     */
    public AttachmentCommand(Index index, AttachmentAction attachmentAction) {
        requireAllNonNull(index, attachmentAction);
        this.index = index;
        this.attachmentAction = attachmentAction;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredTaskList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        Task taskToEdit = lastShownList.get(index.getZeroBased());
        ActionResult result = attachmentAction.perform(taskToEdit);
        Task editedTask = result.getTask();

        updateModel(model, taskToEdit, editedTask);

        return new CommandResult(result.getMessage());
    }

    /**
     * Updates the model to reflect changes in the Task, commits a version history so it can be undone
     *
     * @param model        Current model in use
     * @param originalTask Original Task to be replaced
     * @param updatedTask  Updated Task to replace the original task
     */
    private void updateModel(Model model, Task originalTask, Task updatedTask) {
        requireAllNonNull(model, originalTask, updatedTask);
        if (originalTask.equals(updatedTask)) {
            return;
        }
        model.updateTask(originalTask, updatedTask);
        model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        model.commitTaskCollection();
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AttachmentCommand)) {
            return false;
        }

        // state check
        AttachmentCommand e = (AttachmentCommand) other;
        return index.equals(e.index)
                && attachmentAction.equals(e.attachmentAction);
    }

    @Override
    public String toString() {
        return String.format("AttachmentCommand at index %d, with action: %s",
                index.getOneBased(), attachmentAction.toString());
    }

    /**
     * Checks and throws (@code CommandException) if the attachment is not readable.
     *
     * @param attachment Attachment to check
     * @param format     Format string to format the (@code CommandException) with
     * @throws CommandException
     */
    private static void checkAttachmentReadability(Attachment attachment, String format) throws CommandException {
        requireAllNonNull(attachment, format);
        if (!attachment.isReadable()) {
            logger.info(String.format("Attachment %s is not readable. Checked %s.",
                    attachment.getName(), attachment.file.getAbsolutePath()));
            throw new CommandException(String.format(format, attachment.file));
        }
    }

    /**
     * Utility function to obtain an attachment object from a set of attachments, based on name.
     *
     * @param attachments Set of attachments
     * @param name        Name to search
     * @return Null if there is no attachment with the provided name in the set.
     */
    private static Attachment getAttachment(Set<Attachment> attachments, String name) {
        requireAllNonNull(attachments, name);
        Map<String, Attachment> attachmentNameMap = attachments.stream()
                .collect(Collectors.toMap(x -> x.getName(), x -> x));
        return attachmentNameMap.get(name);
    }

    /**
     * Utility function to determine if an attachment with name (@code name) exists in the set of attachments.
     *
     * @param attachments Set of attachments
     * @param name        Name to search
     * @return True if there is an attachment with name (@code name)
     */
    private static boolean isAttachmentName(Set<Attachment> attachments, String name) {
        requireAllNonNull(attachments, name);
        Set<String> attachmentNames = attachments
                .stream()
                .map(x -> x.getName())
                .collect(Collectors.toSet());
        return attachmentNames.contains(name);
    }

    /**
     * Utility function to check whether a name can be added to the set of attachments of a task.
     * Throws a (@code CommandException) is provided name is already in the set of attachments.
     *
     * @param task task to check from
     * @param name name to be checked
     * @throws CommandException
     */
    private static void checkAttachmentNameUnique(Task task, String name) throws CommandException {
        requireAllNonNull(task, name);
        if (isAttachmentName(task.getAttachments(), name)) {
            logger.info(String.format("Task already contains an attachment with filename %s.",
                    name));
            throw new CommandException(Attachment.MESSAGE_DUPLICATE_ATTACHMENT_NAME);
        }
    }

    /**
     * Utility function to check whether a name already exists in the set of attachments of a task.
     * Throws a (@code CommandException) is provided name is not in the set of attachments.
     *
     * @param task task to check from
     * @param name name to be checked
     * @throws CommandException
     */
    private static void checkAttachmentNameExists(Task task, String name) throws CommandException {
        requireAllNonNull(task, name);
        if (!isAttachmentName(task.getAttachments(), name)) {
            logger.info(String.format("Task does not contains an attachment with filename %s.",
                    name));
            throw new CommandException(String.format(MESSAGE_NAME_NOT_FOUND, name));
        }
    }

    /**
     * Used to represent the information returned by the various AttachmentActions. Contains the updated task and
     * result messages (if any).
     */
    private static class ActionResult {
        public final String resultMessage;
        public final Task updatedTask;


        public ActionResult(Task updatedTask, String resultMessage) {
            requireAllNonNull(updatedTask, resultMessage);
            this.updatedTask = updatedTask;
            this.resultMessage = resultMessage;
        }

        /**
         * Returns the task
         *
         * @return stored task
         */
        public Task getTask() {
            return updatedTask;
        }

        /**
         * Returns the result message
         *
         * @return stored result message
         */
        public String getMessage() {
            return resultMessage;
        }
    }

    /**
     * Stores the actions to perform on a task's attachment with.
     */
    public interface AttachmentAction {

        ActionResult perform(Task taskToEdit) throws CommandException;

    }


    /**
     * Action to add attachment
     */
    public static class AddAttachmentAction implements AttachmentAction {
        public static final String MESSAGE_ADD_NOT_A_FILE = "%1$s is not a valid file.";
        public static final String MESSAGE_SUCCESS = "%1$s added as attachment.";

        private final String filePath;

        public AddAttachmentAction(String filePath) {
            requireAllNonNull(filePath);
            this.filePath = filePath;
        }


        @Override
        public ActionResult perform(Task taskToEdit) throws CommandException {
            requireNonNull(taskToEdit);
            Attachment newAttachment = buildAttachment();
            checkAttachmentNameUnique(taskToEdit, newAttachment.getName());
            HashSet<Attachment> updatedAttachments = new HashSet<>(taskToEdit.getAttachments());
            updatedAttachments.add(newAttachment);
            Task updatedTask = new Task(taskToEdit.getName(), taskToEdit.getPriority(), taskToEdit.getFrequency(),
                    taskToEdit.getDeadline(), taskToEdit.getTags(), updatedAttachments);
            String resultMessage = String.format(MESSAGE_SUCCESS, newAttachment.getName());
            return new ActionResult(updatedTask, resultMessage);
        }

        /**
         * Generates the Attachment Object from the file path.
         * Also performs checks to see if file exists.
         *
         * @return Attachment that is generated
         * @throws CommandException
         */
        private Attachment buildAttachment() throws CommandException {
            File file = new File(filePath);
            Attachment attachment = new Attachment(new File(filePath));
            checkAttachmentReadability(attachment, MESSAGE_ADD_NOT_A_FILE);
            return attachment;
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof AddAttachmentAction)) {
                return false;
            }

            // state check
            AddAttachmentAction e = (AddAttachmentAction) other;
            return filePath.equals(e.filePath);
        }

        @Override
        public String toString() {
            return String.format("Add attachment at path %s", filePath);
        }
    }

    /**
     * Action to list all attachments.
     */
    public static class ListAttachmentAction implements AttachmentAction {

        public static final String MESSAGE_TOTAL_ATTACHMENTS = "%d attachment(s) in total.\n";
        public static final String MESSAGE_LIST_ATTACHMENT_DETAILS = "%d) %s\n";

        @Override
        public ActionResult perform(Task taskToEdit) throws CommandException {
            requireNonNull(taskToEdit);
            Set<Attachment> attachments = taskToEdit.getAttachments();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(String.format(MESSAGE_TOTAL_ATTACHMENTS, attachments.size()));
            int indexCounter = 0;
            for (Attachment attachment : attachments) {
                indexCounter++;
                stringBuilder.append(
                        String.format(MESSAGE_LIST_ATTACHMENT_DETAILS, indexCounter, attachment.toString()));
            }
            return new ActionResult(taskToEdit, stringBuilder.toString());
        }


        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof ListAttachmentAction)) {
                return false;
            }

            // state check
            return true;
        }

        @Override
        public String toString() {
            return "List attachments";
        }
    }

    /**
     * Action to delete all attachment.
     */
    public static class DeleteAttachmentAction implements AttachmentAction {
        public static final String MESSAGE_SUCCESS = "%1$s deleted from attachments.";

        private final String nameToDelete;

        public DeleteAttachmentAction(String nameToDelete) {
            requireAllNonNull(nameToDelete);
            this.nameToDelete = nameToDelete;
        }

        @Override
        public ActionResult perform(Task taskToEdit) throws CommandException {
            requireNonNull(taskToEdit);
            checkAttachmentNameExists(taskToEdit, nameToDelete);
            Attachment attachmentToDelete = getAttachment(taskToEdit.getAttachments(), nameToDelete);
            HashSet<Attachment> updatedAttachments = new HashSet<>(taskToEdit.getAttachments());
            updatedAttachments.remove(attachmentToDelete);
            String resultMessage = String.format(MESSAGE_SUCCESS, nameToDelete);
            Task updatedTask = new Task(taskToEdit.getName(), taskToEdit.getPriority(), taskToEdit.getFrequency(),
                    taskToEdit.getDeadline(), taskToEdit.getTags(), updatedAttachments);
            return new ActionResult(updatedTask, resultMessage);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof DeleteAttachmentAction)) {
                return false;
            }

            // state check
            DeleteAttachmentAction e = (DeleteAttachmentAction) other;
            return nameToDelete.equals(e.nameToDelete);
        }

        @Override
        public String toString() {
            return String.format("Delete attachment with name %s", nameToDelete);
        }


    }

    /**
     * Action to get/retrieve attachment.
     */
    public static class GetAttachmentAction implements AttachmentAction {
        public static final String MESSAGE_GET_FAILED = "Failed to save to %1$s. "
                + "Do note that FILEPATH should be a file to a path, not a directory/folder.";
        public static final String MESSAGE_SUCCESS = "%1$s is now saved to %2$s.";
        public static final String MESSAGE_GET_NOT_A_FILE = "%1$s is not a valid file. "
                + "It might have been deleted, moved or Deadline Manager does not have permissions to read from it.";

        private final String fileName;
        private final String savePath;

        public GetAttachmentAction(String fileName, String savePath) {
            requireAllNonNull(fileName, savePath);
            this.fileName = fileName;
            this.savePath = savePath;
        }

        /**
         * Saves the attachment to savePath, overwriting the destination if a file exists.
         *
         * @param attachment attachment to save
         * @param savePath   path to save the attachment to
         */
        private File copyFileToDestination(Attachment attachment, String savePath) throws CommandException {
            requireAllNonNull(attachment, savePath);
            checkAttachmentReadability(attachment, MESSAGE_GET_NOT_A_FILE);
            try {
                return attachment.saveTo(savePath);
            } catch (IOException ioe) {
                logger.severe(String.format("Attachment copy from %s to %s failed due to: %s",
                        attachment.file.getAbsolutePath(), savePath, ioe));
                throw new CommandException(String.format(MESSAGE_GET_FAILED, savePath));
            }
        }

        @Override
        public ActionResult perform(Task taskToEdit) throws CommandException {
            requireNonNull(taskToEdit);
            checkAttachmentNameExists(taskToEdit, fileName);
            Attachment attachmentToGet = getAttachment(taskToEdit.getAttachments(), fileName);
            copyFileToDestination(attachmentToGet, savePath);
            String resultMessage = String.format(MESSAGE_SUCCESS, attachmentToGet.getName(), savePath);
            return new ActionResult(taskToEdit, resultMessage);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof GetAttachmentAction)) {
                return false;
            }

            // state check
            GetAttachmentAction e = (GetAttachmentAction) other;
            return fileName.equals(e.fileName)
                    && savePath.equals(e.savePath);
        }

        @Override
        public String toString() {
            return String.format("Put attachment with name %s to %s", fileName, savePath);
        }
    }
}
