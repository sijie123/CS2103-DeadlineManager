package seedu.address.testutil;

import seedu.address.model.TaskCollection;
import seedu.address.model.task.Task;

/**
 * A utility class to help with building TaskCollection objects. Example usage: <br> {@code TaskCollection
 * ab = new TaskManagerBuilder().withPerson("John", "Doe").build();}
 */
public class TaskManagerBuilder {

    private TaskCollection taskCollection;

    public TaskManagerBuilder() {
        taskCollection = new TaskCollection();
    }

    public TaskManagerBuilder(TaskCollection taskCollection) {
        this.taskCollection = taskCollection;
    }

    /**
     * Adds a new {@code Task} to the {@code TaskCollection} that we are building.
     */
    public TaskManagerBuilder withPerson(Task task) {
        taskCollection.addTask(task);
        return this;
    }

    public TaskCollection build() {
        return taskCollection;
    }
}
