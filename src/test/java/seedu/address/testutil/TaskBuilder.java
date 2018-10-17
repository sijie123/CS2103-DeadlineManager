package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.attachment.Attachment;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Name;
import seedu.address.model.task.Priority;
import seedu.address.model.task.Task;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Task objects.
 */
public class TaskBuilder {

    public static final String DEFAULT_NAME = "Alice Pauline";
    public static final String DEFAULT_DEADLINE = "1/10/2018";
    public static final String DEFAULT_PRIORITY = "2";

    private Name name;
    private Priority priority;
    private Deadline deadline;
    private Set<Tag> tags;
    private Set<Attachment> attachments;

    public TaskBuilder() {
        name = new Name(DEFAULT_NAME);
        priority = new Priority(DEFAULT_PRIORITY);
        deadline = new Deadline(DEFAULT_DEADLINE);
        tags = new HashSet<>();
        attachments = new HashSet<>();
    }

    /**
     * Initializes the TaskBuilder with the data of {@code taskToCopy}.
     */
    public TaskBuilder(Task taskToCopy) {
        name = taskToCopy.getName();
        priority = taskToCopy.getPriority();
        deadline = taskToCopy.getDeadline();
        tags = new HashSet<>(taskToCopy.getTags());
        attachments = new HashSet<>(taskToCopy.getAttachments());
    }

    /**
     * Sets the {@code Name} of the {@code Task} that we are building.
     */
    public TaskBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Task} that we are
     * building.
     */
    public TaskBuilder withTags(String... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Deadline} of the {@code Task} that we are building.
     */
    public TaskBuilder withDeadline(String deadline) {
        this.deadline = new Deadline(deadline);
        return this;
    }

    /**
     * Sets the {@code Priority} of the {@code Task} that we are building.
     */
    public TaskBuilder withPriority(String priority) {
        this.priority = new Priority(priority);
        return this;
    }

    /**
     * Parses the {@code attachments} into a {@code Set<Attachment>} and set it to the {@code Task} that we are
     * building.
     */
    public TaskBuilder withAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
        return this;
    }

    /**
     * Parses the {@code attachments} into a {@code Set<Attachment>} and set it to the {@code Task} that we are
     * building.
     */
    public TaskBuilder withAttachments(String... attachments) {
        this.attachments = SampleDataUtil.getAttachmentSet(attachments);
        return this;
    }

    public Task build() {
        return new Task(name, priority, deadline, tags, attachments);
    }

}
