package seedu.address.ui;

import static seedu.address.logic.parser.CliSyntax.PREFIX_FILENAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILEPATH;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.commons.events.ui.ExecuteCommandEvent;
import seedu.address.commons.events.ui.SelectFileSaveEvent;
import seedu.address.logic.commands.AttachmentCommand;
import seedu.address.model.attachment.Attachment;
import seedu.address.model.task.Task;

/**
 * An UI component that displays information of a {@code Task}.
 */
public class TaskCard extends UiPart<Region> {

    public static final String DEADLINE_FORMAT = "Due %s";
    public static final String PRIORITY_FORMAT = "Priority %s";
    public static final String FREQUENCY_FORMAT = "Frequency %s";

    private static final String FXML = "TaskListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX. As
     * a consequence, UI elements' variable names cannot be set to such keywords or an exception
     * will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on
     * TaskCollection level 4</a>
     */

    public final Task task;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label deadline;
    @FXML
    private Label priority;
    @FXML
    private Label frequency;
    @FXML
    private FlowPane tags;
    @FXML
    private FlowPane attachments;

    public TaskCard(Task task, int displayedIndex) {
        super(FXML);
        this.task = task;
        id.setText(displayedIndex + ". ");
        name.setText(task.getName().value);
        priority.setText(String.format(PRIORITY_FORMAT, task.getPriority().value));
        frequency.setText(String.format(FREQUENCY_FORMAT, task.getFrequency().value));
        deadline.setText(String.format(DEADLINE_FORMAT, task.getDeadline().toString()));
        task.getTags().forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
        task.getAttachments().forEach(attachment ->
            attachments.getChildren().add(buildAttachmentLabel(attachment, displayedIndex)));
    }

    /**
     * Helper method to construct a label for each attachment.
     * Adds a mouse click handler to invoke the attachment get command when the label is clicked
     * @param attachment the attachment
     * @param index index of the attachmentin the current list/UI
     * @return label with mouse click handler
     */
    private Label buildAttachmentLabel(Attachment attachment, int index) {
        Label label = new Label(attachment.getName());
        label.setOnMouseClicked(event -> {
            raise(new SelectFileSaveEvent(file -> {
                String commandText = String.format("%s %d %s %s\"%s\" %s\"%s\"",
                    AttachmentCommand.COMMAND_WORD, index,
                    AttachmentCommand.COMMAND_GET_ACTION,
                    PREFIX_FILENAME, attachment.getName(),
                    PREFIX_FILEPATH, file.getPath()
                );
                raise(new ExecuteCommandEvent(commandText));
            }, attachment.getName()));
        });

        return label;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TaskCard)) {
            return false;
        }

        // state check
        TaskCard card = (TaskCard) other;
        return id.getText().equals(card.id.getText())
            && task.equals(card.task);
    }
}
