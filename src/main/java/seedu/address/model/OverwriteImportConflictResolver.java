package seedu.address.model;

import java.util.function.Consumer;

import seedu.address.model.task.Task;

/**
 * The OverwriteImportConflictResolver class.
 * This class will resolve import conflicts by overwriting entries, i.e. after the resolver is complete,
 * the newly imported Task will overwrite the existing task in the TaskCollection.
 */
public class OverwriteImportConflictResolver extends ImportConflictResolver {

    @Override
    public void resolve(Consumer<Task> addTask, Consumer<Task> removeTask, Task task) {
        //Ignore task.
        LOGGER.info("Updating task");
        removeTask.accept(task);
        addTask.accept(task);
    }
}
