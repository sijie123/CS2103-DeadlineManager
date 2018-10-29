package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.TaskCollection;
import seedu.address.storage.csvstorage.CsvSerializableTaskCollection;
import seedu.address.testutil.TypicalTasks;

public class CsvSerializableTaskCollectionTest {

    private static final String csvHeader = "Subject, Start date, All Day Event\n";
    private static final String aliceCsv = "'Alice Pauline', 1/10/2018, True\n";
    private static final String bensonCsv = "'Benson Meier', 1/11/2018, True\n";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void toString_typicalTasks_success() {
        TaskCollection typicalTaskCollection = new TaskCollection();
        typicalTaskCollection.addTask(TypicalTasks.ALICE);
        typicalTaskCollection.addTask(TypicalTasks.BENSON);
        String output = csvHeader + aliceCsv + bensonCsv;
        assertEquals(new CsvSerializableTaskCollection(typicalTaskCollection).toString(), output);
    }

}
