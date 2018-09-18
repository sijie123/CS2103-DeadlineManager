package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.CARL;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.address.testutil.AddressBookBuilder;

public class VersionedTaskCollectionTest {

    private final ReadOnlyTaskCollection addressBookWithAmy = new AddressBookBuilder().withPerson(AMY)
        .build();
    private final ReadOnlyTaskCollection addressBookWithBob = new AddressBookBuilder().withPerson(BOB)
        .build();
    private final ReadOnlyTaskCollection addressBookWithCarl = new AddressBookBuilder()
        .withPerson(CARL).build();
    private final ReadOnlyTaskCollection emptyAddressBook = new AddressBookBuilder().build();

    @Test
    public void commit_singleAddressBook_noStatesRemovedCurrentStateSaved() {
        VersionedTaskCollection versionedAddressBook = prepareAddressBookList(emptyAddressBook);

        versionedAddressBook.commit();
        assertAddressBookListStatus(versionedAddressBook,
            Collections.singletonList(emptyAddressBook),
            emptyAddressBook,
            Collections.emptyList());
    }

    @Test
    public void commit_multipleAddressBookPointerAtEndOfStateList_noStatesRemovedCurrentStateSaved() {
        VersionedTaskCollection versionedAddressBook = prepareAddressBookList(
            emptyAddressBook, addressBookWithAmy, addressBookWithBob);

        versionedAddressBook.commit();
        assertAddressBookListStatus(versionedAddressBook,
            Arrays.asList(emptyAddressBook, addressBookWithAmy, addressBookWithBob),
            addressBookWithBob,
            Collections.emptyList());
    }

    @Test
    public void commit_multipleAddressBookPointerNotAtEndOfStateList_statesAfterPointerRemovedCurrentStateSaved() {
        VersionedTaskCollection versionedAddressBook = prepareAddressBookList(
            emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedAddressBook, 2);

        versionedAddressBook.commit();
        assertAddressBookListStatus(versionedAddressBook,
            Collections.singletonList(emptyAddressBook),
            emptyAddressBook,
            Collections.emptyList());
    }

    @Test
    public void canUndo_multipleAddressBookPointerAtEndOfStateList_returnsTrue() {
        VersionedTaskCollection versionedAddressBook = prepareAddressBookList(
            emptyAddressBook, addressBookWithAmy, addressBookWithBob);

        assertTrue(versionedAddressBook.canUndo());
    }

    @Test
    public void canUndo_multipleAddressBookPointerAtStartOfStateList_returnsTrue() {
        VersionedTaskCollection versionedAddressBook = prepareAddressBookList(
            emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedAddressBook, 1);

        assertTrue(versionedAddressBook.canUndo());
    }

    @Test
    public void canUndo_singleAddressBook_returnsFalse() {
        VersionedTaskCollection versionedAddressBook = prepareAddressBookList(emptyAddressBook);

        assertFalse(versionedAddressBook.canUndo());
    }

    @Test
    public void canUndo_multipleAddressBookPointerAtStartOfStateList_returnsFalse() {
        VersionedTaskCollection versionedAddressBook = prepareAddressBookList(
            emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedAddressBook, 2);

        assertFalse(versionedAddressBook.canUndo());
    }

    @Test
    public void canRedo_multipleAddressBookPointerNotAtEndOfStateList_returnsTrue() {
        VersionedTaskCollection versionedAddressBook = prepareAddressBookList(
            emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedAddressBook, 1);

        assertTrue(versionedAddressBook.canRedo());
    }

    @Test
    public void canRedo_multipleAddressBookPointerAtStartOfStateList_returnsTrue() {
        VersionedTaskCollection versionedAddressBook = prepareAddressBookList(
            emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedAddressBook, 2);

        assertTrue(versionedAddressBook.canRedo());
    }

    @Test
    public void canRedo_singleAddressBook_returnsFalse() {
        VersionedTaskCollection versionedAddressBook = prepareAddressBookList(emptyAddressBook);

        assertFalse(versionedAddressBook.canRedo());
    }

    @Test
    public void canRedo_multipleAddressBookPointerAtEndOfStateList_returnsFalse() {
        VersionedTaskCollection versionedAddressBook = prepareAddressBookList(
            emptyAddressBook, addressBookWithAmy, addressBookWithBob);

        assertFalse(versionedAddressBook.canRedo());
    }

    @Test
    public void undo_multipleAddressBookPointerAtEndOfStateList_success() {
        VersionedTaskCollection versionedAddressBook = prepareAddressBookList(
            emptyAddressBook, addressBookWithAmy, addressBookWithBob);

        versionedAddressBook.undo();
        assertAddressBookListStatus(versionedAddressBook,
            Collections.singletonList(emptyAddressBook),
            addressBookWithAmy,
            Collections.singletonList(addressBookWithBob));
    }

    @Test
    public void undo_multipleAddressBookPointerNotAtStartOfStateList_success() {
        VersionedTaskCollection versionedAddressBook = prepareAddressBookList(
            emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedAddressBook, 1);

        versionedAddressBook.undo();
        assertAddressBookListStatus(versionedAddressBook,
            Collections.emptyList(),
            emptyAddressBook,
            Arrays.asList(addressBookWithAmy, addressBookWithBob));
    }

    @Test
    public void undo_singleAddressBook_throwsNoUndoableStateException() {
        VersionedTaskCollection versionedAddressBook = prepareAddressBookList(emptyAddressBook);

        assertThrows(VersionedTaskCollection.NoUndoableStateException.class,
            versionedAddressBook::undo);
    }

    @Test
    public void undo_multipleAddressBookPointerAtStartOfStateList_throwsNoUndoableStateException() {
        VersionedTaskCollection versionedAddressBook = prepareAddressBookList(
            emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedAddressBook, 2);

        assertThrows(VersionedTaskCollection.NoUndoableStateException.class,
            versionedAddressBook::undo);
    }

    @Test
    public void redo_multipleAddressBookPointerNotAtEndOfStateList_success() {
        VersionedTaskCollection versionedAddressBook = prepareAddressBookList(
            emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedAddressBook, 1);

        versionedAddressBook.redo();
        assertAddressBookListStatus(versionedAddressBook,
            Arrays.asList(emptyAddressBook, addressBookWithAmy),
            addressBookWithBob,
            Collections.emptyList());
    }

    @Test
    public void redo_multipleAddressBookPointerAtStartOfStateList_success() {
        VersionedTaskCollection versionedAddressBook = prepareAddressBookList(
            emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedAddressBook, 2);

        versionedAddressBook.redo();
        assertAddressBookListStatus(versionedAddressBook,
            Collections.singletonList(emptyAddressBook),
            addressBookWithAmy,
            Collections.singletonList(addressBookWithBob));
    }

    @Test
    public void redo_singleAddressBook_throwsNoRedoableStateException() {
        VersionedTaskCollection versionedAddressBook = prepareAddressBookList(emptyAddressBook);

        assertThrows(VersionedTaskCollection.NoRedoableStateException.class,
            versionedAddressBook::redo);
    }

    @Test
    public void redo_multipleAddressBookPointerAtEndOfStateList_throwsNoRedoableStateException() {
        VersionedTaskCollection versionedAddressBook = prepareAddressBookList(
            emptyAddressBook, addressBookWithAmy, addressBookWithBob);

        assertThrows(VersionedTaskCollection.NoRedoableStateException.class,
            versionedAddressBook::redo);
    }

    @Test
    public void equals() {
        VersionedTaskCollection versionedAddressBook = prepareAddressBookList(addressBookWithAmy,
            addressBookWithBob);

        // same values -> returns true
        VersionedTaskCollection copy = prepareAddressBookList(addressBookWithAmy, addressBookWithBob);
        assertTrue(versionedAddressBook.equals(copy));

        // same object -> returns true
        assertTrue(versionedAddressBook.equals(versionedAddressBook));

        // null -> returns false
        assertFalse(versionedAddressBook.equals(null));

        // different types -> returns false
        assertFalse(versionedAddressBook.equals(1));

        // different state list -> returns false
        VersionedTaskCollection differentAddressBookList = prepareAddressBookList(addressBookWithBob,
            addressBookWithCarl);
        assertFalse(versionedAddressBook.equals(differentAddressBookList));

        // different current pointer index -> returns false
        VersionedTaskCollection differentCurrentStatePointer = prepareAddressBookList(
            addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedAddressBook, 1);
        assertFalse(versionedAddressBook.equals(differentCurrentStatePointer));
    }

    /**
     * Asserts that {@code versionedAddressBook} is currently pointing at {@code
     * expectedCurrentState}, states before {@code versionedAddressBook#currentStatePointer} is
     * equal to {@code expectedStatesBeforePointer}, and states after {@code
     * versionedAddressBook#currentStatePointer} is equal to {@code expectedStatesAfterPointer}.
     */
    private void assertAddressBookListStatus(VersionedTaskCollection versionedAddressBook,
                                             List<ReadOnlyTaskCollection> expectedStatesBeforePointer,
                                             ReadOnlyTaskCollection expectedCurrentState,
                                             List<ReadOnlyTaskCollection> expectedStatesAfterPointer) {
        // check state currently pointing at is correct
        assertEquals(new TaskCollection(versionedAddressBook), expectedCurrentState);

        // shift pointer to start of state list
        while (versionedAddressBook.canUndo()) {
            versionedAddressBook.undo();
        }

        // check states before pointer are correct
        for (ReadOnlyTaskCollection expectedAddressBook : expectedStatesBeforePointer) {
            assertEquals(expectedAddressBook, new TaskCollection(versionedAddressBook));
            versionedAddressBook.redo();
        }

        // check states after pointer are correct
        for (ReadOnlyTaskCollection expectedAddressBook : expectedStatesAfterPointer) {
            versionedAddressBook.redo();
            assertEquals(expectedAddressBook, new TaskCollection(versionedAddressBook));
        }

        // check that there are no more states after pointer
        assertFalse(versionedAddressBook.canRedo());

        // revert pointer to original position
        expectedStatesAfterPointer.forEach(unused -> versionedAddressBook.undo());
    }

    /**
     * Creates and returns a {@code VersionedTaskCollection} with the {@code addressBookStates} added
     * into it, and the {@code VersionedTaskCollection#currentStatePointer} at the end of list.
     */
    private VersionedTaskCollection prepareAddressBookList(ReadOnlyTaskCollection... addressBookStates) {
        assertFalse(addressBookStates.length == 0);

        VersionedTaskCollection versionedAddressBook = new VersionedTaskCollection(addressBookStates[0]);
        for (int i = 1; i < addressBookStates.length; i++) {
            versionedAddressBook.resetData(addressBookStates[i]);
            versionedAddressBook.commit();
        }

        return versionedAddressBook;
    }

    /**
     * Shifts the {@code versionedAddressBook#currentStatePointer} by {@code count} to the left of
     * its list.
     */
    private void shiftCurrentStatePointerLeftwards(VersionedTaskCollection versionedAddressBook,
                                                   int count) {
        for (int i = 0; i < count; i++) {
            versionedAddressBook.undo();
        }
    }
}
