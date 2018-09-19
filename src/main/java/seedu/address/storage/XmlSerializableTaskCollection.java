package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.ReadOnlyTaskCollection;
import seedu.address.model.TaskCollection;
import seedu.address.model.task.Task;

/**
 * An Immutable TaskCollection that is serializable to XML format
 */
@XmlRootElement(name = "taskcollection")
public class XmlSerializableTaskCollection {

    public static final String MESSAGE_DUPLICATE_TASK = "Task list contains duplicate task(s).";

    @XmlElement
    private List<XmlAdaptedTask> tasks;

    /**
     * Creates an empty XmlSerializableTaskCollection. This empty constructor is required for
     * marshalling.
     */
    public XmlSerializableTaskCollection() {
        tasks = new ArrayList<>();
    }

    /**
     * Conversion
     */
    public XmlSerializableTaskCollection(ReadOnlyTaskCollection src) {
        this();
        tasks.addAll(
            src.getTaskList().stream().map(XmlAdaptedTask::new).collect(Collectors.toList()));
    }

    /**
     * Converts this addressbook into the model's {@code TaskCollection} object.
     *
     * @throws IllegalValueException if there were any data constraints violated or duplicates in
     *                               the {@code XmlAdaptedTask}.
     */
    public TaskCollection toModelType() throws IllegalValueException {
        TaskCollection taskCollection = new TaskCollection();
        for (XmlAdaptedTask p : tasks) {
            Task task = p.toModelType();
            if (taskCollection.hasTask(task)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_TASK);
            }
            taskCollection.addPerson(task);
        }
        return taskCollection;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlSerializableTaskCollection)) {
            return false;
        }
        return tasks.equals(((XmlSerializableTaskCollection) other).tasks);
    }
}
