package systemtests;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.DEADLINE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DEADLINE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DEADLINE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PRIORITY_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PRIORITY_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PRIORITY_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRIORITY_BOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.task.Priority.NO_PRIORITY;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.HOON;
import static seedu.address.testutil.TypicalPersons.IDA;
import static seedu.address.testutil.TypicalPersons.KEYWORD_MATCHING_MEIER;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Name;
import seedu.address.model.task.Priority;
import seedu.address.model.task.Task;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddCommandSystemTest extends TaskCollectionSystemTest {

    @Test
    public void add() {
        Model model = getModel();

        /* ------------------------ Perform add operations on the shown unfiltered list ----------------------------- */

        /* Case: add a task without tags to a non-empty deadline manager,
         *    command with leading spaces and trailing spaces  -> added
         */
        Task toAdd = AMY;

        String command =
            "   " + AddCommand.COMMAND_WORD + "  " + NAME_DESC_AMY + " " + PRIORITY_DESC_AMY
                + " " + DEADLINE_DESC_AMY + "   " + TAG_DESC_FRIEND + " ";
        assertCommandSuccess(command, toAdd);

        /* Case: undo adding Amy to the list -> Amy deleted */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo adding Amy to the list -> Amy added again */
        command = RedoCommand.COMMAND_WORD;
        model.addTask(toAdd);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: add a task with all fields same as another task in the deadline manager except name -> added */
        toAdd = new PersonBuilder(AMY).withName(VALID_NAME_BOB).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_BOB + PRIORITY_DESC_AMY + DEADLINE_DESC_AMY
            + TAG_DESC_FRIEND;
        assertCommandSuccess(command, toAdd);

        /* Case: add a task with all fields same as another task in the deadline manager except priority
         * -> added
         */
        toAdd = new PersonBuilder(AMY).withPriority(VALID_PRIORITY_BOB)
            .build();
        command = PersonUtil.getAddCommand(toAdd);
        assertCommandSuccess(command, toAdd);

        /* Case: add to empty deadline manager -> added */
        deleteAllPersons();
        assertCommandSuccess(ALICE);

        /* Case: add a task with tags, command with parameters in random order -> added */
        toAdd = BOB;
        command = AddCommand.COMMAND_WORD + TAG_DESC_FRIEND + PRIORITY_DESC_BOB + DEADLINE_DESC_BOB
            + NAME_DESC_BOB + TAG_DESC_HUSBAND;
        assertCommandSuccess(command, toAdd);

        /* Case: add a task, missing tags -> added */
        assertCommandSuccess(HOON);

        /* -------------------------- Perform add operation on the shown filtered list ------------------------------ */

        /* Case: filters the task list before adding -> added */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        assertCommandSuccess(IDA);

        /* ------------------------ Perform add operation while a task card is selected --------------------------- */

        /* Case: selects first card in the task list, add a task -> added, card selection remains unchanged */
        selectPerson(Index.fromOneBased(1));
        assertCommandSuccess(CARL);

        /* ---------------------- Perform add operation with duplicate task (should succeed) ----------------------- */

        /* Case: add a duplicate task except with different priority -> added normally */
        toAdd = new PersonBuilder(HOON).withPriority(VALID_PRIORITY_BOB).build();
        command = PersonUtil.getAddCommand(toAdd);
        assertCommandSuccess(command, toAdd);

        /* Case: add a duplicate task except with different tags -> added normally */
        command = PersonUtil.getAddCommand(HOON) + " " + PREFIX_TAG.getPrefix() + "friends";
        toAdd = new PersonBuilder(HOON).withTags("friends").build();
        assertCommandSuccess(command, toAdd);

        /* ----------------------------------- Perform invalid add operations --------------------------------------- */

        /* Case: add a duplicate task -> rejected */
        command = PersonUtil.getAddCommand(HOON);
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_TASK);

        /* Case: missing name -> rejected */
        command = AddCommand.COMMAND_WORD + PRIORITY_DESC_AMY + DEADLINE_DESC_AMY;
        assertCommandFailure(command,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: missing deadline -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PRIORITY_DESC_AMY;
        assertCommandFailure(command,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: invalid keyword -> rejected */
        command = "adds " + PersonUtil.getPersonDetails(toAdd);
        assertCommandFailure(command, Messages.MESSAGE_UNKNOWN_COMMAND);

        /* Case: invalid name -> rejected */
        command = AddCommand.COMMAND_WORD + INVALID_NAME_DESC + PRIORITY_DESC_AMY + DEADLINE_DESC_AMY;
        assertCommandFailure(command, Name.MESSAGE_NAME_CONSTRAINTS);

        /* Case: invalid priority -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + INVALID_PRIORITY_DESC + DEADLINE_DESC_AMY;
        assertCommandFailure(command, Priority.MESSAGE_PRIORITY_CONSTRAINTS);

        /* Case: invalid deadline -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PRIORITY_DESC_AMY + INVALID_DEADLINE_DESC;
        assertCommandFailure(command, Deadline.MESSAGE_DEADLINE_CONSTRAINTS);

        /* Case: invalid tag -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PRIORITY_DESC_AMY + DEADLINE_DESC_AMY + INVALID_TAG_DESC;
        assertCommandFailure(command, Tag.MESSAGE_TAG_CONSTRAINTS);

        /* ------------------------- Perform add operations where optional fields are blank ------------------------- */

        /* Case: missing priority -> success */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + DEADLINE_DESC_AMY + TAG_DESC_FRIEND;
        toAdd = new PersonBuilder(AMY).withPriority(NO_PRIORITY).build();
        assertCommandSuccess(command, toAdd);

    }

    /**
     * Executes the {@code AddCommand} that adds {@code toAdd} to the model and asserts that
     * the,<br> 1. Command box displays an empty string.<br> 2. Command box has the default style
     * class.<br> 3. Result display box displays the success message of executing {@code AddCommand}
     * with the details of {@code toAdd}.<br> 4. {@code Storage} and {@code TaskListPanel} equal
     * to the corresponding components in the current model added with {@code toAdd}.<br> 5. Browser
     * url and selected card remain unchanged.<br> 6. Status bar's sync status changes.<br>
     * Verifications 1, 3 and 4 are performed by {@code TaskCollectionSystemTest#assertApplicationDisplaysExpected
     * (String,
     * String, Model)}.<br>
     *
     * @see TaskCollectionSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(Task toAdd) {
        Task toAddWithoutAttachment = new PersonBuilder(toAdd)
            .withAttachments()
            .build();
        assertCommandSuccess(PersonUtil.getAddCommand(toAddWithoutAttachment), toAddWithoutAttachment);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(Task)}. Executes {@code
     * command} instead.
     *
     * @see AddCommandSystemTest#assertCommandSuccess(Task)
     */
    private void assertCommandSuccess(String command, Task toAdd) {
        Model expectedModel = getModel();
        Task toAddWithoutAttachments = new PersonBuilder(toAdd)
            .withAttachments()
            .build();
        expectedModel.addTask(toAddWithoutAttachments);
        String expectedResultMessage = String.format(AddCommand.MESSAGE_SUCCESS, toAddWithoutAttachments);

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Task)} except asserts
     * that the,<br> 1. Result display box displays {@code expectedResultMessage}.<br> 2. {@code
     * Storage} and {@code TaskListPanel} equal to the corresponding components in {@code
     * expectedModel}.<br>
     *
     * @see AddCommandSystemTest#assertCommandSuccess(String, Task)
     */
    private void assertCommandSuccess(String command, Model expectedModel,
                                      String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and asserts that the,<br> 1. Command box displays {@code
     * command}.<br> 2. Command box has the error style class.<br> 3. Result display box displays
     * {@code expectedResultMessage}.<br> 4. {@code Storage} and {@code TaskListPanel} remain
     * unchanged.<br> 5. Browser url, selected card and status bar remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by {@code TaskCollectionSystemTest#assertApplicationDisplaysExpected
     * (String,
     * String, Model)}.<br>
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
