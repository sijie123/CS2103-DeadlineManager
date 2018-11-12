package seedu.address.storage.csvstorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.model.ReadOnlyTaskCollection;

/**
 * An Immutable TaskCollection that is serializable to CSV format
 */
public class CsvSerializableTaskCollection {

    public static final String CSV_HEADER = "Subject, Start date, All Day Event\n";
    private List<CsvAdaptedTask> tasks;


    /**
     * Creates an empty XmlSerializableTaskCollection. This empty constructor is required for
     * marshalling.
     */
    public CsvSerializableTaskCollection() {
        tasks = new ArrayList<>();
    }

    /**
     * Conversion
     */
    public CsvSerializableTaskCollection(ReadOnlyTaskCollection src) {
        this();
        tasks.addAll(src.getTaskList().stream().map(CsvAdaptedTask::new).collect(Collectors.toList()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof CsvSerializableTaskCollection)) {
            return false;
        }
        return tasks.equals(((CsvSerializableTaskCollection) other).tasks);
    }

    @Override
    public String toString() {
        StringBuilder csvOutput = new StringBuilder(CSV_HEADER);
        for (CsvAdaptedTask task : tasks) {
            csvOutput.append(task.toString() + "\n");
        }
        return csvOutput.toString();
    }
}
