package seedu.address.model;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code TaskCollection} that keeps track of its own history.
 */
public class VersionedTaskCollection extends TaskCollection {

    private final List<ReadOnlyTaskCollection> addressBookStateList;
    private int currentStatePointer;

    public VersionedTaskCollection(ReadOnlyTaskCollection initialState) {
        super(initialState);

        addressBookStateList = new ArrayList<>();
        addressBookStateList.add(new TaskCollection(initialState));
        currentStatePointer = 0;
    }

    /**
     * Saves a copy of the current {@code TaskCollection} state at the end of the state list. Undone
     * states are removed from the state list.
     */
    public void commit() {
        removeStatesAfterCurrentPointer();
        addressBookStateList.add(new TaskCollection(this));
        currentStatePointer++;
    }

    private void removeStatesAfterCurrentPointer() {
        addressBookStateList.subList(currentStatePointer + 1, addressBookStateList.size()).clear();
    }

    /**
     * Restores the deadline manager to its previous state.
     */
    public void undo() {
        if (!canUndo()) {
            throw new NoUndoableStateException();
        }
        currentStatePointer--;
        resetData(addressBookStateList.get(currentStatePointer));
    }

    /**
     * Restores the deadline manager to its previously undone state.
     */
    public void redo() {
        if (!canRedo()) {
            throw new NoRedoableStateException();
        }
        currentStatePointer++;
        resetData(addressBookStateList.get(currentStatePointer));
    }

    /**
     * Returns true if {@code undo()} has deadline manager states to undo.
     */
    public boolean canUndo() {
        return currentStatePointer > 0;
    }

    /**
     * Returns true if {@code redo()} has deadline manager states to redo.
     */
    public boolean canRedo() {
        return currentStatePointer < addressBookStateList.size() - 1;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof VersionedTaskCollection)) {
            return false;
        }

        VersionedTaskCollection otherVersionedAddressBook = (VersionedTaskCollection) other;

        // state check
        return super.equals(otherVersionedAddressBook)
            && addressBookStateList.equals(otherVersionedAddressBook.addressBookStateList)
            && currentStatePointer == otherVersionedAddressBook.currentStatePointer;
    }

    /**
     * Thrown when trying to {@code undo()} but can't.
     */
    public static class NoUndoableStateException extends RuntimeException {

        private NoUndoableStateException() {
            super("Current state pointer at start of addressBookState list, unable to undo.");
        }
    }

    /**
     * Thrown when trying to {@code redo()} but can't.
     */
    public static class NoRedoableStateException extends RuntimeException {

        private NoRedoableStateException() {
            super("Current state pointer at end of addressBookState list, unable to redo.");
        }
    }
}
