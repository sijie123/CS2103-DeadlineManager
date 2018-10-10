package seedu.address.ui.testutil;

import static org.junit.Assert.assertEquals;
import static seedu.address.ui.TaskCard.DEADLINE_FORMAT;
import static seedu.address.ui.TaskCard.PRIORITY_FORMAT;

import java.util.List;
import java.util.stream.Collectors;

import guitests.guihandles.ResultDisplayHandle;
import guitests.guihandles.TaskCardHandle;
import guitests.guihandles.TaskListPanelHandle;
import seedu.address.model.task.Task;

/**
 * A set of assertion methods useful for writing GUI tests.
 */
public class GuiTestAssert {

    /**
     * Asserts that {@code actualCard} displays the same values as {@code expectedCard}.
     */
    public static void assertCardEquals(TaskCardHandle expectedCard,
                                        TaskCardHandle actualCard) {
        assertEquals(expectedCard.getId(), actualCard.getId());
        assertEquals(expectedCard.getName(), actualCard.getName());
        assertEquals(expectedCard.getTags(), actualCard.getTags());
    }

    /**
     * Asserts that {@code actualCard} displays the details of {@code expectedTask}.
     */
    public static void assertCardDisplaysTask(Task expectedTask, TaskCardHandle actualCard) {
        assertEquals(expectedTask.getName().value, actualCard.getName());
        String expectedPriority = String.format(PRIORITY_FORMAT, expectedTask.getPriority().value);
        assertEquals(expectedPriority, actualCard.getPriority());
        String expectedDeadline = String.format(DEADLINE_FORMAT, expectedTask.getDeadline().toString());
        assertEquals(expectedDeadline, actualCard.getDeadline());
        assertEquals(expectedTask.getTags().stream().map(tag -> tag.tagName)
                .collect(Collectors.toList()),
            actualCard.getTags());
        assertEquals(expectedTask.getAttachments().stream().map(attachment -> attachment.getName())
                .collect(Collectors.toList()),
            actualCard.getAttachments());
    }

    /**
     * Asserts that the list in {@code taskListPanelHandle} displays the details of {@code tasks}
     * correctly and in the correct order.
     */
    public static void assertListMatching(TaskListPanelHandle taskListPanelHandle,
                                          Task... tasks) {
        for (int i = 0; i < tasks.length; i++) {
            taskListPanelHandle.navigateToCard(i);
            assertCardDisplaysTask(tasks[i], taskListPanelHandle.getPersonCardHandle(i));
        }
    }

    /**
     * Asserts that the list in {@code taskListPanelHandle} displays the details of {@code tasks}
     * correctly and in the correct order.
     */
    public static void assertListMatching(TaskListPanelHandle taskListPanelHandle,
                                          List<Task> tasks) {
        assertListMatching(taskListPanelHandle, tasks.toArray(new Task[0]));
    }

    /**
     * Asserts the size of the list in {@code taskListPanelHandle} equals to {@code size}.
     */
    public static void assertListSize(TaskListPanelHandle taskListPanelHandle, int size) {
        int numberOfPeople = taskListPanelHandle.getListSize();
        assertEquals(size, numberOfPeople);
    }

    /**
     * Asserts the message shown in {@code resultDisplayHandle} equals to {@code expected}.
     */
    public static void assertResultMessage(ResultDisplayHandle resultDisplayHandle,
                                           String expected) {
        assertEquals(expected, resultDisplayHandle.getText());
    }
}
