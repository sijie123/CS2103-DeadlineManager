package seedu.address.logic.commands;

import java.util.Comparator;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.events.storage.ImportDataAvailableEvent;
import seedu.address.model.ImportConflictResolver;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyTaskCollection;
import seedu.address.model.task.Task;

/**
 * A default model stub that have all of the methods failing.
 */
public class ModelStub extends ComponentManager implements Model {

    @Override
    public void addTask(Task task) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void resetData(ReadOnlyTaskCollection newData) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyTaskCollection getTaskCollection() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasTask(Task task) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void deleteTask(Task target) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void updateTask(Task target, Task editedTask) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void updateSortedTaskList(Comparator<Task> comparator) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ObservableList<Task> getFilteredTaskList() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void updateFilteredTaskList(Predicate<Task> predicate) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean canUndoTaskCollection() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean canRedoTaskCollection() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void undoTaskCollection() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void redoTaskCollection() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void commitTaskCollection() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void exportTaskCollection(String filename, boolean shouldOverwrite) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void importTaskCollection(String filename, ImportConflictResolver conflictMode) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void handleImportDataAvailableEvent(ImportDataAvailableEvent event) {
        throw new AssertionError("This method should not be called.");
    }

}
