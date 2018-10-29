package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static seedu.address.testutil.TypicalTasks.BENSON;

import org.junit.Test;

import seedu.address.storage.csvstorage.CsvAdaptedTask;

public class CsvAdaptedTaskTest {

    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_DEADLINE = "1002";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_DEADLINE = BENSON.getDeadline().toString();

    @Test
    public void constructor_validData_success() {
        CsvAdaptedTask task_validName_invalidDeadline =
            new CsvAdaptedTask(VALID_NAME, INVALID_DEADLINE);
        CsvAdaptedTask task_validName_invalidDeadline2 =
            new CsvAdaptedTask(VALID_NAME, INVALID_DEADLINE);
        CsvAdaptedTask task_validName_validDeadline =
            new CsvAdaptedTask(VALID_NAME, VALID_DEADLINE);
        CsvAdaptedTask task_invalidName_validDeadline =
            new CsvAdaptedTask(INVALID_NAME, VALID_DEADLINE);

        assertEquals(task_validName_invalidDeadline,
            task_validName_invalidDeadline2);
        assertNotEquals(task_validName_invalidDeadline,
            task_validName_validDeadline);
        assertNotEquals(task_validName_invalidDeadline2,
            task_invalidName_validDeadline);
    }

    @Test
    public void constructor_validTask_success() {
        CsvAdaptedTask fromTask = new CsvAdaptedTask(BENSON);
        CsvAdaptedTask fromParams = new CsvAdaptedTask(VALID_NAME, VALID_DEADLINE);
        assertEquals(fromTask, fromParams);
    }

}
