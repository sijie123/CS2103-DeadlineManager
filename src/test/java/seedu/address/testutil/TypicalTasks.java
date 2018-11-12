package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_DEADLINE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DEADLINE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_FREQUENCY_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_FREQUENCY_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRIORITY_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRIORITY_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.TaskCollection;
import seedu.address.model.task.Task;

/**
 * A utility class containing a list of {@code Task} objects to be used in tests.
 */
public class TypicalTasks {

    public static final Task ALICE = new TaskBuilder()
            .withName("Alice Pauline")
            .withDeadline("1/10/2018")
            .withPriority("1")
            .withFrequency("200")
            .withTags("friends")
            .withAttachments("hello.txt", "world.txt")
            .build();
    public static final Task BENSON = new TaskBuilder()
            .withName("Benson Meier")
            .withDeadline("1/11/2018")
            .withPriority("2")
            .withFrequency("205")
            .withTags("owesMoney", "friends")
            .withAttachments("hello.txt", "world.txt")
            .build();
    public static final Task CARL = new TaskBuilder()
            .withName("Carl Kurz")
            .withDeadline("31/10/2018")
            .withPriority("3")
            .withFrequency("210")
            .withAttachments("world.txt")
            .build();
    public static final Task DANIEL = new TaskBuilder()
            .withName("Daniel Meier")
            .withPriority("4")
            .withFrequency("215")
            .withDeadline("2/1/2019")
            .withTags("friends")
            .build();
    public static final Task ELLE = new TaskBuilder()
            .withName("Elle Meyer")
            .withPriority("1")
            .withFrequency("220")
            .withDeadline("2/10/2018")
            .build();
    public static final Task FIONA = new TaskBuilder()
            .withName("Fiona Kunz")
            .withPriority("2")
            .withFrequency("225")
            .withDeadline("2/10/2018")
            .withAttachments("hello.txt", "world.txt")
            .build();
    public static final Task GEORGE = new TaskBuilder()
            .withName("George Best")
            .withPriority("3")
            .withFrequency("230")
            .withDeadline("2/10/2018")
            .withAttachments("hello.txt")
            .build();

    // Manually added
    public static final Task HOON = new TaskBuilder()
            .withName("Hoon Meier")
            .withPriority("4")
            .withFrequency("55")
            .withDeadline("2/10/2018")
            .withAttachments("world.txt")
            .build();
    public static final Task IDA = new TaskBuilder()
            .withName("Ida Mueller")
            .withPriority("1")
            .withFrequency("60")
            .withDeadline("2/10/2018")
            .build();

    // Manually added - Task's details found in {@code CommandTestUtil}
    public static final Task AMY = new TaskBuilder()
            .withName(VALID_NAME_AMY)
            .withPriority(VALID_PRIORITY_AMY)
            .withFrequency(VALID_FREQUENCY_AMY)
            .withDeadline(VALID_DEADLINE_AMY)
            .withTags(VALID_TAG_FRIEND)
            .build();
    public static final Task BOB = new TaskBuilder()
            .withName(VALID_NAME_BOB)
            .withPriority(VALID_PRIORITY_BOB)
            .withFrequency(VALID_FREQUENCY_BOB)
            .withDeadline(VALID_DEADLINE_BOB)
            .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalTasks() {
    } // prevents instantiation

    /**
     * Returns an {@code TaskCollection} with all the typical tasks.
     */
    public static TaskCollection getTypicalTaskCollections() {
        TaskCollection ab = new TaskCollection();
        for (Task task : getTypicalTasks()) {
            ab.addTask(task);
        }
        return ab;
    }

    public static List<Task> getTypicalTasks() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
