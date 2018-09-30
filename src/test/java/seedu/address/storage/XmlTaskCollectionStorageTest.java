package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.HOON;
import static seedu.address.testutil.TypicalPersons.IDA;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

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

public class XmlTaskCollectionStorageTest {

    private static final Path TEST_DATA_FOLDER = Paths
        .get("src", "test", "data", "XmlTaskCollectionStorageTest");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readAddressBook_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readAddressBook(null);
    }

    private java.util.Optional<ReadOnlyTaskCollection> readAddressBook(String filePath)
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
        assertFalse(readAddressBook("NonExistentFile.xml").isPresent());
    }

    @Test
    public void read_notXmlFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readAddressBook("NotXmlFormatTaskCollection.xml");

        /* IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
         * That means you should not have more than one exception test in one method
         */
    }

    @Test
    public void readAddressBook_invalidPersonAddressBook_throwDataConversionException()
        throws Exception {
        thrown.expect(DataConversionException.class);
        readAddressBook("invalidTaskInTaskCollection.xml");
    }

    @Test
    public void readAddressBook_invalidAndValidPersonAddressBook_throwDataConversionException()
        throws Exception {
        thrown.expect(DataConversionException.class);
        readAddressBook("invalidAndValidTaskInTaskCollection.xml");
    }

    @Test
    public void readAndSaveAddressBook_allInOrder_success() throws Exception {
        Path filePath = testFolder.getRoot().toPath().resolve("TempAddressBook.xml");
        TaskCollection original = getTypicalAddressBook();
        XmlTaskCollectionStorage xmlAddressBookStorage = new XmlTaskCollectionStorage(filePath);

        //Save in new file and read back
        xmlAddressBookStorage.saveTaskCollection(original, filePath);
        ReadOnlyTaskCollection readBack = xmlAddressBookStorage.readTaskCollection(filePath).get();
        assertEquals(original, new TaskCollection(readBack));

        //Modify data, overwrite exiting file, and read back
        original.addPerson(HOON);
        original.removeTask(ALICE);
        xmlAddressBookStorage.saveTaskCollection(original, filePath);
        readBack = xmlAddressBookStorage.readTaskCollection(filePath).get();
        assertEquals(original, new TaskCollection(readBack));

        //Save and read without specifying file path
        original.addPerson(IDA);
        xmlAddressBookStorage.saveTaskCollection(original); //file path not specified
        readBack = xmlAddressBookStorage.readTaskCollection().get(); //file path not specified
        assertEquals(original, new TaskCollection(readBack));

    }

    @Test
    public void saveAddressBook_nullAddressBook_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveAddressBook(null, "SomeFile.xml");
    }

    /**
     * Saves {@code addressBook} at the specified {@code filePath}.
     */
    private void saveAddressBook(ReadOnlyTaskCollection addressBook, String filePath) {
        try {
            new XmlTaskCollectionStorage(Paths.get(filePath))
                .saveTaskCollection(addressBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveAddressBook_nullFilePath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveAddressBook(new TaskCollection(), null);
    }


}
