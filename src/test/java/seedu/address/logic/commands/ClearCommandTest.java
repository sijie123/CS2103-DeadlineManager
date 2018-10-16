package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalTasks.getTypicalTaskCollections;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.TaskCollection;
import seedu.address.model.UserPrefs;

public class ClearCommandTest {

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_emptyAddressBook_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();
        expectedModel.commitTaskCollection();

        assertCommandSuccess(new ClearCommand(), model, commandHistory,
            ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyAddressBook_success() {
        Model model = new ModelManager(getTypicalTaskCollections(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalTaskCollections(), new UserPrefs());
        expectedModel.resetData(new TaskCollection());
        expectedModel.commitTaskCollection();

        assertCommandSuccess(new ClearCommand(), model, commandHistory,
            ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
