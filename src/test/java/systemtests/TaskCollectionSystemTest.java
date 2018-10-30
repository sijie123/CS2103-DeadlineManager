package systemtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.ui.StatusBarFooter.SYNC_STATUS_INITIAL;
import static seedu.address.ui.StatusBarFooter.SYNC_STATUS_UPDATED;
import static seedu.address.ui.testutil.GuiTestAssert.assertListMatching;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;

import guitests.guihandles.CommandBoxHandle;
import guitests.guihandles.MainMenuHandle;
import guitests.guihandles.MainWindowHandle;
import guitests.guihandles.ResultDisplayHandle;
import guitests.guihandles.StatusBarFooterHandle;
import guitests.guihandles.TaskListPanelHandle;
import seedu.address.TestApp;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.model.Model;
import seedu.address.model.TaskCollection;
import seedu.address.testutil.TypicalTasks;
import seedu.address.ui.CommandBox;

/**
 * A system test class for TaskCollection, which provides access to handles of GUI components and
 * helper methods for test verification.
 */
public abstract class TaskCollectionSystemTest {

    @ClassRule
    public static ClockRule clockRule = new ClockRule();

    private static final List<String> COMMAND_BOX_DEFAULT_STYLE = Arrays
        .asList("text-input", "text-field");
    private static final List<String> COMMAND_BOX_ERROR_STYLE =
        Arrays.asList("text-input", "text-field", CommandBox.ERROR_STYLE_CLASS);

    private MainWindowHandle mainWindowHandle;
    private TestApp testApp;
    private SystemTestSetupHelper setupHelper;

    @BeforeClass
    public static void setupBeforeClass() {
        SystemTestSetupHelper.initialize();
    }

    @Before
    public void setUp() {
        setupHelper = new SystemTestSetupHelper();
        testApp = setupHelper.setupApplication(this::getInitialData, getDataFileLocation());
        mainWindowHandle = setupHelper.setupMainWindowHandle();

        assertApplicationStartingStateIsCorrect();
    }

    @After
    public void tearDown() {
        setupHelper.tearDownStage();
        EventsCenter.clearSubscribers();
    }

    /**
     * Returns the data to be loaded into the file in {@link #getDataFileLocation()}.
     */
    protected TaskCollection getInitialData() {
        return TypicalTasks.getTypicalTaskCollections();
    }

    /**
     * Returns the directory of the data file.
     */
    protected Path getDataFileLocation() {
        return TestApp.SAVE_LOCATION_FOR_TESTING;
    }

    public MainWindowHandle getMainWindowHandle() {
        return mainWindowHandle;
    }

    public CommandBoxHandle getCommandBox() {
        return mainWindowHandle.getCommandBox();
    }

    public TaskListPanelHandle getPersonListPanel() {
        return mainWindowHandle.getPersonListPanel();
    }

    public MainMenuHandle getMainMenu() {
        return mainWindowHandle.getMainMenu();
    }

    public StatusBarFooterHandle getStatusBarFooter() {
        return mainWindowHandle.getStatusBarFooter();
    }

    public ResultDisplayHandle getResultDisplay() {
        return mainWindowHandle.getResultDisplay();
    }

    /**
     * Executes {@code command} in the application's {@code CommandBox}. Method returns after UI
     * components have been updated.
     */
    protected void executeCommand(String command) {
        rememberStates();
        // Injects a fixed clock before executing a command so that the time stamp shown in the status bar
        // after each command is predictable and also different from the previous command.
        clockRule.setInjectedClockToCurrentTime();

        mainWindowHandle.getCommandBox().run(command);

    }

    /**
     * Displays all tasks in the deadline manager.
     */
    protected void showAllTasks() {
        executeCommand(ListCommand.COMMAND_WORD);
        assertEquals(getModel().getTaskCollection().getTaskList().size(),
            getModel().getFilteredTaskList().size());
    }

    /**
     * Displays all tasks with any parts of their names matching {@code keyword}
     * (case-insensitive).
     */
    protected void showTasksWithName(String keyword) {
        executeCommand(FilterCommand.COMMAND_WORD + " " + keyword);
        assertTrue(getModel().getFilteredTaskList().size() < getModel().getTaskCollection()
            .getTaskList().size());
    }

    /**
     * Selects the task at {@code index} of the displayed list.
     */
    protected void selectTask(Index index) {
        executeCommand(SelectCommand.COMMAND_WORD + " " + index.getOneBased());
        assertEquals(index.getZeroBased(), getPersonListPanel().getSelectedCardIndex());
    }

    /**
     * Deletes all tasks in the deadline manager.
     */
    protected void deleteAllTests() {
        executeCommand(ClearCommand.COMMAND_WORD);
        assertEquals(0, getModel().getTaskCollection().getTaskList().size());
    }

    /**
     * Asserts that the {@code CommandBox} displays {@code expectedCommandInput}, the {@code
     * ResultDisplay} displays {@code expectedResultMessage}, the storage contains the same task
     * objects as {@code expectedModel} and the task list panel displays the tasks in the model
     * correctly.
     */
    protected void assertApplicationDisplaysExpected(String expectedCommandInput,
                                                     String expectedResultMessage,
                                                     Model expectedModel) {
        assertEquals(expectedCommandInput, getCommandBox().getInput());
        assertEquals(expectedResultMessage, getResultDisplay().getText());
        assertEquals(new TaskCollection(expectedModel.getTaskCollection()),
            testApp.readStorageTaskCollection());
        assertListMatching(getPersonListPanel(), expectedModel.getFilteredTaskList());
    }

    /**
     * Calls {@code TaskListPanelHandle} and {@code
     * StatusBarFooterHandle} to remember their current state.
     */
    private void rememberStates() {
        StatusBarFooterHandle statusBarFooterHandle = getStatusBarFooter();
        statusBarFooterHandle.rememberSaveLocation();
        statusBarFooterHandle.rememberSyncStatus();
        getPersonListPanel().rememberSelectedTaskCard();
    }

    /**
     * Asserts that the previously selected card is now deselected.
     */
    protected void assertSelectedCardDeselected() {
        assertFalse(getPersonListPanel().isAnyCardSelected());
    }

    /**
     * Asserts that the browser's url is changed to display the details of the task in the task list
     * panel at {@code expectedSelectedCardIndex}, and only the card at {@code
     * expectedSelectedCardIndex} is selected.
     *
     * @see TaskListPanelHandle#isSelectedTaskCardChanged()
     */
    protected void assertSelectedCardChanged(Index expectedSelectedCardIndex) {
        getPersonListPanel().navigateToCard(getPersonListPanel().getSelectedCardIndex());
        assertEquals(expectedSelectedCardIndex.getZeroBased(),
            getPersonListPanel().getSelectedCardIndex());
    }

    /**
     * Asserts that the browser's url and the selected card in the task list panel remain
     * unchanged.
     *
     * @see TaskListPanelHandle#isSelectedTaskCardChanged()
     */
    protected void assertSelectedCardUnchanged() {
        assertFalse(getPersonListPanel().isSelectedTaskCardChanged());
    }

    /**
     * Asserts that the command box's shows the default style.
     */
    protected void assertCommandBoxShowsDefaultStyle() {
        assertEquals(COMMAND_BOX_DEFAULT_STYLE, getCommandBox().getStyleClass());
    }

    /**
     * Asserts that the command box's shows the error style.
     */
    protected void assertCommandBoxShowsErrorStyle() {
        assertEquals(COMMAND_BOX_ERROR_STYLE, getCommandBox().getStyleClass());
    }

    /**
     * Asserts that the entire status bar remains the same.
     */
    protected void assertStatusBarUnchanged() {
        StatusBarFooterHandle handle = getStatusBarFooter();
        assertFalse(handle.isSaveLocationChanged());
        assertFalse(handle.isSyncStatusChanged());
    }

    /**
     * Asserts that only the sync status in the status bar was changed to the timing of {@code
     * ClockRule#getInjectedClock()}, while the save location remains the same.
     */
    protected void assertStatusBarUnchangedExceptSyncStatus() {
        StatusBarFooterHandle handle = getStatusBarFooter();
        String timestamp = new Date(clockRule.getInjectedClock().millis()).toString();
        String expectedSyncStatus = String.format(SYNC_STATUS_UPDATED, timestamp);
        assertEquals(expectedSyncStatus, handle.getSyncStatus());
        assertFalse(handle.isSaveLocationChanged());
    }

    /**
     * Asserts that the starting state of the application is correct.
     */
    private void assertApplicationStartingStateIsCorrect() {
        assertEquals("", getCommandBox().getInput());
        assertEquals("", getResultDisplay().getText());
        assertListMatching(getPersonListPanel(), getModel().getFilteredTaskList());
        assertEquals(Paths.get(".").resolve(testApp.getStorageSaveLocation()).toString(),
            getStatusBarFooter().getSaveLocation());
        assertEquals(SYNC_STATUS_INITIAL, getStatusBarFooter().getSyncStatus());
    }

    /**
     * Returns a defensive copy of the current model.
     */
    protected Model getModel() {
        return testApp.getModel();
    }
}
