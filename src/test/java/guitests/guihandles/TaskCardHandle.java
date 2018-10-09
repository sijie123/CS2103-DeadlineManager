package guitests.guihandles;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMultiset;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.model.task.Task;

/**
 * Provides a handle to a task card in the task list panel.
 */
public class TaskCardHandle extends NodeHandle<Node> {

    private static final String ID_FIELD_ID = "#id";
    private static final String NAME_FIELD_ID = "#name";
    private static final String PRIORITY_FIELD_ID = "#priority";
    private static final String DEADLINE_FIELD_ID = "#deadline";
    private static final String TAGS_FIELD_ID = "#tags";
    private static final String ATTACHMENTS_FIELD_ID = "#attachments";

    private final Label idLabel;
    private final Label nameLabel;
    private final Label priorityLabel;
    private final Label deadlineLabel;
    private final List<Label> tagLabels;
    private final List<Label> attachmentLabels;

    public TaskCardHandle(Node cardNode) {
        super(cardNode);

        idLabel = getChildNode(ID_FIELD_ID);
        nameLabel = getChildNode(NAME_FIELD_ID);
        deadlineLabel = getChildNode(DEADLINE_FIELD_ID);
        priorityLabel = getChildNode(PRIORITY_FIELD_ID);

        Region tagsContainer = getChildNode(TAGS_FIELD_ID);
        tagLabels = tagsContainer
            .getChildrenUnmodifiable()
            .stream()
            .map(Label.class::cast)
            .collect(Collectors.toList());

        Region attachmentsContainer = getChildNode(ATTACHMENTS_FIELD_ID);
        attachmentLabels = tagsContainer
            .getChildrenUnmodifiable()
            .stream()
            .map(Label.class::cast)
            .collect(Collectors.toList());
    }

    public String getId() {
        return idLabel.getText();
    }

    public String getName() {
        return nameLabel.getText();
    }

    public String getPriority() {
        return priorityLabel.getText();
    }

    public String getDeadline() { return deadlineLabel.getText(); }

    public List<String> getTags() {
        return tagLabels
            .stream()
            .map(Label::getText)
            .collect(Collectors.toList());
    }

    public List<String> getAttachments() {
        return attachmentLabels
            .stream()
            .map(Label::getText)
            .collect(Collectors.toList());
    }

    /**
     * Returns true if this handle contains {@code task}.
     */
    public boolean equals(Task task) {
        return getName().equals(task.getName().value)
            && getPriority().equals(task.getPriority().value)
            && getDeadline().equals(task.getDeadline().toString())
            && ImmutableMultiset.copyOf(getTags())
            .equals(ImmutableMultiset.copyOf(task.getTags().stream()
                .map(tag -> tag.tagName)
                .collect(Collectors.toList())))
            && ImmutableMultiset.copyOf(getAttachments())
            .equals(ImmutableMultiset.copyOf(task.getAttachments().stream()
                .map(attachment -> attachment.getName())
                .collect(Collectors.toList())));
    }
}
