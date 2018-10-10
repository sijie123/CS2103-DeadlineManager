package seedu.address.model.util;

import java.io.File;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.ReadOnlyTaskCollection;
import seedu.address.model.TaskCollection;
import seedu.address.model.attachment.Attachment;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Address;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Email;
import seedu.address.model.task.Name;
import seedu.address.model.task.Phone;
import seedu.address.model.task.Priority;
import seedu.address.model.task.Task;

/**
 * Contains utility methods for populating {@code TaskCollection} with sample data.
 */
public class SampleDataUtil {

    public static Task[] getSampleTasks() {
        return new Task[] {
            new Task(new Name("CS2101 Oral Presentation"), new Phone("87438807"), new Priority("1"),
                new Email("alexyeoh@example.com"),
                new Deadline("12/10/2018"),
                new Address("Blk 30 Geylang Street 29, #06-40"),
                getTagSet("project", "cs2101")),
            new Task(new Name("CS2106 Lab 4"), new Phone("99272758"), new Priority("2"),
                new Email("berniceyu@example.com"),
                new Deadline("25/10/2018"),
                new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                getTagSet("programming", "easy", "cs2106")),
            new Task(new Name("CS2103T Tutorial"), new Phone("93210283"), new Priority("3"),
                new Email("charlotte@example.com"),
                new Deadline("10/10/2018"),
                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                getTagSet("cs2103t")),
            new Task(new Name("Dinner at MBS"), new Phone("91031282"), new Priority("3"),
                new Email("lidavid@example.com"),
                new Deadline("27/10/2018"),
                new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                getTagSet("family")),
            new Task(new Name("PS4"), new Phone("92492021"), new Priority("2"),
                new Email("irfan@example.com"),
                new Deadline("12/10/2018"),
                new Address("Blk 47 Tampines Street 20, #17-35"),
                getTagSet("cs2040c")),
            new Task(new Name("Badminton"), new Phone("92624417"), new Priority("3"),
                new Email("royb@example.com"),
                new Deadline("12/10/2018"),
                new Address("Blk 45 Aljunied Street 85, #11-31"),
                getTagSet("friends"))
        };
    }

    public static ReadOnlyTaskCollection getSampleAddressBook() {
        TaskCollection sampleAb = new TaskCollection();
        for (Task sampleTask : getSampleTasks()) {
            sampleAb.addTask(sampleTask);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
            .map(Tag::new)
            .collect(Collectors.toSet());
    }


    /**
     * Returns a attachment set containing the list of attachments given.
     */
    public static Set<Attachment> getAttachmentSet(String... strings) {
        return Arrays.stream(strings)
            .map(File::new)
            .map(Attachment::new)
            .collect(Collectors.toSet());
    }
}
