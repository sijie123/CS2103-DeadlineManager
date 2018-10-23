package seedu.address.model;

import java.util.function.Consumer;

import seedu.address.model.task.Task;

/**
 * The DuplicateImportConflictResolver class.
 * This class will resolve import conflicts by duplicating entries, i.e. after the resolver is complete,
 * both entries from the existing and imported TaskCollection will be saved.
 */
public class DuplicateImportConflictResolver extends ImportConflictResolver {

    @Override
    public void resolve(Consumer<Task> addTask, Consumer<Task> removeTask, Task task) {
        //Ignore task.
        LOGGER.info("Duplicating task");
        addTask.accept(task);
    }
}
