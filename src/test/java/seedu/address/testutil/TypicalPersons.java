package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
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
        .withAddress("123, Jurong West Ave 6, #08-111")
        .withEmail("alice@example.com")
        .withPhone("94351253")
        .withPriority("1")
        .withTags("friends")
        .withAttachments("hello.txt", "world.txt")
        .build();
    public static final Task BENSON = new PersonBuilder()
        .withName("Benson Meier")
        .withAddress("311, Clementi Ave 2, #02-25")
        .withEmail("johnd@example.com")
        .withPhone("98765432")
        .withPriority("2")
        .withTags("owesMoney", "friends")
        .withAttachments("hello.txt", "world.txt")
        .build();
    public static final Task CARL = new PersonBuilder()
        .withName("Carl Kurz")
        .withPhone("95352563")
        .withPriority("3")
        .withEmail("heinz@example.com")
        .withAddress("wall street")
        .withAttachments("world.txt")
        .build();
    public static final Task DANIEL = new PersonBuilder().withName("Daniel Meier")
        .withPhone("87652533")
        .withPriority("4")
        .withEmail("cornelia@example.com")
        .withAddress("10th street")
        .withTags("friends")
        .build();
    public static final Task ELLE = new PersonBuilder()
        .withName("Elle Meyer")
        .withPhone("9482224")
        .withPriority("1")
        .withEmail("werner@example.com")
        .withAddress("michegan ave")
        .build();
    public static final Task FIONA = new PersonBuilder()
        .withName("Fiona Kunz")
        .withPhone("9482427")
        .withPriority("2")
        .withEmail("lydia@example.com")
        .withAddress("little tokyo")
        .withAttachments("hello.txt", "world.txt")
        .build();
    public static final Task GEORGE = new PersonBuilder()
        .withName("George Best")
        .withPhone("9482442")
        .withPriority("3")
        .withEmail("anna@example.com")
        .withAddress("4th street")
        .withAttachments("hello.txt")
        .build();

    // Manually added
    public static final Task HOON = new PersonBuilder()
        .withName("Hoon Meier")
        .withPhone("8482424")
        .withPriority("4")
        .withEmail("stefan@example.com")
        .withAddress("little india")
        .withAttachments("world.txt")
        .build();
    public static final Task IDA = new PersonBuilder()
        .withName("Ida Mueller")
        .withPhone("8482131")
        .withPriority("1")
        .withEmail("hans@example.com")
        .withAddress("chicago ave")
        .build();

    // Manually added - Task's details found in {@code CommandTestUtil}
    public static final Task AMY = new PersonBuilder()
        .withName(VALID_NAME_AMY)
        .withPhone(VALID_PHONE_AMY)
        .withPriority(VALID_PRIORITY_AMY)
        .withEmail(VALID_EMAIL_AMY)
        .withAddress(VALID_ADDRESS_AMY)
        .withTags(VALID_TAG_FRIEND)
        .build();
    public static final Task BOB = new PersonBuilder()
        .withName(VALID_NAME_BOB)
        .withPhone(VALID_PHONE_BOB)
        .withPriority(VALID_PRIORITY_BOB)
        .withEmail(VALID_EMAIL_BOB)
        .withAddress(VALID_ADDRESS_BOB)
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
            ab.addPerson(task);
        }
        return ab;
    }

    public static List<Task> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
