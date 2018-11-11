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
            new Task(new Name("CS2103T Product Demo"),
                new Priority("1"),
                new Frequency("0"),
                new Deadline("14/11/2018"),
                getTagSet("presentation", "cs2103t")),
            new Task(new Name("Make presentation slides"),
                new Priority("1"),
                new Frequency("7"),
                new Deadline("12/11/2018"),
                getTagSet("cs1010")),
            new Task(new Name("Make presentation slides"),
                new Priority("1"),
                new Frequency("7"),
                new Deadline("14/11/2018"),
                getTagSet("cs2101")),
            new Task(new Name("Present gifts for Community Service Project"),
                new Priority("2"),
                new Frequency("0"),
                new Deadline("15/11/2018"),
                getTagSet("CSP", "volunteer")),
            new Task(new Name("Buy slide cover for 3D printer"),
                new Priority("3"),
                new Frequency("0"),
                new Deadline("23/11/2018"),
                getTagSet("3dprinting", "hobby")),
            new Task(new Name("CS2101 Meeting"),
                new Priority("1"),
                new Frequency("0"),
                new Deadline("16/11/2018"),
                getTagSet("meeting", "cs2101")),
            new Task(new Name("CS2106 Lab 4"),
                new Priority("2"),
                new Frequency("10"),
                new Deadline("25/11/2018"),
                getTagSet("programming", "easy", "cs2106")),
            new Task(new Name("CS2103T Tutorial"),
                new Priority("3"),
                new Frequency("15"),
                new Deadline("10/11/2018"),
                getTagSet("cs2103t"),
                getAttachmentSet("powerpoint presentation.pptx")),
            new Task(new Name("CS focus area briefing"),
                new Priority("3"),
                new Frequency("0"),
                new Deadline("18/11/2018"),
                getTagSet("cs", "briefing")),
            new Task(new Name("Do homework for MA1101R"),
                new Priority("2"),
                new Frequency("10"),
                new Deadline("12/11/2018"),
                getTagSet("cs", "briefing")),
            new Task(new Name("Make project portfolio page"),
                new Priority("1"),
                new Frequency("0"),
                new Deadline("12/11/2018"),
                getTagSet("cs2103t", "cs2101")),
            new Task(new Name("Christmas dinner at MBS"),
                new Priority("3"),
                new Frequency("20"),
                new Deadline("25/12/2018"),
                getTagSet("family")),
            new Task(new Name("Semester 2 starts"),
                new Priority("2"),
                new Frequency("0"),
                new Deadline("14/1/2019"),
                getTagSet()),
            new Task(new Name("PS4"),
                new Priority("2"),
                new Frequency("0"),
                new Deadline("12/10/2018"),
                getTagSet("cs2040c")),
            new Task(new Name("Badminton"),
                new Priority("3"),
                new Frequency("30"),
                new Deadline("12/11/2018"),
                getTagSet("friends")),
            new Task(new Name("STePS showcase"),
                new Priority("1"),
                new Frequency("0"),
                new Deadline("14/11/2018"),
                getTagSet("friends", "cs")),
            new Task(new Name("Write presentation notes"),
                new Priority("1"),
                new Frequency("7"),
                new Deadline("21/11/2018"),
                getTagSet("cs2101")),
            new Task(new Name("Weekly swim"),
                new Priority("3"),
                new Frequency("7"),
                new Deadline("13/11/2018"),
                getTagSet("friends")),
            new Task(new Name("CS2106 lab assignment"),
                new Priority("2"),
                new Frequency("14"),
                new Deadline("19/11/2018"),
                getTagSet("cs2106", "homework", "lab")),
            new Task(new Name("GEQ1000 design quiz"),
                new Priority("4"),
                new Frequency("0"),
                new Deadline("11/11/2018"),
                getTagSet("geq1000"))
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
