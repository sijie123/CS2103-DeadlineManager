package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.address.model.ReadOnlyTaskCollection;
import seedu.address.model.TaskCollection;
import seedu.address.storage.csvstorage.CsvTaskCollectionWriteStorage;

public class CsvTaskCollectionWriteStorageTest {

    private static final Path TEST_DATA_FOLDER = Paths
            .get("src", "test", "data", "CsvTaskCollectionStorageTest");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void saveTaskCollection_nullTaskCollection_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveTaskCollection(null, "SomeFile.csv");
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    /**
     * Saves {@code taskCollection} at the specified {@code filePath}.
     */
    private void saveTaskCollection(ReadOnlyTaskCollection taskCollection, String filePath) {
        try {
            new CsvTaskCollectionWriteStorage(Paths.get(filePath))
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

    @Test
    public void saveTaskCollection_validInput_success() {
        try {
            saveTaskCollection(new TaskCollection(), "test.csv");
        } catch (Exception e) {
            throw new AssertionError("Correct parameters should not throw.", e);
        }
    }

}
