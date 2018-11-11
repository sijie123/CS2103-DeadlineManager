package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FREQUENCY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Task;

/**
 * A utility class for Task.
 */
public class TaskUtil {

    /**
     * Returns an add command string for adding the {@code task}.
     */
    public static String getAddCommand(Task task) {
        return AddCommand.COMMAND_WORD + " " + getTaskDetails(task);
    }

    /**
     * Returns the part of command string for the given {@code task}'s details.
     */
    public static String getTaskDetails(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + task.getName().value + " ");
        sb.append(PREFIX_PRIORITY + task.getPriority().toString() + " ");
        sb.append(PREFIX_FREQUENCY + task.getFrequency().toString() + " ");
        sb.append(PREFIX_DEADLINE + task.getDeadline().toString() + " ");
        task.getTags().stream().forEach(
                s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditTaskDescriptor}'s details.
     */
    public static String getEditTaskDescriptorDetails(EditCommand.EditTaskDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName()
                .ifPresent(name -> sb.append(PREFIX_NAME).append(name.value).append(" "));
        descriptor.getPriority()
                .ifPresent(priority -> sb.append(PREFIX_PRIORITY).append(priority.toString()).append(" "));
        descriptor.getFrequency()
                .ifPresent(frequency -> sb.append(PREFIX_FREQUENCY).append(frequency.toString()).append(" "));
        descriptor.getDeadline()
                .ifPresent(deadline -> sb.append(PREFIX_DEADLINE).append(deadline.toString()).append(" "));
        if (descriptor.getTags().isPresent()) {
            Set<Tag> tags = descriptor.getTags().get();
            if (tags.isEmpty()) {
                sb.append(PREFIX_TAG);
            } else {
                tags.forEach(s -> sb.append(PREFIX_TAG).append(s.tagName).append(" "));
            }
        }
        return sb.toString();
    }
}
