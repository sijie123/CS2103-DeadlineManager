package seedu.address.model;

import java.util.function.Consumer;

import seedu.address.model.task.Task;

/**
 * The DuplicateImportConflictResolver class.
 * This class will resolve import conflicts by ignoring the incoming entry, i.e. after the resolver is complete,
 * the existing entry in the TaskCollection will remain.
 */
public class IgnoreImportConflictResolver extends ImportConflictResolver {

    @Override
    public void resolve(Consumer<Task> addTask, Consumer<Task> removeTask, Task task) {
        //Ignore task.
        LOGGER.info("Ignoring task");
    }
}
