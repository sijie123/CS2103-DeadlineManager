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
        CsvAdaptedTask taskValidNameInvalidDeadline =
                new CsvAdaptedTask(VALID_NAME, INVALID_DEADLINE);
        CsvAdaptedTask taskValidNameInvalidDeadline2 =
                new CsvAdaptedTask(VALID_NAME, INVALID_DEADLINE);
        CsvAdaptedTask taskValidNameValidDeadline =
                new CsvAdaptedTask(VALID_NAME, VALID_DEADLINE);
        CsvAdaptedTask taskInvalidNameValidDeadline =
                new CsvAdaptedTask(INVALID_NAME, VALID_DEADLINE);

        assertEquals(taskValidNameInvalidDeadline,
                taskValidNameInvalidDeadline2);
        assertNotEquals(taskValidNameInvalidDeadline,
                taskValidNameValidDeadline);
        assertNotEquals(taskValidNameInvalidDeadline2,
                taskInvalidNameValidDeadline);
    }

    @Test
    public void constructor_validTask_success() {
        CsvAdaptedTask fromTask = new CsvAdaptedTask(BENSON);
        CsvAdaptedTask fromParams = new CsvAdaptedTask(VALID_NAME, VALID_DEADLINE);
        assertEquals(fromTask, fromParams);
    }

}
