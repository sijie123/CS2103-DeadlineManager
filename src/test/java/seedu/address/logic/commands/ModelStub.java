package seedu.address.logic.commands;

import java.util.Comparator;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.events.storage.ImportDataAvailableEvent;
import seedu.address.commons.events.storage.ImportExportExceptionEvent;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ModelManager.ImportConflictMode;
import seedu.address.model.ReadOnlyTaskCollection;
import seedu.address.model.task.Task;

/**
 * A default model stub that have all of the methods failing.
 */
public class ModelStub implements Model {

    @Override
    public void addPerson(Task task) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void resetData(ReadOnlyTaskCollection newData) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyTaskCollection getAddressBook() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasPerson(Task task) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void deletePerson(Task target) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void updatePerson(Task target, Task editedTask) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void updateSortedPersonList(Comparator<Task> comparator) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ObservableList<Task> getFilteredPersonList() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void updateFilteredPersonList(Predicate<Task> predicate) {
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
