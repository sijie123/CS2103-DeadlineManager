package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysPerson;

import org.junit.Test;

import guitests.guihandles.TaskCardHandle;
import seedu.address.model.task.Task;
import seedu.address.testutil.PersonBuilder;

public class TaskCardTest extends GuiUnitTest {

    @Test
    public void display() {
        // no tags
        Task taskWithNoTags = new PersonBuilder().withTags().build();
        TaskCard taskCard = new TaskCard(taskWithNoTags, 1);
        uiPartRule.setUiPart(taskCard);
        assertCardDisplay(taskCard, taskWithNoTags, 1);

        // with tags
        Task taskWithTags = new PersonBuilder().build();
        taskCard = new TaskCard(taskWithTags, 2);
        uiPartRule.setUiPart(taskCard);
        assertCardDisplay(taskCard, taskWithTags, 2);
    }

    @Test
    public void equals() {
        Task task = new PersonBuilder().build();
        TaskCard taskCard = new TaskCard(task, 0);

        // same task, same index -> returns true
        TaskCard copy = new TaskCard(task, 0);
        assertTrue(taskCard.equals(copy));

        // same object -> returns true
        assertTrue(taskCard.equals(taskCard));

        // null -> returns false
        assertFalse(taskCard.equals(null));

        // different types -> returns false
        assertFalse(taskCard.equals(0));

        // different task, same index -> returns false
        Task differentTask = new PersonBuilder().withName("differentName").build();
        assertFalse(taskCard.equals(new TaskCard(differentTask, 0)));

        // same task, different index -> returns false
        assertFalse(taskCard.equals(new TaskCard(task, 1)));
    }

    /**
     * Asserts that {@code taskCard} displays the details of {@code expectedTask} correctly and
     * matches {@code expectedId}.
     */
    private void assertCardDisplay(TaskCard taskCard, Task expectedTask, int expectedId) {
        guiRobot.pauseForHuman();

        TaskCardHandle taskCardHandle = new TaskCardHandle(taskCard.getRoot());

        // verify id is displayed correctly
        assertEquals(Integer.toString(expectedId) + ". ", taskCardHandle.getId());

        // verify task details are displayed correctly
        assertCardDisplaysPerson(expectedTask, taskCardHandle);
    }
}
