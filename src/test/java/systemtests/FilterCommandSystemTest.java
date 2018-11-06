package systemtests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static seedu.address.commons.core.Messages.MESSAGE_TASKS_LISTED_OVERVIEW;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.TypicalTasks.ALICE;
import static seedu.address.testutil.TypicalTasks.BENSON;
import static seedu.address.testutil.TypicalTasks.CARL;
import static seedu.address.testutil.TypicalTasks.DANIEL;
import static seedu.address.testutil.TypicalTasks.KEYWORD_MATCHING_MEIER;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.tag.Tag;

public class FilterCommandSystemTest extends TaskCollectionSystemTest {

    @Test
    public void find() {
        /* Case: find multiple tasks in deadline manager, command with leading spaces and trailing spaces
         * -> 2 tasks found
         */
        String command = "   " + FilterCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MEIER + "   ";
        Model expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, BENSON,
                DANIEL); // first names of Benson and Daniel are "Meier"
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: repeat previous find command where task list is displaying the tasks we are finding
         * -> 2 tasks found
         */
        command = FilterCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MEIER;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find task where task list is not displaying the task we are finding -> 1 task found */
        command = FilterCommand.COMMAND_WORD + " Carl";
        ModelHelper.setFilteredList(expectedModel, CARL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find tags of task in deadline manager -> 3 tasks found */
        List<Tag> tags = new ArrayList<>(DANIEL.getTags());
        command = FilterCommand.COMMAND_WORD + " " + tags.get(0).tagName;
        ModelHelper.setFilteredList(expectedModel, ALICE, BENSON, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find tasks matching both words */
        command = FilterCommand.COMMAND_WORD + " Benson Meier";
        ModelHelper.setFilteredList(expectedModel, BENSON);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find tasks matching both words */
        command = FilterCommand.COMMAND_WORD + " Meier Benson";
        ModelHelper.setFilteredList(expectedModel, BENSON);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: undo previous find command -> rejected */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: redo previous find command -> rejected */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: find same tasks in deadline manager after deleting 1 of them -> 1 task found */
        executeCommand(DeleteCommand.COMMAND_WORD + " 1");
        assertFalse(getModel().getTaskCollection().getTaskList().contains(BENSON));
        command = FilterCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MEIER;
        expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find task in deadline manager, keyword is same as name but of different case -> 1 task found */
        command = FilterCommand.COMMAND_WORD + " MeIeR";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find task in deadline manager, keyword is substring of name -> 1 task found */
        command = FilterCommand.COMMAND_WORD + " Mei";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find task in deadline manager, name is substring of keyword -> 0 tasks found */
        command = FilterCommand.COMMAND_WORD + " Meiers";
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find task not in deadline manager -> 0 tasks found */
        command = FilterCommand.COMMAND_WORD + " Mark";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find while a task is selected -> selected card deselected */
        showAllTasks();
        selectTask(Index.fromOneBased(1));
        assertNotEquals(getPersonListPanel().getHandleToSelectedCard().getName(), DANIEL.getName().value);
        command = FilterCommand.COMMAND_WORD + " Daniel";
        ModelHelper.setFilteredList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardDeselected();

        /* Case: find task in empty deadline manager -> 0 tasks found */
        deleteAllTests();
        command = FilterCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MEIER;
        expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: mixed case command word -> rejected */
        command = "FiLter Meier";
        assertCommandFailure(command, MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the
     * result display box displays {@code Messages#MESSAGE_TASKS_LISTED_OVERVIEW} with the number
     * of people in the filtered list, and the model related components equal to {@code
     * expectedModel}. These verifications are done by {@code TaskCollectionSystemTest#assertApplicationDisplaysExpected
     * (String,
     * String, Model)}.<br> Also verifies that the status bar remains unchanged, and the command box
     * has the default style class, and the selected card updated accordingly, depending on {@code
     * cardStatus}.
     *
     * @see TaskCollectionSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel) {
        String expectedResultMessage = String.format(
                MESSAGE_TASKS_LISTED_OVERVIEW, expectedModel.getFilteredTaskList().size());

        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the
     * result display box displays {@code expectedResultMessage} and the model related components
     * equal to the current model. These verifications are done by {@code
     * TaskCollectionSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br> Also
     * verifies that the browser url, selected card and status bar remain unchanged, and the command
     * box has the error style.
     *
     * @see TaskCollectionSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
