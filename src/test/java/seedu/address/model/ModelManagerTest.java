package seedu.address.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_TASKS;
import static seedu.address.testutil.TypicalTasks.ALICE;
import static seedu.address.testutil.TypicalTasks.BENSON;

import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.events.BaseEvent;
import seedu.address.commons.events.model.ExportRequestEvent;
import seedu.address.commons.events.model.ImportRequestEvent;
import seedu.address.model.task.NameContainsKeywordsPredicate;
import seedu.address.testutil.TaskManagerBuilder;
import seedu.address.ui.testutil.EventsCollectorRule;

public class ModelManagerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private ModelManager modelManager = new ModelManager();

    private static final String VALID_PATH = "dummyFile.txt";

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.hasTask(null);
    }

    @Test
    public void hasPerson_personNotInTaskCollection_returnsFalse() {
        assertFalse(modelManager.hasTask(ALICE));
    }

    @Test
    public void hasPerson_personInTaskCollection_returnsTrue() {
        modelManager.addTask(ALICE);
        assertTrue(modelManager.hasTask(ALICE));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        modelManager.getFilteredTaskList().remove(0);
    }

    @Test
    public void exportTaskCollection_nullFilename_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.exportTaskCollection(null, false, false);
    }

    @Test
    public void exportTaskCollection_validFilename_eventRaised() {
        ModelManager exportManager = new ModelManager();
        exportManager.addTask(ALICE);

        TaskCollection expectedTaskCollection = new TaskCollection();
        expectedTaskCollection.addTask(ALICE);

        exportManager.exportTaskCollection(VALID_PATH, false, false);
        BaseEvent event = eventsCollectorRule.eventsCollector.getMostRecent();
        assertTrue(event instanceof ExportRequestEvent);
        assertTrue(((ExportRequestEvent) event).filename.equals(VALID_PATH));
        assertTrue(((ExportRequestEvent) event).data.equals(expectedTaskCollection));
    }

    @Test
    public void importTaskCollection_nullFilename_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.importTaskCollection(null, new IgnoreImportConflictResolver());
    }

    @Test
    public void importTaskCollection_nullResolver_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.importTaskCollection(VALID_PATH, null);
    }

    @Test
    public void importTaskCollection_validParameters_eventRaised() {
        modelManager.importTaskCollection(VALID_PATH, new IgnoreImportConflictResolver());
        BaseEvent event = eventsCollectorRule.eventsCollector.getMostRecent();
        assertTrue(event instanceof ImportRequestEvent);
        assertTrue(((ImportRequestEvent) event).filename.equals(VALID_PATH));
    }


    @Test
    public void equals() {
        TaskCollection taskCollection = new TaskManagerBuilder().withPerson(ALICE).withPerson(BENSON)
            .build();
        TaskCollection differentTaskCollection = new TaskCollection();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(taskCollection, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(taskCollection, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different taskCollection -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentTaskCollection, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().value.split("\\s+");
        modelManager.updateFilteredTaskList(
            new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(taskCollection, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);

        // different userPrefs -> returns true
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setDeadlineManagerFilePath(Paths.get("differentFilePath"));
        assertTrue(modelManager.equals(new ModelManager(taskCollection, differentUserPrefs)));
    }
}
