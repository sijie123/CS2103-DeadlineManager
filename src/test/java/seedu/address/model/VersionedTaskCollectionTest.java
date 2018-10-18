package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalTasks.AMY;
import static seedu.address.testutil.TypicalTasks.BOB;
import static seedu.address.testutil.TypicalTasks.CARL;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.address.testutil.TaskManagerBuilder;

public class VersionedTaskCollectionTest {

    private final ReadOnlyTaskCollection taskCollectionsWithAmy = new TaskManagerBuilder().withPerson(AMY)
        .build();
    private final ReadOnlyTaskCollection taskCollectionWithBob = new TaskManagerBuilder().withPerson(BOB)
        .build();
    private final ReadOnlyTaskCollection taskCollectionWithCarl = new TaskManagerBuilder()
        .withPerson(CARL).build();
    private final ReadOnlyTaskCollection emptyTaskCollection = new TaskManagerBuilder().build();

    @Test
    public void commit_singleTaskCollection_noStatesRemovedCurrentStateSaved() {
        VersionedTaskCollection versionedTaskCollection = prepareTaskCollectionList(emptyTaskCollection);

        versionedTaskCollection.commit();
        assertTaskCollectionListStatus(versionedTaskCollection,
            Collections.singletonList(emptyTaskCollection),
            emptyTaskCollection,
            Collections.emptyList());
    }

    @Test
    public void commit_multipleTaskCollectionPointerAtEndOfStateList_noStatesRemovedCurrentStateSaved() {
        VersionedTaskCollection versionedTaskCollection = prepareTaskCollectionList(
            emptyTaskCollection, taskCollectionsWithAmy, taskCollectionWithBob);

        versionedTaskCollection.commit();
        assertTaskCollectionListStatus(versionedTaskCollection,
            Arrays.asList(emptyTaskCollection, taskCollectionsWithAmy, taskCollectionWithBob),
            taskCollectionWithBob,
            Collections.emptyList());
    }

    @Test
    public void commit_multipleTaskCollectionPointerNotAtEndOfStateList_statesAfterPointerRemovedCurrentStateSaved() {
        VersionedTaskCollection versionedTaskCollection = prepareTaskCollectionList(
            emptyTaskCollection, taskCollectionsWithAmy, taskCollectionWithBob);
        shiftCurrentStatePointerLeftwards(versionedTaskCollection, 2);

        versionedTaskCollection.commit();
        assertTaskCollectionListStatus(versionedTaskCollection,
            Collections.singletonList(emptyTaskCollection),
            emptyTaskCollection,
            Collections.emptyList());
    }

    @Test
    public void canUndo_multipleTaskCollectionPointerAtEndOfStateList_returnsTrue() {
        VersionedTaskCollection versionedTaskCollection = prepareTaskCollectionList(
            emptyTaskCollection, taskCollectionsWithAmy, taskCollectionWithBob);

        assertTrue(versionedTaskCollection.canUndo());
    }

    @Test
    public void canUndo_multipleTaskCollectionPointerAtStartOfStateList_returnsTrue() {
        VersionedTaskCollection versionedTaskCollection = prepareTaskCollectionList(
            emptyTaskCollection, taskCollectionsWithAmy, taskCollectionWithBob);
        shiftCurrentStatePointerLeftwards(versionedTaskCollection, 1);

        assertTrue(versionedTaskCollection.canUndo());
    }

    @Test
    public void canUndo_singleTaskCollection_returnsFalse() {
        VersionedTaskCollection versionedTaskCollection = prepareTaskCollectionList(emptyTaskCollection);

        assertFalse(versionedTaskCollection.canUndo());
    }

    @Test
    public void canUndo_multipleTaskCollectionPointerAtStartOfStateList_returnsFalse() {
        VersionedTaskCollection versionedTaskCollection = prepareTaskCollectionList(
            emptyTaskCollection, taskCollectionsWithAmy, taskCollectionWithBob);
        shiftCurrentStatePointerLeftwards(versionedTaskCollection, 2);

        assertFalse(versionedTaskCollection.canUndo());
    }

    @Test
    public void canRedo_multipleTaskCollectionPointerNotAtEndOfStateList_returnsTrue() {
        VersionedTaskCollection versionedTaskCollection = prepareTaskCollectionList(
            emptyTaskCollection, taskCollectionsWithAmy, taskCollectionWithBob);
        shiftCurrentStatePointerLeftwards(versionedTaskCollection, 1);

        assertTrue(versionedTaskCollection.canRedo());
    }

    @Test
    public void canRedo_multipleTaskCollectionPointerAtStartOfStateList_returnsTrue() {
        VersionedTaskCollection versionedTaskCollection = prepareTaskCollectionList(
            emptyTaskCollection, taskCollectionsWithAmy, taskCollectionWithBob);
        shiftCurrentStatePointerLeftwards(versionedTaskCollection, 2);

        assertTrue(versionedTaskCollection.canRedo());
    }

    @Test
    public void canRedo_singleTaskCollection_returnsFalse() {
        VersionedTaskCollection versionedTaskCollection = prepareTaskCollectionList(emptyTaskCollection);

        assertFalse(versionedTaskCollection.canRedo());
    }

    @Test
    public void canRedo_multipleTaskCollectionPointerAtEndOfStateList_returnsFalse() {
        VersionedTaskCollection versionedTaskCollection = prepareTaskCollectionList(
            emptyTaskCollection, taskCollectionsWithAmy, taskCollectionWithBob);

        assertFalse(versionedTaskCollection.canRedo());
    }

    @Test
    public void undo_multipleTaskCollectionPointerAtEndOfStateList_success() {
        VersionedTaskCollection versionedTaskCollection = prepareTaskCollectionList(
            emptyTaskCollection, taskCollectionsWithAmy, taskCollectionWithBob);

        versionedTaskCollection.undo();
        assertTaskCollectionListStatus(versionedTaskCollection,
            Collections.singletonList(emptyTaskCollection),
            taskCollectionsWithAmy,
            Collections.singletonList(taskCollectionWithBob));
    }

    @Test
    public void undo_multipleTaskCollectionPointerNotAtStartOfStateList_success() {
        VersionedTaskCollection versionedTaskCollection = prepareTaskCollectionList(
            emptyTaskCollection, taskCollectionsWithAmy, taskCollectionWithBob);
        shiftCurrentStatePointerLeftwards(versionedTaskCollection, 1);

        versionedTaskCollection.undo();
        assertTaskCollectionListStatus(versionedTaskCollection,
            Collections.emptyList(),
            emptyTaskCollection,
            Arrays.asList(taskCollectionsWithAmy, taskCollectionWithBob));
    }

    @Test
    public void undo_singleTaskCollection_throwsNoUndoableStateException() {
        VersionedTaskCollection versionedTaskCollection = prepareTaskCollectionList(emptyTaskCollection);

        assertThrows(VersionedTaskCollection.NoUndoableStateException.class,
            versionedTaskCollection::undo);
    }

    @Test
    public void undo_multipleTaskCollectionPointerAtStartOfStateList_throwsNoUndoableStateException() {
        VersionedTaskCollection versionedTaskCollection = prepareTaskCollectionList(
            emptyTaskCollection, taskCollectionsWithAmy, taskCollectionWithBob);
        shiftCurrentStatePointerLeftwards(versionedTaskCollection, 2);

        assertThrows(VersionedTaskCollection.NoUndoableStateException.class,
            versionedTaskCollection::undo);
    }

    @Test
    public void redo_multipleTaskCollectionPointerNotAtEndOfStateList_success() {
        VersionedTaskCollection versionedTaskCollection = prepareTaskCollectionList(
            emptyTaskCollection, taskCollectionsWithAmy, taskCollectionWithBob);
        shiftCurrentStatePointerLeftwards(versionedTaskCollection, 1);

        versionedTaskCollection.redo();
        assertTaskCollectionListStatus(versionedTaskCollection,
            Arrays.asList(emptyTaskCollection, taskCollectionsWithAmy),
            taskCollectionWithBob,
            Collections.emptyList());
    }

    @Test
    public void redo_multipleTaskCollectionPointerAtStartOfStateList_success() {
        VersionedTaskCollection versionedTaskCollection = prepareTaskCollectionList(
            emptyTaskCollection, taskCollectionsWithAmy, taskCollectionWithBob);
        shiftCurrentStatePointerLeftwards(versionedTaskCollection, 2);

        versionedTaskCollection.redo();
        assertTaskCollectionListStatus(versionedTaskCollection,
            Collections.singletonList(emptyTaskCollection),
            taskCollectionsWithAmy,
            Collections.singletonList(taskCollectionWithBob));
    }

    @Test
    public void redo_singleTaskCollection_throwsNoRedoableStateException() {
        VersionedTaskCollection versionedTaskCollection = prepareTaskCollectionList(emptyTaskCollection);

        assertThrows(VersionedTaskCollection.NoRedoableStateException.class,
            versionedTaskCollection::redo);
    }

    @Test
    public void redo_multipleTaskCollectionPointerAtEndOfStateList_throwsNoRedoableStateException() {
        VersionedTaskCollection versionedTaskCollection = prepareTaskCollectionList(
            emptyTaskCollection, taskCollectionsWithAmy, taskCollectionWithBob);

        assertThrows(VersionedTaskCollection.NoRedoableStateException.class,
            versionedTaskCollection::redo);
    }

    @Test
    public void equals() {
        VersionedTaskCollection versionedTaskCollection = prepareTaskCollectionList(taskCollectionsWithAmy,
            taskCollectionWithBob);

        // same values -> returns true
        VersionedTaskCollection copy = prepareTaskCollectionList(taskCollectionsWithAmy, taskCollectionWithBob);
        assertTrue(versionedTaskCollection.equals(copy));

        // same object -> returns true
        assertTrue(versionedTaskCollection.equals(versionedTaskCollection));

        // null -> returns false
        assertFalse(versionedTaskCollection.equals(null));

        // different types -> returns false
        assertFalse(versionedTaskCollection.equals(1));

        // different state list -> returns false
        VersionedTaskCollection differentTaskCollectionList = prepareTaskCollectionList(taskCollectionWithBob,
            taskCollectionWithCarl);
        assertFalse(versionedTaskCollection.equals(differentTaskCollectionList));

        // different current pointer index -> returns false
        VersionedTaskCollection differentCurrentStatePointer = prepareTaskCollectionList(
            taskCollectionsWithAmy, taskCollectionWithBob);
        shiftCurrentStatePointerLeftwards(versionedTaskCollection, 1);
        assertFalse(versionedTaskCollection.equals(differentCurrentStatePointer));
    }

    /**
     * Asserts that {@code versionedTaskCollection} is currently pointing at {@code
     * expectedCurrentState}, states before {@code versionedTaskCollection#currentStatePointer} is
     * equal to {@code expectedStatesBeforePointer}, and states after {@code
     * versionedTaskCollection#currentStatePointer} is equal to {@code expectedStatesAfterPointer}.
     */
    private void assertTaskCollectionListStatus(VersionedTaskCollection versionedTaskCollection,
                                             List<ReadOnlyTaskCollection> expectedStatesBeforePointer,
                                             ReadOnlyTaskCollection expectedCurrentState,
                                             List<ReadOnlyTaskCollection> expectedStatesAfterPointer) {
        // check state currently pointing at is correct
        assertEquals(new TaskCollection(versionedTaskCollection), expectedCurrentState);

        // shift pointer to start of state list
        while (versionedTaskCollection.canUndo()) {
            versionedTaskCollection.undo();
        }

        // check states before pointer are correct
        for (ReadOnlyTaskCollection expectedTaskCollection : expectedStatesBeforePointer) {
            assertEquals(expectedTaskCollection, new TaskCollection(versionedTaskCollection));
            versionedTaskCollection.redo();
        }

        // check states after pointer are correct
        for (ReadOnlyTaskCollection expectedTaskCollection : expectedStatesAfterPointer) {
            versionedTaskCollection.redo();
            assertEquals(expectedTaskCollection, new TaskCollection(versionedTaskCollection));
        }

        // check that there are no more states after pointer
        assertFalse(versionedTaskCollection.canRedo());

        // revert pointer to original position
        expectedStatesAfterPointer.forEach(unused -> versionedTaskCollection.undo());
    }

    /**
     * Creates and returns a {@code VersionedTaskCollection} with the {@code addressBookStates} added
     * into it, and the {@code VersionedTaskCollection#currentStatePointer} at the end of list.
     */
    private VersionedTaskCollection prepareTaskCollectionList(ReadOnlyTaskCollection... addressBookStates) {
        assertFalse(addressBookStates.length == 0);

        VersionedTaskCollection versionedTaskCollection = new VersionedTaskCollection(addressBookStates[0]);
        for (int i = 1; i < addressBookStates.length; i++) {
            versionedTaskCollection.resetData(addressBookStates[i]);
            versionedTaskCollection.commit();
        }

        return versionedTaskCollection;
    }

    /**
     * Shifts the {@code versionedTaskCollection#currentStatePointer} by {@code count} to the left of
     * its list.
     */
    private void shiftCurrentStatePointerLeftwards(VersionedTaskCollection versionedTaskCollection,
                                                   int count) {
        for (int i = 0; i < count; i++) {
            versionedTaskCollection.undo();
        }
    }
}
