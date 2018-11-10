package seedu.address.model.task;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.attachment.Attachment;
import seedu.address.model.tag.Tag;

/**
 * Represents a Task in the deadline manager. Guarantees: details are present and not null, field values
 * are validated, immutable.
 */
public class Task {
    private final Name name;
    private final Priority priority;
    private final Frequency frequency;
    private final Deadline deadline;
    private final Set<Tag> tags = new HashSet<>();
    private final Set<Attachment> attachments = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Task(Name name, Priority priority, Frequency frequency, Deadline deadline,
            Set<Tag> tags, Set<Attachment> attachments) {
        requireAllNonNull(name, priority, deadline, tags, attachments);
        this.name = name;
        this.priority = priority;
        this.frequency = frequency;
        this.deadline = deadline;
        this.tags.addAll(tags);
        this.attachments.addAll(attachments);
    }

    /**
     * Convenience constructor. Tasks are initialized without any attachments.
     */
    public Task(Name name, Priority priority, Frequency frequency, Deadline deadline, Set<Tag> tags) {
        this(name, priority, frequency, deadline, tags, new HashSet<>());
    }

    /**
     * Convenience constructor, to be removed eventually
     */
    public Task(Name name, Priority priority, Frequency frequency, Set<Tag> tags) {
        this(name, priority, frequency, new Deadline("1/10/2018"), tags);
    }

    public Name getName() {
        return name;
    }

    public Priority getPriority() {
        return priority;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public Deadline getDeadline() {
        return deadline;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException} if
     * modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns an immutable attachment set, which throws {@code UnsupportedOperationException} if
     * modification is attempted.
     */
    public Set<Attachment> getAttachments() {
        return Collections.unmodifiableSet(attachments);
    }


    /**
     * Returns true if both tasks have the same identity and data fields. This defines a stronger
     * notion of equality between two tasks.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Task)) {
            return false;
        }
        Task otherTask = (Task) other;
        return otherTask.getName().equals(getName())
                && otherTask.getPriority().equals(getPriority())
                && otherTask.getFrequency().equals(getFrequency())
                && otherTask.getTags().equals(getTags())
                && otherTask.getDeadline().equals(getDeadline())
                && otherTask.getAttachments().equals(getAttachments());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, priority, frequency, tags, deadline, attachments);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Priority: ")
                .append(getPriority())
                .append(" Frequency: ")
                .append(getFrequency())
                .append(" Deadline: ")
                .append(getDeadline())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        builder.append(" Attachments: ");
        getAttachments().forEach(builder::append);
        return builder.toString();
    }

}
