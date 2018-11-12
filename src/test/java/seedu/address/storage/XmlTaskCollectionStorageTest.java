package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.address.testutil.TypicalTasks.ALICE;
import static seedu.address.testutil.TypicalTasks.HOON;
import static seedu.address.testutil.TypicalTasks.IDA;
import static seedu.address.testutil.TypicalTasks.getTypicalTaskCollections;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyTaskCollection;
import seedu.address.model.TaskCollection;
import seedu.address.storage.xmlstorage.XmlTaskCollectionStorage;

public class XmlTaskCollectionStorageTest {

    private static final Path TEST_DATA_FOLDER = Paths
            .get("src", "test", "data", "XmlTaskCollectionStorageTest");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readTaskCollection_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readTaskCollection(null);
    }

    private java.util.Optional<ReadOnlyTaskCollection> readTaskCollection(String filePath)
            throws Exception {
        return new XmlTaskCollectionStorage(Paths.get(filePath))
                .readTaskCollection(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readTaskCollection("NonExistentFile.xml").isPresent());
    }

    @Test
    public void read_notXmlFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readTaskCollection("NotXmlFormatTaskCollection.xml");

        /* IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
         * That means you should not have more than one exception test in one method
         */
    }

    @Test
    public void readTaskCollection_invalidPersonTaskCollection_throwDataConversionException()
            throws Exception {
        thrown.expect(DataConversionException.class);
        readTaskCollection("invalidTaskInTaskCollection.xml");
    }

    @Test
    public void readTaskCollection_invalidAndValidPersonTaskCollection_throwDataConversionException()
            throws Exception {
        thrown.expect(DataConversionException.class);
        readTaskCollection("invalidAndValidTaskInTaskCollection.xml");
    }

    @Test
    public void readAndSaveTaskCollection_allInOrder_success() throws Exception {
        Path filePath = testFolder.getRoot().toPath().resolve("TempTaskCollection.xml");
        TaskCollection original = getTypicalTaskCollections();
        XmlTaskCollectionStorage xmlTaskCollectionStorage = new XmlTaskCollectionStorage(filePath);

        //Save in new file and read back
        xmlTaskCollectionStorage.saveTaskCollection(original, filePath);
        ReadOnlyTaskCollection readBack = xmlTaskCollectionStorage.readTaskCollection(filePath).get();
        assertEquals(original, new TaskCollection(readBack));

        //Modify data, overwrite exiting file, and read back
        original.addTask(HOON);
        original.removeTask(ALICE);
        xmlTaskCollectionStorage.saveTaskCollection(original, filePath);
        readBack = xmlTaskCollectionStorage.readTaskCollection(filePath).get();
        assertEquals(original, new TaskCollection(readBack));

        //Save and read without specifying file path
        original.addTask(IDA);
        xmlTaskCollectionStorage.saveTaskCollection(original); //file path not specified
        readBack = xmlTaskCollectionStorage.readTaskCollection().get(); //file path not specified
        assertEquals(original, new TaskCollection(readBack));

    }

    @Test
    public void saveTaskCollection_nullTaskCollection_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveTaskCollection(null, "SomeFile.xml");
    }

    /**
     * Saves {@code taskCollection} at the specified {@code filePath}.
     */
    private void saveTaskCollection(ReadOnlyTaskCollection taskCollection, String filePath) {
        try {
            new XmlTaskCollectionStorage(Paths.get(filePath))
                    .saveTaskCollection(taskCollection, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveTaskCollection_nullFilePath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveTaskCollection(new TaskCollection(), null);
    }


}
