package seedu.address.logic.commands;

import static org.junit.Assert.assertNotNull;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.storage.StorageManager;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ExportCommandTest {

    public final String temporaryFilePath = "dummySaveFile";
    private Model model;
    private Model expectedModel;
    private CommandHistory commandHistory = new CommandHistory();
    private Path defaultPath = Paths.get("fakeDefaultPath");
    private boolean shouldDeleteDefaultPath = false;

    @Before
    public void setUp() throws IOException {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        if (Files.notExists(defaultPath)) {
            Files.createFile(defaultPath);
            shouldDeleteDefaultPath = true;
        }
    }

    @Test
    public void execute_exportOnWorkingFile_exceptionThrown() {
        assertCommandFailure(new ExportCommand(defaultPath.toString()), model, commandHistory,
                String.format(ExportCommand.MESSAGE_EXPORT_ERROR, StorageManager.MESSAGE_WRITE_FILE_EXISTS_ERROR));
    }

    @Test
    public void execute_exportNewFile_exportSuccessful() {
        assertCommandSuccess(new ExportCommand(temporaryFilePath), model,
                commandHistory, ExportCommand.MESSAGE_SUCCESS, model);
        Path exportPath = Paths.get(temporaryFilePath);
        assertDataCorrect(exportPath);
    }

    /**
     * Checks that a file was correctly written.
     * @param exportPath the file to check
     */
    private void assertDataCorrect(Path exportPath) {
        try {
            byte[] b2 = Files.readAllBytes(exportPath);
            assertNotNull(b2);
        } catch (IOException ioe) {
            throw new AssertionError("Export files not found", ioe);
        }
    }

    @After
    public void tearDown() throws IOException {
        if (shouldDeleteDefaultPath) {
            Files.deleteIfExists(defaultPath);
        }
        Files.deleteIfExists(Paths.get(temporaryFilePath));
    }
}
