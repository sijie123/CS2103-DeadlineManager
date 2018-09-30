package seedu.address.model.task;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.Date;
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

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Deadline deadline;
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();
    private final Set<Attachment> attachments = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Task(Name name, Phone phone, Email email, Deadline deadline, Address address,
            Set<Tag> tags, Set<Attachment> attachments) {
        requireAllNonNull(name, phone, email, deadline, address, tags, attachments);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.deadline = deadline;
        this.address = address;
        this.tags.addAll(tags);
        this.attachments.addAll(attachments);
    }

    /**
     * Convenience constructor, to be removed eventually
     */
    public Task(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        this(name, phone, email, new Deadline(new Date(2018, 10, 1)), address, tags, new HashSet<Attachment>());
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Deadline getDeadline() {
        return deadline;
    }

    public Address getAddress() {
        return address;
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
     * Returns true if both persons have the same identity and data fields. This defines a stronger
     * notion of equality between two persons.
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
            && otherTask.getPhone().equals(getPhone())
            && otherTask.getEmail().equals(getEmail())
            && otherTask.getAddress().equals(getAddress())
            && otherTask.getTags().equals(getTags())
            && otherTask.getDeadline().equals(getDeadline())
            && otherTask.getAttachments().equals(getAttachments());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags, deadline, attachments);
    }

    @Override
    public String toString() {
        // TODO: add deadline and attachments
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
            .append(" Phone: ")
            .append(getPhone())
            .append(" Email: ")
            .append(getEmail())
            .append(" Address: ")
            .append(getAddress())
            .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

}
