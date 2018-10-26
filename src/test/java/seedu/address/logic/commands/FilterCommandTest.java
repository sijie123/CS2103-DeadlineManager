package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.TypicalTasks.ALICE;
import static seedu.address.testutil.TypicalTasks.BENSON;
import static seedu.address.testutil.TypicalTasks.CARL;
import static seedu.address.testutil.TypicalTasks.DANIEL;
import static seedu.address.testutil.TypicalTasks.ELLE;
import static seedu.address.testutil.TypicalTasks.FIONA;
import static seedu.address.testutil.TypicalTasks.GEORGE;
import static seedu.address.testutil.TypicalTasks.getTypicalTaskCollections;

import java.util.Arrays;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.parser.FilterCommandParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FilterCommandTest {

    private Model model = new ModelManager(getTypicalTaskCollections(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalTaskCollections(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_nameSubstring_success() {
        FilterCommand command = ensureParseSuccess("n>Meier");
        command.execute(model, null);
        assertEquals(Arrays.asList(BENSON, DANIEL), model.getFilteredTaskList());
    }

    @Test
    public void execute_nameSuperstring_success() {
        FilterCommand command = ensureParseSuccess("n<'Carl Kurz Test'");
        command.execute(model, null);
        assertEquals(Arrays.asList(CARL), model.getFilteredTaskList());
    }

    @Test
    public void execute_nameQuotedDouble_success() {
        FilterCommand command = ensureParseSuccess("n>\"Carl K\"");
        command.execute(model, null);
        assertEquals(Arrays.asList(CARL), model.getFilteredTaskList());
    }

    @Test
    public void execute_nameQuotedSingle_success() {
        FilterCommand command = ensureParseSuccess("n>\'Carl K\'");
        command.execute(model, null);
        assertEquals(Arrays.asList(CARL), model.getFilteredTaskList());
    }

    @Test
    public void execute_nameConvenience_success() {
        FilterCommand command = ensureParseSuccess("n:Meier");
        command.execute(model, null);
        assertEquals(Arrays.asList(BENSON, DANIEL), model.getFilteredTaskList());
    }

    @Test
    public void execute_nameExact_success() {
        FilterCommand command = ensureParseSuccess("n:'Benson Meier'");
        command.execute(model, null);
        assertEquals(Arrays.asList(BENSON), model.getFilteredTaskList());
    }

    @Test
    public void execute_nameLongform_success() {
        FilterCommand command = ensureParseSuccess("name:'Benson Meier'");
        command.execute(model, null);
        assertEquals(Arrays.asList(BENSON), model.getFilteredTaskList());
    }

    @Test
    public void execute_nameSpaced_success() {
        FilterCommand command;

        command = ensureParseSuccess("n: Meier");
        command.execute(model, null);
        assertEquals(Arrays.asList(BENSON, DANIEL), model.getFilteredTaskList());

        command = ensureParseSuccess("n : Meier");
        command.execute(model, null);
        assertEquals(Arrays.asList(BENSON, DANIEL), model.getFilteredTaskList());

        command = ensureParseSuccess("n :Meier");
        command.execute(model, null);
        assertEquals(Arrays.asList(BENSON, DANIEL), model.getFilteredTaskList());

        command = ensureParseSuccess("n  :   Meier");
        command.execute(model, null);
        assertEquals(Arrays.asList(BENSON, DANIEL), model.getFilteredTaskList());
    }

    @Test
    public void execute_dueExact_success() {
        FilterCommand command = ensureParseSuccess("d=1/10/2018");
        command.execute(model, null);
        assertEquals(Arrays.asList(ALICE), model.getFilteredTaskList());
    }

    @Test
    public void execute_dueEarlier_success() {
        FilterCommand command = ensureParseSuccess("d<2/10/2018");
        command.execute(model, null);
        assertEquals(Arrays.asList(ALICE, ELLE, FIONA, GEORGE), model.getFilteredTaskList());
    }

    @Test
    public void execute_dueLater_success() {
        FilterCommand command = ensureParseSuccess("d>1/11/2018");
        command.execute(model, null);
        assertEquals(Arrays.asList(BENSON, DANIEL), model.getFilteredTaskList());
    }

    @Test
    public void execute_dueConvenience_success() {
        FilterCommand command = ensureParseSuccess("d:2/10/2018");
        command.execute(model, null);
        assertEquals(Arrays.asList(ALICE, ELLE, FIONA, GEORGE), model.getFilteredTaskList());
    }

    @Test
    public void execute_dueLongform_success() {
        FilterCommand command = ensureParseSuccess("due:2/10/2018");
        command.execute(model, null);
        assertEquals(Arrays.asList(ALICE, ELLE, FIONA, GEORGE), model.getFilteredTaskList());
    }

    @Test
    public void execute_dueSpaced_success() {
        FilterCommand command;

        command = ensureParseSuccess("due: 2/10/2018");
        command.execute(model, null);
        assertEquals(Arrays.asList(ALICE, ELLE, FIONA, GEORGE), model.getFilteredTaskList());

        command = ensureParseSuccess("due : 2/10/2018");
        command.execute(model, null);
        assertEquals(Arrays.asList(ALICE, ELLE, FIONA, GEORGE), model.getFilteredTaskList());

        command = ensureParseSuccess("due :2/10/2018");
        command.execute(model, null);
        assertEquals(Arrays.asList(ALICE, ELLE, FIONA, GEORGE), model.getFilteredTaskList());

        command = ensureParseSuccess("due  :   2/10/2018");
        command.execute(model, null);
        assertEquals(Arrays.asList(ALICE, ELLE, FIONA, GEORGE), model.getFilteredTaskList());
    }

    @Test
    public void execute_andOperator_success() {
        FilterCommand command;

        command = ensureParseSuccess("due: 2/10/2018 && n:e");
        command.execute(model, null);
        assertEquals(Arrays.asList(ALICE, ELLE, GEORGE), model.getFilteredTaskList());

        command = ensureParseSuccess("due: 2/10/2018 && n:benson");
        command.execute(model, null);
        assertEquals(Arrays.asList(), model.getFilteredTaskList());
    }

    @Test
    public void execute_orOperator_success() {
        FilterCommand command;

        command = ensureParseSuccess("due: 2/10/2018 || n:e");
        command.execute(model, null);
        assertEquals(Arrays.asList(ALICE, BENSON, DANIEL, ELLE, FIONA, GEORGE), model.getFilteredTaskList());
    }

    @Test
    public void execute_notOperator_success() {
        FilterCommand command;

        command = ensureParseSuccess("due: 2/10/2018 || !n:e");
        command.execute(model, null);
        assertEquals(Arrays.asList(ALICE, CARL, ELLE, FIONA, GEORGE), model.getFilteredTaskList());
    }

    /**
     * Throws an assertion error if parsing fails, or else returns the successfully parsed FilterCommand.
     *
     * @param predicate The string to parse as a predicate.
     * @return The filter command, on success.
     */
    private FilterCommand ensureParseSuccess(String predicate) {
        try {
            return new FilterCommandParser().parse(predicate);
        } catch (ParseException e) {
            throw new AssertionError("ParseException was thrown.", e);
        }
    }
}
