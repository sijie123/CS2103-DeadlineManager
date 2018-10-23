package seedu.address.model.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_FREQUENCY_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRIORITY_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.TypicalTasks.ALICE;
import static seedu.address.testutil.TypicalTasks.BOB;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.task.exceptions.DuplicateTaskException;
import seedu.address.model.task.exceptions.TaskNotFoundException;
import seedu.address.testutil.TaskBuilder;

public class UniqueTaskListTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final UniqueTaskList uniqueTaskList = new UniqueTaskList();

    @Test
    public void contains_nullTask_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueTaskList.contains(null);
    }

    @Test
    public void contains_taskNotInList_returnsFalse() {
        assertFalse(uniqueTaskList.contains(ALICE));
    }

    @Test
    public void contains_taskInList_returnsTrue() {
        uniqueTaskList.add(ALICE);
        assertTrue(uniqueTaskList.contains(ALICE));
    }

    @Test
    public void add_nullTask_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueTaskList.add(null);
    }

    @Test
    public void add_duplicateTask_throwsDuplicateTaskException() {
        uniqueTaskList.add(ALICE);
        thrown.expect(DuplicateTaskException.class);
        uniqueTaskList.add(ALICE);
    }

    @Test
    public void setTask_nullTargetTask_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueTaskList.setTask(null, ALICE);
    }

    @Test
    public void setTask_nullEditedTask_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueTaskList.setTask(ALICE, null);
    }

    @Test
    public void setTask_targetTaskNotInList_throwsTaskNotFoundException() {
        thrown.expect(TaskNotFoundException.class);
        uniqueTaskList.setTask(ALICE, ALICE);
    }

    @Test
    public void setTask_editedTaskIsSameTask_success() {
        uniqueTaskList.add(ALICE);
        uniqueTaskList.setTask(ALICE, ALICE);
        UniqueTaskList expectedUniqueTaskList = new UniqueTaskList();
        expectedUniqueTaskList.add(ALICE);
        assertEquals(expectedUniqueTaskList, uniqueTaskList);
    }

    @Test
    public void setTask_editedTaskHasSameIdentity_success() {
        uniqueTaskList.add(ALICE);
        Task editedAlice = new TaskBuilder(ALICE)
            .withPriority(VALID_PRIORITY_BOB)
            .withFrequency(VALID_FREQUENCY_BOB)
            .withTags(VALID_TAG_HUSBAND)
            .build();
        uniqueTaskList.setTask(ALICE, editedAlice);
        UniqueTaskList expectedUniqueTaskList = new UniqueTaskList();
        expectedUniqueTaskList.add(editedAlice);
        assertEquals(expectedUniqueTaskList, uniqueTaskList);
    }

    @Test
    public void setTask_editedTaskHasDifferentIdentity_success() {
        uniqueTaskList.add(ALICE);
        uniqueTaskList.setTask(ALICE, BOB);
        UniqueTaskList expectedUniqueTaskList = new UniqueTaskList();
        expectedUniqueTaskList.add(BOB);
        assertEquals(expectedUniqueTaskList, uniqueTaskList);
    }

    @Test
    public void setTask_editedTaskHasNonUniqueIdentity_throwsDuplicateTaskException() {
        uniqueTaskList.add(ALICE);
        uniqueTaskList.add(BOB);
        thrown.expect(DuplicateTaskException.class);
        uniqueTaskList.setTask(ALICE, BOB);
    }

    @Test
    public void remove_nullTask_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueTaskList.remove(null);
    }

    @Test
    public void remove_taskDoesNotExist_throwsTaskNotFoundException() {
        thrown.expect(TaskNotFoundException.class);
        uniqueTaskList.remove(ALICE);
    }

    @Test
    public void remove_existingTask_removesTask() {
        uniqueTaskList.add(ALICE);
        uniqueTaskList.remove(ALICE);
        UniqueTaskList expectedUniqueTaskList = new UniqueTaskList();
        assertEquals(expectedUniqueTaskList, uniqueTaskList);
    }

    @Test
    public void setTasks_nullUniqueTaskList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueTaskList.setTasks((UniqueTaskList) null);
    }

    @Test
    public void setTasks_uniqueTaskList_replacesOwnListWithProvidedUniqueTaskList() {
        uniqueTaskList.add(ALICE);
        UniqueTaskList expectedUniqueTaskList = new UniqueTaskList();
        expectedUniqueTaskList.add(BOB);
        uniqueTaskList.setTasks(expectedUniqueTaskList);
        assertEquals(expectedUniqueTaskList, uniqueTaskList);
    }

    @Test
    public void setTasks_nullList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueTaskList.setTasks((List<Task>) null);
    }

    @Test
    public void setTasks_list_replacesOwnListWithProvidedList() {
        uniqueTaskList.add(ALICE);
        List<Task> taskList = Collections.singletonList(BOB);
        uniqueTaskList.setTasks(taskList);
        UniqueTaskList expectedUniqueTaskList = new UniqueTaskList();
        expectedUniqueTaskList.add(BOB);
        assertEquals(expectedUniqueTaskList, uniqueTaskList);
    }

    @Test
    public void setTasks_listWithDuplicateTasks_throwsDuplicateTaskException() {
        List<Task> listWithDuplicateTasks = Arrays.asList(ALICE, ALICE);
        thrown.expect(DuplicateTaskException.class);
        uniqueTaskList.setTasks(listWithDuplicateTasks);
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        uniqueTaskList.asUnmodifiableObservableList().remove(0);
    }
}
