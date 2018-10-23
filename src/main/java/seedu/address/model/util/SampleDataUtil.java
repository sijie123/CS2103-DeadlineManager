package seedu.address.model.util;

import java.io.File;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.ReadOnlyTaskCollection;
import seedu.address.model.TaskCollection;
import seedu.address.model.attachment.Attachment;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Frequency;
import seedu.address.model.task.Name;
import seedu.address.model.task.Priority;
import seedu.address.model.task.Task;

/**
 * Contains utility methods for populating {@code TaskCollection} with sample data.
 */
public class SampleDataUtil {

    public static Task[] getSampleTasks() {
        return new Task[] {
            new Task(new Name("CS2101 Oral Presentation"),
                new Priority("1"),
                new Frequency("5"),
                new Deadline("12/10/2018"),
                getTagSet("project", "cs2101")),
            new Task(new Name("CS2106 Lab 4"),
                new Priority("2"),
                new Frequency("10"),
                new Deadline("25/10/2018"),
                getTagSet("programming", "easy", "cs2106")),
            new Task(new Name("CS2103T Tutorial"),
                new Priority("3"),
                new Frequency("15"),
                new Deadline("10/10/2018"),
                getTagSet("cs2103t")),
            new Task(new Name("Dinner at MBS"),
                new Priority("3"),
                new Frequency("20"),
                new Deadline("27/10/2018"),
                getTagSet("family")),
            new Task(new Name("PS4"),
                new Priority("2"),
                new Frequency("25"),
                new Deadline("12/10/2018"),
                getTagSet("cs2040c")),
            new Task(new Name("Badminton"),
                new Priority("3"),
                new Frequency("30"),
                new Deadline("12/10/2018"),
                getTagSet("friends"))
        };
    }

    public static ReadOnlyTaskCollection getSampleTaskCollection() {
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
