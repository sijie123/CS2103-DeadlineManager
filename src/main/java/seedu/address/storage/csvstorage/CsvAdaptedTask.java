package seedu.address.storage.csvstorage;

import java.util.Objects;

import seedu.address.model.task.Task;

/**
 * JAXB-friendly version of the Task.
 */
public class CsvAdaptedTask {

    private String name;
    private String deadline;

    /**
     * Constructs an XmlAdaptedTask. This is the no-arg constructor that is required by JAXB.
     */
    public CsvAdaptedTask() {
    }

    /**
     * Constructs an {@code XmlAdaptedTask} with the given task details.
     */
    public CsvAdaptedTask(String name, String deadline) {
        this.name = name;
        this.deadline = deadline;
    }

    /**
     * Converts a given Task into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedTask
     */
    public CsvAdaptedTask(Task source) {
        name = "'" + source.getName().value + "'";
        deadline = source.getDeadline().toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof CsvAdaptedTask)) {
            return false;
        }

        CsvAdaptedTask otherTask = (CsvAdaptedTask) other;
        return Objects.equals(name, otherTask.name) && Objects.equals(deadline, otherTask.deadline);
    }

    @Override
    public String toString() {
        return name + ", " + deadline + ", " + "True";
    }
}
