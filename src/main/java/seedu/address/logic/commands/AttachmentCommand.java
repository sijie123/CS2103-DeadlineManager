package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILENAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILEPATH;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public static final String MESSAGE_USAGE =
        COMMAND_WORD + ": Modify and manages the attachments of the task identified "
            + "by the index number used in the displayed task list. "
            + "Parameters: INDEX (must be a positive integer) "
            + "[add|get|delete|list] "
            + "[" + PREFIX_FILEPATH + "FILEPATH] "
            + "[" + PREFIX_FILENAME + "FILENAME]\n"
            + "Example 1: " + COMMAND_WORD + " 1 " + COMMAND_ADD_ACTION + " "
            + PREFIX_FILEPATH + "D:\\Documents\\HelloWorld.docx\n"
            + "Example 2: " + COMMAND_WORD + " 2 " + COMMAND_GET_ACTION + " "
            + PREFIX_FILEPATH + "D:\\Documents\\TaskAttachments.zip\n"
            + "Example 3: " + COMMAND_WORD + " 1 " + COMMAND_LIST_ACTION + "\n"
            + "Example 4: " + COMMAND_WORD + " 1 " + COMMAND_DELETE_ACTION + " "
            + PREFIX_FILENAME + "HelloWorld.docx";


    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Task: %1$s";
    public static final String MESSAGE_MISSING_ARGUMENTS = "Missing argument %1$s for %2$s action";

    private final Index index;
    private final AttachmentAction attachmentAction;

    /**
     * @param index                of the task in the filtered task list to edit
     * @param attachmentAction action that is to be performed on the task
     */
    public AttachmentCommand(Index index, AttachmentAction attachmentAction) {
        requireNonNull(index);
        //requireNonNull(attachmentAction);

        this.index = index;
        this.attachmentAction = attachmentAction;
        //this.AttachmentAction = attachmentCommandDescriptor;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Task taskToEdit = lastShownList.get(index.getZeroBased());
        Task editedTask = attachmentAction.perform(taskToEdit);

        model.updatePerson(taskToEdit, editedTask);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.commitAddressBook();
        //return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, editedTask));
        return new CommandResult(attachmentAction.resultMessage());
    }

    /**
     * Creates and returns a {@code Task} with the details of {@code taskToEdit} edited with {@code
     * editPersonDescriptor}.
     */
    private static Task createEditedTask(Task taskToEdit,
                                         AttachmentAction attachmentAction) {
        assert taskToEdit != null;


        return new Task(taskToEdit.getName(), taskToEdit.getPhone(), taskToEdit.getEmail(), taskToEdit.getDeadline(),
            taskToEdit.getAddress(), taskToEdit.getTags(), taskToEdit.getAttachments());
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

    /**
     * Stores the actions to perform on a task's attachment with.
     */
    public abstract static class AttachmentAction {
        public static final String MESSAGE_ATTACHMENT_ACTION_COMPELTED = "Action Completed.";

        public abstract Task perform(Task taskToEdit) throws CommandException;

        public String resultMessage() {
            return MESSAGE_ATTACHMENT_ACTION_COMPELTED;
        }
    }


    /**
     * Action to add attachment
     */
    public static class AddAttachmentAction extends AttachmentAction {
        private final String filePath;
        public static final String MESSAGE_ADD_NOT_A_FILE = "%1$s is not a valid file.";
        public static final String MESSAGE_SUCCESS = "%1$s added as attachment.";
        private String resultMessage = "Action Not Performed";

        public AddAttachmentAction(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public Task perform(Task taskToEdit) throws CommandException {
            assert taskToEdit != null;
            HashSet<Attachment> updatedAttachments = new HashSet<>(taskToEdit.getAttachments());
            updatedAttachments.add(buildAttachment());
            return new Task(taskToEdit.getName(), taskToEdit.getPhone(), taskToEdit.getEmail(),
                taskToEdit.getDeadline(),
                taskToEdit.getAddress(), taskToEdit.getTags(), updatedAttachments);

        }

        private Attachment buildAttachment() throws CommandException {
            File file = new File(filePath);
            if (!file.exists() || !file.isFile()) {
                throw new CommandException(String.format(MESSAGE_ADD_NOT_A_FILE, filePath));
            }
            resultMessage = String.format(MESSAGE_SUCCESS, file.getName());
            return new Attachment(new File(filePath));
        }

        public String resultMessage() {
            return resultMessage;
        }
    }
}
