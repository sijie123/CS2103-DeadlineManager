package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalTasks.getTypicalTaskCollections;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.events.model.TaskCollectionChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.model.ReadOnlyTaskCollection;
import seedu.address.model.TaskCollection;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.Assert;
import seedu.address.ui.testutil.EventsCollectorRule;

public class StorageManagerTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private StorageManager storageManager;

    @Before
    public void setUp() {
        XmlTaskCollectionStorage taskCollectionStorage = new XmlTaskCollectionStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        storageManager = new StorageManager(taskCollectionStorage, userPrefsStorage);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.getRoot().toPath().resolve(fileName);
    }


    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(300, 600, 4, 6);
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void taskCollectionReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link XmlTaskCollectionStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link XmlTaskCollectionStorageTest} class.
         */
        TaskCollection original = getTypicalTaskCollections();
        storageManager.saveTaskCollection(original);
        ReadOnlyTaskCollection retrieved = storageManager.readTaskCollection().get();
        assertEquals(original, new TaskCollection(retrieved));
    }

    @Test
    public void getTaskCollectionFilePath() {
        assertNotNull(storageManager.getTaskCollectionFilePath());
    }

    @Test
    public void handleTaskCollectionChangedEvent_exceptionThrown_eventRaised() {
        // Create a StorageManager while injecting a stub that  throws an exception when the save method is called
        Storage storage = new StorageManager(
            new XmlTaskCollectionStorageExceptionThrowingStub(Paths.get("dummy")),
            new JsonUserPrefsStorage(Paths.get("dummy")));
        storage.handleTaskCollectionChangedEvent(new TaskCollectionChangedEvent(new TaskCollection()));
        assertTrue(eventsCollectorRule.eventsCollector
            .getMostRecent() instanceof DataSavingExceptionEvent);
    }

    @Test
    public void exportOnExistingFile_exceptionThrown() throws IOException {
        // Exporting with file name equal to the working file should throw IllegalValueException.
        TaskCollection original = getTypicalTaskCollections();
        storageManager.saveTaskCollection(original);
        Assert.assertThrows(IOException.class,
            String.format(Storage.MESSAGE_WRITE_FILE_EXISTS_ERROR, storageManager.getTaskCollectionFilePath()), () ->
            storageManager.exportTaskCollection(original, storageManager.getTaskCollectionFilePath(), false));
    }

    @Test
    public void exportNewFile_success() {
        TaskCollection original = getTypicalTaskCollections();
        try {
            storageManager.exportTaskCollection(original, getTempFilePath("exportNewNonExistent"), false);
        } catch (IOException ioe) {
            throw new AssertionError("Export on non-existent file should not throw.");
        }
    }

    @Test
    public void overwriteExportOnExistingFile_success() throws IOException {
        // Exporting with file name equal to the working file should throw IllegalValueException.
        TaskCollection original = getTypicalTaskCollections();
        storageManager.saveTaskCollection(original);
        try {
            storageManager.exportTaskCollection(original, storageManager.getTaskCollectionFilePath(), true);
        } catch (IOException ioe) {
            throw new AssertionError("Export on non-existent file should not throw.");
        }
    }

    @Test
    public void overwriteExportOnUnwritableFile_exceptionThrown() throws IOException {
        // Exporting with file name "." should throw as directory is unwritable.
        TaskCollection original = getTypicalTaskCollections();
        Assert.assertThrows(IOException.class,
            String.format(Storage.MESSAGE_WRITE_FILE_NO_PERMISSION_ERROR, getTempFilePath(".")), () ->
            storageManager.exportTaskCollection(original, getTempFilePath("."), false));
    }

    @Test
    public void importNonExistingFile_exceptionThrown() {
        Assert.assertThrows(IOException.class, Storage.MESSAGE_READ_FILE_MISSING_ERROR, () ->
                storageManager.importTaskCollection(getTempFilePath("nonExistent")));
    }

    @Test
    public void importUnreadableFile_exceptionThrown() throws IOException {
        String rubbishFile = "rubbishFile";
        createTempFile(rubbishFile);
        Assert.assertThrows(IOException.class,
            String.format(Storage.MESSAGE_READ_FILE_PARSE_ERROR, getTempFilePath(rubbishFile)), () ->
                storageManager.importTaskCollection(getTempFilePath(rubbishFile)));
        removeTempFile(rubbishFile);
    }

    private void removeTempFile(String filename) {
        File delete = new File(getTempFilePath(filename).toString());
        delete.delete();
    }

    private void createTempFile(String filename) throws IOException {
        FileWriter fileWriter = new FileWriter(getTempFilePath(filename).toString());
        fileWriter.write("garbage");
        fileWriter.close();
    }

    @Test
    public void importExistingFile_success() {
        TaskCollection original = getTypicalTaskCollections();
        try {
            storageManager.saveTaskCollection(original);
            ReadOnlyTaskCollection read = storageManager
                    .importTaskCollection(storageManager.getTaskCollectionFilePath()).get();
        } catch (IOException e) {
            throw new AssertionError("Import on existent file should not throw.");
        }
    }

    @Test
    public void taskCollectionExportImport() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link XmlTaskCollectionStorage} class.
         * More extensive testing of importing exporting is done in {@link XmlTaskCollectionStorageTest} class.
         */
        TaskCollection original = getTypicalTaskCollections();
        storageManager.exportTaskCollection(original, getTempFilePath("dummyExport"), false);
        ReadOnlyTaskCollection retrieved = storageManager.importTaskCollection(getTempFilePath("dummyExport"))
                                                         .get();
        assertEquals(original, new TaskCollection(retrieved));
        Assert.assertThrows(IOException.class,
            String.format(Storage.MESSAGE_WRITE_FILE_EXISTS_ERROR, getTempFilePath("dummyExport")), () ->
            storageManager.exportTaskCollection(original, getTempFilePath("dummyExport"), false));

        try {
            storageManager.exportTaskCollection(original, storageManager.getTaskCollectionFilePath(), true);
        } catch (IOException ioe) {
            throw new AssertionError("Export on non-existent file should not throw.");
        }

    }


    /**
     * A Stub class to throw an exception when the save method is called
     */
    class XmlTaskCollectionStorageExceptionThrowingStub extends XmlTaskCollectionStorage {

        public XmlTaskCollectionStorageExceptionThrowingStub(Path filePath) {
            super(filePath);
        }

        @Override
        public void saveTaskCollection(ReadOnlyTaskCollection taskCollection, Path filePath)
            throws IOException {
            throw new IOException("dummy exception");
        }
    }


}
