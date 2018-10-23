package seedu.address.model;

import java.util.function.Consumer;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.task.Task;

/**
 * The base ImportConflictResolver class.
 * Instantiates a logger for child classes to use.
 * Has a single abstract method resolve, which takes in the add and remove methods to be applied to a task.
 */
public abstract class ImportConflictResolver {
    protected static final Logger LOGGER = LogsCenter.getLogger(ImportConflictResolver.class);
    abstract void resolve(Consumer<Task> addTask, Consumer<Task> removeTask, Task task);
}
