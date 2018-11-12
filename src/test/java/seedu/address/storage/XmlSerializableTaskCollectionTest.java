package seedu.address.storage;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.XmlUtil;
import seedu.address.model.TaskCollection;
import seedu.address.storage.xmlstorage.XmlSerializableTaskCollection;
import seedu.address.testutil.TypicalTasks;

public class XmlSerializableTaskCollectionTest {

    private static final Path TEST_DATA_FOLDER = Paths
            .get("src", "test", "data", "XmlSerializableTaskCollectionTest");
    private static final Path TYPICAL_TASKS_FILE = TEST_DATA_FOLDER
            .resolve("typicalTasksInTaskCollection.xml");
    private static final Path INVALID_TASK_FILE = TEST_DATA_FOLDER
            .resolve("invalidTaskInTaskCollection.xml");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void toModelType_typicalTasksFile_success() throws Exception {
        XmlSerializableTaskCollection dataFromFile = XmlUtil.getDataFromFile(TYPICAL_TASKS_FILE,
                XmlSerializableTaskCollection.class);
        TaskCollection taskCollectionFromFile = dataFromFile.toModelType();
        TaskCollection typicalTasksTaskCollection = TypicalTasks.getTypicalTaskCollections();
        assertEquals(taskCollectionFromFile, typicalTasksTaskCollection);
    }

    @Test
    public void toModelType_invalidTaskFile_throwsIllegalValueException() throws Exception {
        XmlSerializableTaskCollection dataFromFile = XmlUtil.getDataFromFile(INVALID_TASK_FILE,
                XmlSerializableTaskCollection.class);
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }

}
