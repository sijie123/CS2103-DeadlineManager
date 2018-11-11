package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalTasks.getTypicalTaskCollections;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.task.Task;
import seedu.address.testutil.TaskBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalTaskCollections(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() {
        Task validTask = new TaskBuilder().build();

        Model expectedModel = new ModelManager(model.getTaskCollection(), new UserPrefs());
        expectedModel.addTask(validTask);
        expectedModel.commitTaskCollection();

        assertCommandSuccess(new AddCommand(validTask), model, commandHistory,
                String.format(AddCommand.MESSAGE_SUCCESS, validTask), expectedModel);
    }

    @Test
    public void execute_duplicatePerson_success() {
        Task taskInList = model.getTaskCollection().getTaskList().get(0);

        Model expectedModel = new ModelManager(model.getTaskCollection(), new UserPrefs());
        expectedModel.addTask(taskInList);
        expectedModel.commitTaskCollection();

        assertCommandSuccess(new AddCommand(taskInList), model, commandHistory,
                String.format(AddCommand.MESSAGE_SUCCESS, taskInList), expectedModel);
    }

}
