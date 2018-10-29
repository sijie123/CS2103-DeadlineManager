package seedu.address.storage.CsvStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.model.ReadOnlyTaskCollection;

/**
 * An Immutable TaskCollection that is serializable to CSV format
 */
public class CsvSerializableTaskCollection {

    private List<CsvAdaptedTask> tasks;
    public static final String CSV_HEADER = "Subject, Start date, All Day Event\n";

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
        tasks.addAll(
            src.getTaskList().stream().map(CsvAdaptedTask::new).collect(Collectors.toList()));
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
        for (CsvAdaptedTask task: tasks) {
            csvOutput.append(task.toString() + "\n");
        }
        return csvOutput.toString();
    }
}
