package seedu.address.commons.util;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.TaskCollection;
import seedu.address.storage.XmlAdaptedTag;
import seedu.address.storage.XmlAdaptedTask;
import seedu.address.storage.XmlSerializableTaskCollection;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TestUtil;

public class XmlUtilTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "XmlUtilTest");
    private static final Path EMPTY_FILE = TEST_DATA_FOLDER.resolve("empty.xml");
    private static final Path MISSING_FILE = TEST_DATA_FOLDER.resolve("missing.xml");
    private static final Path VALID_FILE = TEST_DATA_FOLDER.resolve("validTaskCollection.xml");
    private static final Path MISSING_PERSON_FIELD_FILE = TEST_DATA_FOLDER
        .resolve("missingTaskField.xml");
    private static final Path INVALID_PERSON_FIELD_FILE = TEST_DATA_FOLDER
        .resolve("invalidTaskField.xml");
    private static final Path VALID_PERSON_FILE = TEST_DATA_FOLDER.resolve("validTask.xml");
    private static final Path TEMP_FILE = TestUtil
        .getFilePathInSandboxFolder("tempTaskCollection.xml");

    private static final String INVALID_PHONE = "9482asf424";

    private static final String VALID_NAME = "Hans Muster";
    private static final String VALID_PHONE = "9482424";
    private static final String VALID_EMAIL = "hans@example";
    private static final String VALID_ADDRESS = "4th street";
    private static final List<XmlAdaptedTag> VALID_TAGS = Collections
        .singletonList(new XmlAdaptedTag("friends"));

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getDataFromFile_nullFile_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.getDataFromFile(null, TaskCollection.class);
    }

    @Test
    public void getDataFromFile_nullClass_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.getDataFromFile(VALID_FILE, null);
    }

    @Test
    public void getDataFromFile_missingFile_fileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.getDataFromFile(MISSING_FILE, TaskCollection.class);
    }

    @Test
    public void getDataFromFile_emptyFile_dataFormatMismatchException() throws Exception {
        thrown.expect(JAXBException.class);
        XmlUtil.getDataFromFile(EMPTY_FILE, TaskCollection.class);
    }

    @Test
    public void getDataFromFile_validFile_validResult() throws Exception {
        TaskCollection dataFromFile = XmlUtil
            .getDataFromFile(VALID_FILE, XmlSerializableTaskCollection.class).toModelType();
        assertEquals(9, dataFromFile.getTaskList().size());
    }

    @Test
    public void xmlAdaptedPersonFromFile_fileWithMissingPersonField_validResult() throws Exception {
        XmlAdaptedTask actualPerson = XmlUtil.getDataFromFile(
            MISSING_PERSON_FIELD_FILE, XmlAdaptedTaskWithRootElement.class);
        XmlAdaptedTask expectedPerson = new XmlAdaptedTask(
            null, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        assertEquals(expectedPerson, actualPerson);
    }

    @Test
    public void xmlAdaptedPersonFromFile_fileWithInvalidPersonField_validResult() throws Exception {
        XmlAdaptedTask actualPerson = XmlUtil.getDataFromFile(
            INVALID_PERSON_FIELD_FILE, XmlAdaptedTaskWithRootElement.class);
        XmlAdaptedTask expectedPerson = new XmlAdaptedTask(
            VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        assertEquals(expectedPerson, actualPerson);
    }

    @Test
    public void xmlAdaptedPersonFromFile_fileWithValidPerson_validResult() throws Exception {
        XmlAdaptedTask actualPerson = XmlUtil.getDataFromFile(
            VALID_PERSON_FILE, XmlAdaptedTaskWithRootElement.class);
        XmlAdaptedTask expectedPerson = new XmlAdaptedTask(
            VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        assertEquals(expectedPerson, actualPerson);
    }

    @Test
    public void saveDataToFile_nullFile_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.saveDataToFile(null, new TaskCollection());
    }

    @Test
    public void saveDataToFile_nullClass_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.saveDataToFile(VALID_FILE, null);
    }

    @Test
    public void saveDataToFile_missingFile_fileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.saveDataToFile(MISSING_FILE, new TaskCollection());
    }

    @Test
    public void saveDataToFile_validFile_dataSaved() throws Exception {
        FileUtil.createFile(TEMP_FILE);
        XmlSerializableTaskCollection dataToWrite = new XmlSerializableTaskCollection(new TaskCollection());
        XmlUtil.saveDataToFile(TEMP_FILE, dataToWrite);
        XmlSerializableTaskCollection dataFromFile = XmlUtil
            .getDataFromFile(TEMP_FILE, XmlSerializableTaskCollection.class);
        assertEquals(dataToWrite, dataFromFile);

        AddressBookBuilder builder = new AddressBookBuilder(new TaskCollection());
        dataToWrite = new XmlSerializableTaskCollection(
            builder.withPerson(new PersonBuilder().build()).build());

        XmlUtil.saveDataToFile(TEMP_FILE, dataToWrite);
        dataFromFile = XmlUtil.getDataFromFile(TEMP_FILE, XmlSerializableTaskCollection.class);
        assertEquals(dataToWrite, dataFromFile);
    }

    /**
     * Test class annotated with {@code XmlRootElement} to allow unmarshalling of .xml data to
     * {@code XmlAdaptedTask} objects.
     */
    @XmlRootElement(name = "task")
    private static class XmlAdaptedTaskWithRootElement extends XmlAdaptedTask {

    }
}
