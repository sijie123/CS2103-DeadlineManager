package seedu.address.model;

import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.model.task.Task;

/**
 * The API of the Model component.
 */
public interface Model {

    /**
     * {@code Predicate} that always evaluate to true
     */
    Predicate<Task> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /**
     * Clears existing backing model and replaces with the provided new data.
     */
    void resetData(ReadOnlyTaskCollection newData);

    /**
     * Returns the TaskCollection
     */
    ReadOnlyTaskCollection getAddressBook();

    /**
     * Returns true if a task with the same identity as {@code task} exists in the address book.
     */
    boolean hasPerson(Task task);

    /**
     * Deletes the given task. The task must exist in the address book.
     */
    void deletePerson(Task target);

    /**
     * Adds the given task. {@code task} must not already exist in the address book.
     */
    void addPerson(Task task);

    /**
     * Replaces the given task {@code target} with {@code editedTask}. {@code target} must exist in
     * the address book. The task identity of {@code editedTask} must not be the same as another
     * existing task in the address book.
     */
    void updatePerson(Task target, Task editedTask);

    /**
     * Returns an unmodifiable view of the filtered task list
     */
    ObservableList<Task> getFilteredPersonList();

    /**
     * Updates the filter of the filtered task list to filter by the given {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Task> predicate);

    /**
     * Returns true if the model has previous address book states to restore.
     */
    boolean canUndoAddressBook();

    /**
     * Returns true if the model has undone address book states to restore.
     */
    boolean canRedoAddressBook();

    /**
     * Restores the model's address book to its previous state.
     */
    void undoAddressBook();

    /**
     * Restores the model's address book to its previously undone state.
     */
    void redoAddressBook();

    /**
     * Saves the current address book state for undo/redo.
     */
    void commitAddressBook();
}
