package seedu.address.logic.commands;

import java.util.Comparator;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.events.storage.ImportDataAvailableEvent;
import seedu.address.commons.events.storage.ImportExportExceptionEvent;
import seedu.address.model.Model;
import seedu.address.model.ModelManager.ImportConflictMode;
import seedu.address.model.ReadOnlyTaskCollection;
import seedu.address.model.task.Task;

/**
 * A default model stub that have all of the methods failing.
 */
public class ModelStub implements Model {

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
    public boolean canUndoAddressBook() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean canRedoAddressBook() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void undoAddressBook() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void redoAddressBook() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void commitAddressBook() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean importExportFailed() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public String getLastError() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void exportAddressBook(String filename) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void importAddressBook(String filename) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void importAddressBook(String filename, ImportConflictMode conflictMode) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void handleImportDataAvailableEvent(ImportDataAvailableEvent event) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void handleImportExportExceptionEvent(ImportExportExceptionEvent event) {
        throw new AssertionError("This method should not be called.");
    }
}
