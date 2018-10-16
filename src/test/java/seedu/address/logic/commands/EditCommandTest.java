package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_TASK;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.EditCommand.EditTaskDescriptor;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.TaskCollection;
import seedu.address.model.UserPrefs;
import seedu.address.model.task.Task;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit
 * tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        // Preserve attachments as editedTask should have the same attachment
        Task editedTask = new PersonBuilder()
            .withAttachments(model.getFilteredTaskList().get(0).getAttachments())
            .build();

        EditTaskDescriptor descriptor = new EditPersonDescriptorBuilder(editedTask).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_TASK, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_TASK_SUCCESS, editedTask);

        Model expectedModel = new ModelManager(new TaskCollection(model.getTaskCollection()),
            new UserPrefs());
        expectedModel.updateTask(model.getFilteredTaskList().get(0), editedTask);
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredTaskList().size());
        Task lastTask = model.getFilteredTaskList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastTask);
        Task editedTask = personInList.withName(VALID_NAME_BOB)
            .withTags(VALID_TAG_HUSBAND).build();

        EditCommand.EditTaskDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
            .withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_TASK_SUCCESS, editedTask);

        Model expectedModel = new ModelManager(new TaskCollection(model.getTaskCollection()),
            new UserPrefs());
        expectedModel.updateTask(lastTask, editedTask);
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_TASK, new EditCommand.EditTaskDescriptor());
        Task editedTask = model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_TASK_SUCCESS, editedTask);

        Model expectedModel = new ModelManager(new TaskCollection(model.getTaskCollection()),
            new UserPrefs());
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_TASK);

        Task taskInFilteredList = model.getFilteredTaskList()
            .get(INDEX_FIRST_TASK.getZeroBased());
        Task editedTask = new PersonBuilder(taskInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_TASK,
            new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_TASK_SUCCESS, editedTask);

        Model expectedModel = new ModelManager(new TaskCollection(model.getTaskCollection()),
            new UserPrefs());
        expectedModel.updateTask(model.getFilteredTaskList().get(0), editedTask);
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_success() {
        Task firstTask = model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        EditCommand.EditTaskDescriptor descriptor = new EditPersonDescriptorBuilder(firstTask).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_TASK, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_TASK_SUCCESS,
            model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased()));

        Model expectedModel = new ModelManager(new TaskCollection(model.getTaskCollection()),
            new UserPrefs());
        expectedModel.updateTask(model.getFilteredTaskList().get(1), firstTask);
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory,
            expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTaskList().size() + 1);
        EditTaskDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
            .build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, commandHistory,
            Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list, but smaller than size of
     * deadline manager
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_TASK);
        Index outOfBoundIndex = INDEX_SECOND_TASK;
        // ensures that outOfBoundIndex is still in bounds of deadline manager list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getTaskCollection().getTaskList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
            new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, commandHistory,
            Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Task taskToEdit = model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        Task editedTask = new PersonBuilder()
            .withAttachments(taskToEdit.getAttachments())
            .build();
        EditTaskDescriptor descriptor = new EditPersonDescriptorBuilder(editedTask).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_TASK, descriptor);
        Model expectedModel = new ModelManager(new TaskCollection(model.getTaskCollection()),
            new UserPrefs());
        expectedModel.updateTask(taskToEdit, editedTask);
        expectedModel.commitAddressBook();

        // edit -> first task edited
        editCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered task list to show all persons
        expectedModel.undoAddressBook();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS,
            expectedModel);

        // redo -> same first task edited again
        expectedModel.redoAddressBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS,
            expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTaskList().size() + 1);
        EditTaskDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
            .build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        // execution failed -> deadline manager state not added into model
        assertCommandFailure(editCommand, model, commandHistory,
            Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        // single deadline manager state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Edits a {@code Task} from a filtered list. 2. Undo the edit. 3. The unfiltered list should
     * be shown now. Verify that the index of the previously edited task in the unfiltered list is
     * different from the index at the filtered list. 4. Redo the edit. This ensures {@code
     * RedoCommand} edits the task object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_samePersonEdited() throws Exception {
        showPersonAtIndex(model, INDEX_SECOND_TASK);
        Task taskToEdit = model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        Task editedTask = new PersonBuilder()
            .withAttachments(taskToEdit.getAttachments())
            .build();
        EditCommand.EditTaskDescriptor descriptor = new EditPersonDescriptorBuilder(editedTask).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_TASK, descriptor);
        Model expectedModel = new ModelManager(new TaskCollection(model.getTaskCollection()),
            new UserPrefs());

        expectedModel.updateTask(taskToEdit, editedTask);
        expectedModel.commitAddressBook();

        // edit -> edits second task in unfiltered task list / first task in filtered task list
        editCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered task list to show all persons
        expectedModel.undoAddressBook();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS,
            expectedModel);

        assertNotEquals(model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased()),
            taskToEdit);
        // redo -> edits same second task in unfiltered task list
        expectedModel.redoAddressBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS,
            expectedModel);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_TASK, DESC_AMY);

        // same values -> returns true
        EditCommand.EditTaskDescriptor copyDescriptor = new EditCommand.EditTaskDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_TASK, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_TASK, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_TASK, DESC_BOB)));
    }

}
