package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.TaskCollection;
import seedu.address.model.UserPrefs;
import seedu.address.model.task.Task;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.TaskBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit
 * tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Task editedTask = new TaskBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedTask).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedTask);

        Model expectedModel = new ModelManager(new TaskCollection(model.getAddressBook()),
            new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), editedTask);
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Task lastTask = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        TaskBuilder personInList = new TaskBuilder(lastTask);
        Task editedTask = personInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withTags(VALID_TAG_HUSBAND).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
            .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedTask);

        Model expectedModel = new ModelManager(new TaskCollection(model.getAddressBook()),
            new UserPrefs());
        expectedModel.updatePerson(lastTask, editedTask);
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, new EditPersonDescriptor());
        Task editedTask = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedTask);

        Model expectedModel = new ModelManager(new TaskCollection(model.getAddressBook()),
            new UserPrefs());
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Task taskInFilteredList = model.getFilteredPersonList()
            .get(INDEX_FIRST_PERSON.getZeroBased());
        Task editedTask = new TaskBuilder(taskInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
            new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedTask);

        Model expectedModel = new ModelManager(new TaskCollection(model.getAddressBook()),
            new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), editedTask);
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_success() {
        Task firstTask = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstTask).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()));

        Model expectedModel = new ModelManager(new TaskCollection(model.getAddressBook()),
                new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(1), firstTask);
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory,
                expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
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
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of deadline manager list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getTaskList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
            new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, commandHistory,
            Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Task editedTask = new TaskBuilder().build();
        Task taskToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedTask).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);
        Model expectedModel = new ModelManager(new TaskCollection(model.getAddressBook()),
            new UserPrefs());
        expectedModel.updatePerson(taskToEdit, editedTask);
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
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
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
        Task editedTask = new TaskBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedTask).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);
        Model expectedModel = new ModelManager(new TaskCollection(model.getAddressBook()),
            new UserPrefs());

        showPersonAtIndex(model, INDEX_SECOND_PERSON);
        Task taskToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        expectedModel.updatePerson(taskToEdit, editedTask);
        expectedModel.commitAddressBook();

        // edit -> edits second task in unfiltered task list / first task in filtered task list
        editCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered task list to show all persons
        expectedModel.undoAddressBook();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS,
            expectedModel);

        assertNotEquals(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()),
            taskToEdit);
        // redo -> edits same second task in unfiltered task list
        expectedModel.redoAddressBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS,
            expectedModel);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_PERSON, DESC_BOB)));
    }

}
