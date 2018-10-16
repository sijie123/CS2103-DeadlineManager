package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_DEADLINE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DEADLINE_BOB;
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
public class TypicalPersons {

    public static final Task ALICE = new PersonBuilder()
        .withName("Alice Pauline")
        .withDeadline("1/10/2018")
        .withPriority("1")
        .withTags("friends")
        .withAttachments("hello.txt", "world.txt")
        .build();
    public static final Task BENSON = new PersonBuilder()
        .withName("Benson Meier")
        .withDeadline("1/11/2018")
        .withPriority("2")
        .withTags("owesMoney", "friends")
        .withAttachments("hello.txt", "world.txt")
        .build();
    public static final Task CARL = new PersonBuilder()
        .withName("Carl Kurz")
        .withDeadline("31/10/2018")
        .withPriority("3")
        .withAttachments("world.txt")
        .build();
    public static final Task DANIEL = new PersonBuilder()
        .withName("Daniel Meier")
        .withPriority("4")
        .withDeadline("2/1/2019")
        .withTags("friends")
        .build();
    public static final Task ELLE = new PersonBuilder()
        .withName("Elle Meyer")
        .withPriority("1")
        .withDeadline("2/10/2018")
        .build();
    public static final Task FIONA = new PersonBuilder()
        .withName("Fiona Kunz")
        .withPriority("2")
        .withDeadline("2/10/2018")
        .withAttachments("hello.txt", "world.txt")
        .build();
    public static final Task GEORGE = new PersonBuilder()
        .withName("George Best")
        .withPriority("3")
        .withDeadline("2/10/2018")
        .withAttachments("hello.txt")
        .build();

    // Manually added
    public static final Task HOON = new PersonBuilder()
        .withName("Hoon Meier")
        .withPriority("4")
        .withDeadline("2/10/2018")
        .withAttachments("world.txt")
        .build();
    public static final Task IDA = new PersonBuilder()
        .withName("Ida Mueller")
        .withPriority("1").withDeadline("2/10/2018")
        .build();

    // Manually added - Task's details found in {@code CommandTestUtil}
    public static final Task AMY = new PersonBuilder()
        .withName(VALID_NAME_AMY)
        .withPriority(VALID_PRIORITY_AMY)
        .withDeadline(VALID_DEADLINE_AMY)
        .withTags(VALID_TAG_FRIEND)
        .build();
    public static final Task BOB = new PersonBuilder()
        .withName(VALID_NAME_BOB)
        .withPriority(VALID_PRIORITY_BOB)
        .withDeadline(VALID_DEADLINE_BOB)
        .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
        .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {
    } // prevents instantiation

    /**
     * Returns an {@code TaskCollection} with all the typical persons.
     */
    public static TaskCollection getTypicalAddressBook() {
        TaskCollection ab = new TaskCollection();
        for (Task task : getTypicalPersons()) {
            ab.addTask(task);
        }
        return ab;
    }

    public static List<Task> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
