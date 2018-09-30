package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILENAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILEPATH;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.task.Task;

/**
 * Edits the details of an existing task in the deadline manager.
 */
public class AttachmentCommand extends Command {

    public static final String COMMAND_WORD = "attachment";

    public static final String MESSAGE_USAGE =
        COMMAND_WORD + ": Modify and manages the attachments of the task identified "
            + "by the index number used in the displayed task list. "
            + "Parameters: INDEX (must be a positive integer) "
            + "[add|get|delete|list] "
            + "[" + PREFIX_FILEPATH + "FILEPATH] "
            + "[" + PREFIX_FILENAME + "FILENAME]\n"
            + "Example 1: " + COMMAND_WORD + " 1 add "
            + PREFIX_FILEPATH + "D:\\Documents\\HelloWorld.docx\n"
            + "Example 2: " + COMMAND_WORD + " 2 get"
            + PREFIX_FILEPATH + "D:\\Documents\\TaskAttachments.zip\n"
            + "Example 3: " + COMMAND_WORD + " 1 list\n"
            + "Example 4: " + COMMAND_WORD + " 1 delete"
            + PREFIX_FILENAME + "HelloWorld.docx";


    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Task: %1$s";


    private final Index index;
    private final AttachmentAction attachmentAction;

    /**
     * @param index                of the task in the filtered task list to edit
     * @param editPersonDescriptor details to edit the task with
     */
    public AttachmentCommand(Index index, AttachmentAction attachmentAction) {
        requireNonNull(index);
        //requireNonNull(attachmentAction);

        this.index = index;
        this.attachmentAction = null;
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
        Task editedTask = createEditedTask(taskToEdit, attachmentAction);

        model.updatePerson(taskToEdit, editedTask);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.commitAddressBook();
        //return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, editedTask));
        return new CommandResult("TO BE COMPLETED, NOTHING CHANGED");
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
     * Stores the actions to oerform on a task's attachment with.
     */
    public static class AttachmentAction {

    }
}
