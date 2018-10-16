package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.GEORGE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.parser.SortCommandParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class SortCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_nameAscending_success() {
        SortCommand command = ensureParseSuccess("n<");
        command.execute(model, null);
        assertEquals(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE), model.getFilteredTaskList());
    }


    @Test
    public void execute_nameDescending_success() {
        SortCommand command = ensureParseSuccess("n>");
        command.execute(model, null);
        assertEquals(Arrays.asList(GEORGE, FIONA, ELLE, DANIEL, CARL, BENSON, ALICE), model.getFilteredTaskList());
    }

    @Test
    public void execute_dueAscendingNameAscending_success() {
        SortCommand command = ensureParseSuccess("d< n<");
        command.execute(model, null);
        assertEquals(Arrays.asList(ALICE, ELLE, FIONA, GEORGE, CARL, BENSON, DANIEL), model.getFilteredTaskList());
    }

    @Test
    public void execute_dueDescendingNameAscending_success() {
        SortCommand command = ensureParseSuccess("due>  n< ");
        command.execute(model, null);
        assertEquals(Arrays.asList(DANIEL, BENSON, CARL, ELLE, FIONA, GEORGE, ALICE), model.getFilteredTaskList());
    }

    @Test
    public void execute_priorityAscendingNameAscending_success() {
        SortCommand command = ensureParseSuccess("priority< n<");
        command.execute(model, null);
        assertEquals(Arrays.asList(DANIEL, CARL, GEORGE, BENSON, FIONA, ALICE, ELLE), model.getFilteredTaskList());
    }

    @Test
    public void execute_priorityDescendingNameAscending_success() {
        SortCommand command = ensureParseSuccess("p>  name<");
        command.execute(model, null);
        assertEquals(Arrays.asList(ALICE, ELLE, BENSON, FIONA, CARL, GEORGE, DANIEL), model.getFilteredTaskList());
    }

    @Test
    public void execute_tagAscendingNameDescending_success() {
        SortCommand command = ensureParseSuccess("tag<{owesMoney friend} n>");
        command.execute(model, null);
        assertEquals(Arrays.asList(BENSON, GEORGE, FIONA, ELLE, DANIEL, CARL, ALICE), model.getFilteredTaskList());
    }

    @Test
    public void execute_tagDescendingNameDescending_success() {
        SortCommand command = ensureParseSuccess("t>{owesMoney friend} n>");
        command.execute(model, null);
        assertEquals(Arrays.asList(BENSON, GEORGE, FIONA, ELLE, DANIEL, CARL, ALICE), model.getFilteredTaskList());
    }

    /**
     * Throws an assertion error if parsing fails, or else returns the successfully parsed SortCommand.
     *
     * @param comparator The string to parse as a comparator.
     * @return The sort command, on success.
     */
    private SortCommand ensureParseSuccess(String comparator) {
        try {
            return new SortCommandParser().parse(comparator);
        } catch (ParseException e) {
            throw new AssertionError("ParseException was thrown.", e);
        }
    }
}
