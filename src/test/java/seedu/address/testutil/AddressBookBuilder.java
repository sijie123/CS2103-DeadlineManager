package seedu.address.testutil;

import seedu.address.model.TaskCollection;
import seedu.address.model.task.Task;

/**
 * A utility class to help with building Addressbook objects. Example usage: <br> {@code TaskCollection
 * ab = new AddressBookBuilder().withPerson("John", "Doe").build();}
 */
public class AddressBookBuilder {

    private TaskCollection taskCollection;

    public AddressBookBuilder() {
        taskCollection = new TaskCollection();
    }

    public AddressBookBuilder(TaskCollection taskCollection) {
        this.taskCollection = taskCollection;
    }

    /**
     * Adds a new {@code Task} to the {@code TaskCollection} that we are building.
     */
    public AddressBookBuilder withPerson(Task task) {
        taskCollection.addPerson(task);
        return this;
    }

    public TaskCollection build() {
        return taskCollection;
    }
}
